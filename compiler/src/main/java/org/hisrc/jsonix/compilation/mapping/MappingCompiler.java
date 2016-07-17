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

package org.hisrc.jsonix.compilation.mapping;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.xml.namespace.QName;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hisrc.jscm.codemodel.JSCodeModel;
import org.hisrc.jscm.codemodel.expression.JSArrayLiteral;
import org.hisrc.jscm.codemodel.expression.JSAssignmentExpression;
import org.hisrc.jscm.codemodel.expression.JSMemberExpression;
import org.hisrc.jscm.codemodel.expression.JSObjectLiteral;
import org.hisrc.jsonix.compilation.mapping.typeinfo.ClassInfoCompiler;
import org.hisrc.jsonix.compilation.mapping.typeinfo.CreateTypeInfoCompiler;
import org.hisrc.jsonix.compilation.mapping.typeinfo.EnumLeafInfoCompiler;
import org.hisrc.jsonix.compilation.mapping.typeinfo.TypeInfoCompiler;
import org.hisrc.jsonix.definition.Mapping;
import org.hisrc.jsonix.definition.MappingDependency;
import org.hisrc.jsonix.definition.Module;
import org.hisrc.jsonix.definition.Modules;
import org.hisrc.jsonix.definition.Output;
import org.hisrc.jsonix.naming.Naming;
import org.jvnet.jaxb2_commons.xml.bind.model.MClassInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MElementInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MElementTypeInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MEnumLeafInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MPackageInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MPropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MTypeInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.origin.MClassInfoOrigin;
import org.jvnet.jaxb2_commons.xml.bind.model.origin.MElementInfoOrigin;
import org.jvnet.jaxb2_commons.xml.bind.model.origin.MEnumLeafInfoOrigin;
import org.jvnet.jaxb2_commons.xml.bind.model.origin.MOriginated;
import org.jvnet.jaxb2_commons.xml.bind.model.origin.MPropertyInfoOrigin;

public class MappingCompiler<T, C extends T> {
	public static final String DEFAULT_SCOPED_NAME_DELIMITER = ".";

	private final JSCodeModel codeModel;
	public final String mappingName;

	private final String targetNamespaceURI;
	private final String defaultElementNamespaceURI;
	private final String defaultAttributeNamespaceURI;

	private final Modules<T, C> modules;
	private Naming naming;
	private Mapping<T, C> mapping;
	private Output output;

