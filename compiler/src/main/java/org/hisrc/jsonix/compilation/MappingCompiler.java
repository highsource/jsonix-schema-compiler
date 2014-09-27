/**
 * Jsonix is a JavaScript library which allows you to convert between XML
 * and JavaScript object structures.
 *
 * Copyright (c) 2010 - 2014, Alexey Valikov, Highsource.org
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice, this
 *   list of conditions and the following disclaimer in the documentation and/or
 *   other materials provided with the distribution.
 *
 * * Neither the name of the copyright holder nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.hisrc.jsonix.compilation;

import javax.xml.namespace.QName;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hisrc.jscm.codemodel.JSCodeModel;
import org.hisrc.jscm.codemodel.expression.JSArrayLiteral;
import org.hisrc.jscm.codemodel.expression.JSAssignmentExpression;
import org.hisrc.jscm.codemodel.expression.JSMemberExpression;
import org.hisrc.jscm.codemodel.expression.JSObjectLiteral;
import org.hisrc.jsonix.definition.Mapping;
import org.hisrc.jsonix.definition.Module;
import org.hisrc.jsonix.definition.Modules;
import org.hisrc.jsonix.definition.Output;
import org.hisrc.jsonix.naming.Naming;
import org.jvnet.jaxb2_commons.xml.bind.model.MClassInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MClassTypeInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MElementInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MEnumConstantInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MEnumLeafInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MPropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MTypeInfo;

public class MappingCompiler<T, C extends T> {
	public static final String DEFAULT_SCOPED_NAME_DELIMITER = ".";

	private final JSCodeModel codeModel;
	public final String mappingName;

	private final String defaultElementNamespaceURI;
	private final String defaultAttributeNamespaceURI;

	private final Modules<T, C> modules;
	private Naming naming;
	private Mapping<T, C> mapping;
	private Output output;

	public MappingCompiler(JSCodeModel codeModel, Modules<T, C> modules,
			Module<T, C> module, Output output, Mapping<T, C> mapping) {
		Validate.notNull(codeModel);
		Validate.notNull(modules);
		Validate.notNull(module);
		Validate.notNull(mapping);
		Validate.notNull(output);
		this.codeModel = codeModel;
		this.modules = modules;
		this.mapping = mapping;
		this.mappingName = mapping.getMappingName();
		this.defaultElementNamespaceURI = mapping
				.getDefaultElementNamespaceURI();
		this.defaultAttributeNamespaceURI = mapping
				.getDefaultAttributeNamespaceURI();

		this.output = output;
		this.naming = output.getNaming();
	}

	public Modules<T, C> getModules() {
		return modules;
	}

	public JSCodeModel getCodeModel() {
		return codeModel;
	}

	public Naming getNaming() {
		return naming;
	}

	public Mapping<T, C> getMapping() {
		return mapping;
	}

	public Output getOutput() {
		return output;
	}

	public JSObjectLiteral compile() {

		final JSObjectLiteral mappingBody = codeModel.object();

		mappingBody.append(naming.name(), codeModel.string(this.mappingName));

		if (!StringUtils.isEmpty(this.defaultElementNamespaceURI)) {
			mappingBody.append(naming.defaultElementNamespaceURI(),
					codeModel.string(this.defaultElementNamespaceURI));
		}

		if (!StringUtils.isEmpty(this.defaultAttributeNamespaceURI)) {
			mappingBody.append(naming.defaultAttributeNamespaceURI(),
					codeModel.string(this.defaultAttributeNamespaceURI));
		}

		final JSArrayLiteral typeInfos = codeModel.array();
		mappingBody.append(naming.typeInfos(), typeInfos);

		final JSArrayLiteral elementInfos = codeModel.array();
		mappingBody.append(naming.elementInfos(), elementInfos);

		compileClassInfos(typeInfos);
		compileEnumLeafInfos(typeInfos);
		compileElementInfos(elementInfos);

		return mappingBody;
	}

	private void compileClassInfos(JSArrayLiteral typeInfos) {
		for (MClassInfo<T, C> classInfo : mapping.getClassInfos()) {
			typeInfos.append(compileClassInfo(classInfo));
		}
	}

	private void compileEnumLeafInfos(JSArrayLiteral typeInfos) {
		for (MEnumLeafInfo<T, C> enumLeafInfo : mapping.getEnumLeafInfos()) {
			typeInfos.append(compileEnumLeafInfo(enumLeafInfo));
		}
	}

	private JSObjectLiteral compileClassInfo(MClassInfo<T, C> classInfo) {
		final JSObjectLiteral classInfoMapping = this.codeModel.object();
		classInfoMapping.append(naming.localName(), this.codeModel
				.string(classInfo
						.getContainerLocalName(DEFAULT_SCOPED_NAME_DELIMITER)));
		final MClassTypeInfo<T, C> baseTypeInfo = classInfo.getBaseTypeInfo();
		if (baseTypeInfo != null) {
			classInfoMapping.append(naming.baseTypeInfo(),
					getTypeInfoDeclaration(baseTypeInfo));
		}
		final JSArrayLiteral ps = compilePropertyInfos(classInfo);
		if (!ps.getElements().isEmpty()) {
			classInfoMapping.append(naming.propertyInfos(), ps);
		}
		return classInfoMapping;
	}

	private JSObjectLiteral compileEnumLeafInfo(MEnumLeafInfo<T, C> enumLeafInfo) {
		final JSObjectLiteral mapping = this.codeModel.object();
		mapping.append(naming.type(), this.codeModel.string(naming.enumInfo()));
		mapping.append(naming.localName(), this.codeModel.string(enumLeafInfo
				.getContainerLocalName(DEFAULT_SCOPED_NAME_DELIMITER)));

		final MTypeInfo<T, C> baseTypeInfo = enumLeafInfo.getBaseTypeInfo();
		if (baseTypeInfo != null) {
			final JSAssignmentExpression baseTypeInfoDeclaration = getTypeInfoDeclaration(baseTypeInfo);
			if (!baseTypeInfoDeclaration
					.acceptExpressionVisitor(new CheckValueStringLiteralExpressionVisitor(
							"String"))) {
				mapping.append(naming.baseTypeInfo(), baseTypeInfoDeclaration);
			}
		}
		mapping.append(naming.values(), compileEnumConstrantInfos(enumLeafInfo));
		return mapping;
	}

	private JSArrayLiteral compilePropertyInfos(MClassInfo<T, C> classInfo) {
		final JSArrayLiteral propertyInfoMappings = this.codeModel.array();
		for (MPropertyInfo<T, C> propertyInfo : classInfo.getProperties()) {
			if (mapping.getPropertyInfos().contains(propertyInfo)) {
				propertyInfoMappings
						.append(propertyInfo
								.acceptPropertyInfoVisitor(new PropertyInfoVisitor<T, C>(
										this)));
			}
		}
		return propertyInfoMappings;
	}

	private JSArrayLiteral compileEnumConstrantInfos(
			MEnumLeafInfo<T, C> enumLeafInfo) {
		final JSArrayLiteral mappings = this.codeModel.array();
		for (MEnumConstantInfo<T, C> enumConstantInfo : enumLeafInfo
				.getConstants()) {
			mappings.append(this.codeModel.string(enumConstantInfo
					.getLexicalValue()));
		}
		return mappings;
	}

	private void compileElementInfos(JSArrayLiteral eis) {
		for (MElementInfo<T, C> elementInfo : mapping.getElementInfos()) {
			eis.append(compileElementInfo(elementInfo));
		}
	}

	private JSObjectLiteral compileElementInfo(MElementInfo<T, C> elementInfo) {
		MTypeInfo<T, C> typeInfo = elementInfo.getTypeInfo();
		MTypeInfo<T, C> scope = elementInfo.getScope();
		QName substitutionHead = elementInfo.getSubstitutionHead();

		final JSObjectLiteral value = this.codeModel.object();
		JSAssignmentExpression typeInfoDeclaration = getTypeInfoDeclaration(typeInfo);
		QName elementName = elementInfo.getElementName();
		value.append(naming.elementName(),
				createElementNameExpression(elementName));
		if (typeInfoDeclaration != null) {
			if (!typeInfoDeclaration
					.acceptExpressionVisitor(new CheckValueStringLiteralExpressionVisitor(
							"String"))) {
				value.append(naming.typeInfo(), typeInfoDeclaration);
			}
		}

		if (scope != null) {
			value.append(naming.scope(), getTypeInfoDeclaration(scope));
		}
		if (substitutionHead != null) {
			value.append(naming.substitutionHead(),
					createElementNameExpression(substitutionHead));
		}
		return value;
	}

	public JSMemberExpression createElementNameExpression(final QName name) {
		Validate.notNull(name);
		return createNameExpression(name, this.defaultElementNamespaceURI);
	}

	public JSMemberExpression createAttributeNameExpression(final QName name) {
		Validate.notNull(name);
		return createNameExpression(name, this.defaultAttributeNamespaceURI);
	}

	@SuppressWarnings("deprecation")
	private JSMemberExpression createNameExpression(final QName name,
			final String defaultNamespaceURI) {
		final String draftNamespaceURI = name.getNamespaceURI();
		final String namespaceURI = StringUtils.isEmpty(draftNamespaceURI) ? null
				: draftNamespaceURI;

		if (ObjectUtils.equals(defaultNamespaceURI, namespaceURI)) {
			return this.codeModel.string(name.getLocalPart());
		} else {

			final JSObjectLiteral nameExpression = this.codeModel.object();

			nameExpression.append(naming.localPart(),
					this.codeModel.string(name.getLocalPart()));

			if (!StringUtils.isEmpty(namespaceURI)) {
				nameExpression.append(naming.namespaceURI(),
						this.codeModel.string(namespaceURI));

			}
			return nameExpression;
		}
	}

	public JSAssignmentExpression getTypeInfoDeclaration(
			MTypeInfo<T, C> typeInfo) {
		return typeInfo
				.acceptTypeInfoVisitor(new CreateTypeInfoDeclarationVisitor<T, C>(
						this));
	}
}