	public MappingCompiler(JSCodeModel codeModel, Modules<T, C> modules, Module<T, C> module, Output output,
			Mapping<T, C> mapping) {
		Validate.notNull(codeModel);
		Validate.notNull(modules);
		Validate.notNull(module);
		Validate.notNull(mapping);
		Validate.notNull(output);
		this.codeModel = codeModel;
		this.modules = modules;
		this.mapping = mapping;
		this.mappingName = mapping.getMappingName();
		this.targetNamespaceURI = mapping.getTargetNamespaceURI();
		this.defaultElementNamespaceURI = mapping.getDefaultElementNamespaceURI();
		this.defaultAttributeNamespaceURI = mapping.getDefaultAttributeNamespaceURI();

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

		if (!(this.targetNamespaceURI.equals(this.defaultElementNamespaceURI))) {
			mappingBody.append(naming.targetNamespaceURI(), codeModel.string(this.targetNamespaceURI));
		}

		if (!StringUtils.isEmpty(this.defaultElementNamespaceURI)) {
			mappingBody.append(naming.defaultElementNamespaceURI(), codeModel.string(this.defaultElementNamespaceURI));
		}

		if (!StringUtils.isEmpty(this.defaultAttributeNamespaceURI)) {
			mappingBody.append(naming.defaultAttributeNamespaceURI(),
					codeModel.string(this.defaultAttributeNamespaceURI));
		}

		final JSArrayLiteral dependencies = codeModel.array();
		compileDependencies(dependencies);
		if (!dependencies.getElements().isEmpty()) {
			mappingBody.append(naming.dependencies(), dependencies);
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

	private void compileDependencies(JSArrayLiteral dependencies) {
		final Collection<MappingDependency<T, C>> mappingDependencies = mapping.getDirectDependencies();
		final Set<String> mappingDependencyNames = new LinkedHashSet<String>();
		for (final MappingDependency<T, C> mappingDependency : mappingDependencies) {
			final MPackageInfo dependencyPackageInfo = mappingDependency.getPackageInfo();
			final String dependencyPackageName = dependencyPackageInfo.getPackageName();
			final String dependencyMappingName = modules.getMappingName(dependencyPackageName);
			mappingDependencyNames.add(dependencyMappingName);
		}
		for (final String mappingDependencyName : mappingDependencyNames) {
			dependencies.append(getCodeModel().string(mappingDependencyName));
		}
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
		return new ClassInfoCompiler<T, C>(classInfo).compile(this);
	}

	private JSObjectLiteral compileEnumLeafInfo(MEnumLeafInfo<T, C> enumLeafInfo) {
		return new EnumLeafInfoCompiler<T, C>(enumLeafInfo).compile(this);
	}

	private void compileElementInfos(JSArrayLiteral eis) {
		for (MElementInfo<T, C> elementInfo : mapping.getElementInfos()) {
			eis.append(compileElementInfo(elementInfo));
		}
	}

	private JSObjectLiteral compileElementInfo(MElementInfo<T, C> elementInfo) {
		MTypeInfo<T, C> typeInfo = elementInfo.getTypeInfo();
		MClassInfo<T, C> scope = elementInfo.getScope();
		QName substitutionHead = elementInfo.getSubstitutionHead();

		final JSObjectLiteral value = this.codeModel.object();
		typeInfo.acceptTypeInfoVisitor(
				new CreateTypeInfoDelaration<T, C, MElementInfo<T, C>, MElementInfoOrigin>(this, elementInfo, value));
		QName elementName = elementInfo.getElementName();
		value.append(naming.elementName(), createElementNameExpression(elementName));

		if (scope != null) {
			value.append(naming.scope(), getTypeInfoCompiler(elementInfo, scope).createTypeInfoDeclaration(this));
		}
		if (substitutionHead != null) {
			value.append(naming.substitutionHead(), createElementNameExpression(substitutionHead));
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
	private JSMemberExpression createNameExpression(final QName name, final String defaultNamespaceURI) {
		final String draftNamespaceURI = name.getNamespaceURI();
		final String namespaceURI = StringUtils.isEmpty(draftNamespaceURI) ? null : draftNamespaceURI;

		if (ObjectUtils.equals(defaultNamespaceURI, namespaceURI)) {
			return this.codeModel.string(name.getLocalPart());
		} else {

			final JSObjectLiteral nameExpression = this.codeModel.object();

			nameExpression.append(naming.localPart(), this.codeModel.string(name.getLocalPart()));

			if (!StringUtils.isEmpty(namespaceURI)) {
				nameExpression.append(naming.namespaceURI(), this.codeModel.string(namespaceURI));

			}
			return nameExpression;
		}
	}

	public TypeInfoCompiler<T, C> getTypeInfoCompiler(MClassInfo<T, C> classInfo, MTypeInfo<T, C> typeInfo) {
		return getTypeInfoCompiler((MOriginated<MClassInfoOrigin>) classInfo, typeInfo);
	}

	public TypeInfoCompiler<T, C> getTypeInfoCompiler(MEnumLeafInfo<T, C> enumLeafInfo, MTypeInfo<T, C> typeInfo) {
		return getTypeInfoCompiler((MOriginated<MEnumLeafInfoOrigin>) enumLeafInfo, typeInfo);
	}

	public TypeInfoCompiler<T, C> getTypeInfoCompiler(MPropertyInfo<T, C> propertyInfo, MTypeInfo<T, C> typeInfo) {
		return getTypeInfoCompiler((MOriginated<MPropertyInfoOrigin>) propertyInfo, typeInfo);
	}

	public <M extends MElementTypeInfo<T, C, O>, O> TypeInfoCompiler<T, C> getTypeInfoCompiler(M elementInfo,
			MTypeInfo<T, C> typeInfo) {
		return getTypeInfoCompiler((MOriginated<O>) elementInfo, typeInfo);
	}

	public <O> TypeInfoCompiler<T, C> getTypeInfoCompiler(MOriginated<O> originated, MTypeInfo<T, C> typeInfo) {

		return typeInfo.acceptTypeInfoVisitor(new CreateTypeInfoCompiler<T, C, O>(originated));
	}
}