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

package org.hisrc.jsonix.compiler;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.commons.lang3.Validate;
import org.hisrc.jscm.codemodel.JSCodeModel;
import org.hisrc.jscm.codemodel.expression.JSArrayLiteral;
import org.hisrc.jscm.codemodel.expression.JSAssignmentExpression;
import org.hisrc.jscm.codemodel.expression.JSObjectLiteral;
import org.hisrc.jscm.codemodel.impl.CodeModelImpl;
import org.hisrc.jsonix.compilation.Mapping;
import org.hisrc.jsonix.compilation.Module;
import org.hisrc.jsonix.compilation.Modules;
import org.hisrc.jsonix.compilation.Output;
import org.hisrc.jsonix.compiler.log.Log;
import org.hisrc.jsonix.compiler.log.SystemLog;
import org.jvnet.jaxb2_commons.xml.bind.model.MClassInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MClassTypeInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MElementInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MEnumConstantInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MEnumLeafInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MModelInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MPackageInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MPackaged;
import org.jvnet.jaxb2_commons.xml.bind.model.MPropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MTypeInfo;

public class JsonixCompiler<T, C extends T> {

	public Log log = new SystemLog();
	public static final String DEFAULT_SCOPED_NAME_DELIMITER = ".";

	private final JSCodeModel codeModel = new CodeModelImpl();

	private final MModelInfo<T, C> model;

	private final Modules modules;

	public JsonixCompiler(final MModelInfo<T, C> model, Modules modules) {
		Validate.notNull(model);
		Validate.notNull(modules);
		this.model = model;
		this.modules = modules;
	}

	public String getSpaceName(final MPackaged packaged) {
		return modules.getMappingName(packaged.getPackageInfo()
				.getPackageName());
	}

	// private JsonixModule getModule(final MPackageInfo packageInfo) {
	// String packageName = packageInfo.getPackageName();
	// final boolean blankPackage;
	// if (StringUtils.isBlank(packageName)) {
	// packageName = "";
	// blankPackage = true;
	// } else {
	// blankPackage = false;
	// }
	// JsonixModule module = this.defaultModules.get(packageName);
	// if (module == null) {
	// final PackageInfoQNameAnalyzer<T, C> analyzer = new
	// PackageInfoQNameAnalyzer<T, C>(
	// this.model);
	//
	// final String defaultElementNamespaceURI = analyzer
	// .getMostUsedElementNamespaceURI(packageInfo);
	// final String defaultAttributeNamespaceURI = analyzer
	// .getMostUsedAttributeNamespaceURI(packageInfo);
	//
	// Mapping packageMapping = packageMappings.get(packageName);
	//
	// if (packageMapping == null) {
	//
	// packageMapping = new Mapping();
	// packageMapping.setPackage(packageName);
	// packageMappings.put(packageName, packageMapping);
	// }
	//
	// if (StringUtils.isBlank(packageMapping
	// .getDefaultElementNamespaceURI())) {
	// packageMapping
	// .setDefaultElementNamespaceURI(defaultElementNamespaceURI);
	// }
	//
	// if (StringUtils.isBlank(packageMapping
	// .getDefaultAttributeNamespaceURI())) {
	// packageMapping
	// .setDefaultAttributeNamespaceURI(defaultAttributeNamespaceURI);
	// }
	//
	// if (StringUtils.isBlank(packageMapping.getSpaceName())) {
	// packageMapping.setSpaceName(blankPackage ? "generated"
	// : packageName.replace('.', '_'));
	// }
	// if (packageMapping.getOutputPackageName() == null) {
	// packageMapping.setOutputPackageName("");
	// }
	//
	// if (packageMapping.getDirectory() == null) {
	// packageMapping.setDirectory((blankPackage ? "" : packageMapping
	// .getOutputPackageName().replace('.', '/')));
	// }
	//
	// if (packageMapping.getFileName() == null) {
	// packageMapping.setFileName(packageMapping.getSpaceName()
	// + ".js");
	//
	// }
	//
	// module = new JsonixModule(this.codeModel, this.naming,
	// packageMapping);
	// this.defaultModules.put(packageName, module);
	// }
	// return module;
	// }

	public Iterable<JsonixModule> compile() {

		// for (String packageName : this.analyzer.getPackageNames()) {
		// this.log.debug(MessageFormat.format(
		// "Compiling space for package [{0}].", packageName));
		// final SpaceBuilder spaceBuilder = this.analyzer
		// .createSpaceBuilder(packageName);
		//
		// }

		final List<JsonixModule> compiledMappings = new LinkedList<JsonixModule>();

		for (Module module : modules.getModules()) {
			for (final Output output : module.getOutputs()) {
				for (Mapping mapping : module.getMappings()) {
					final MPackageInfo packageInfo = mapping.getPackageInfo();
					final JsonixModule m = new JsonixModule(this.codeModel,
							mapping, output);
					compileClassInfos(m, packageInfo, model.getClassInfos());
					compileEnumLeafInfos(m, packageInfo,
							model.getEnumLeafInfos());
					compileElementInfos(m, packageInfo, model.getElementInfos());
					compiledMappings.add(m);
				}
			}
		}
		return compiledMappings;
	}

	public JSAssignmentExpression getTypeInfoDeclaration(JsonixModule module,
			MTypeInfo<T, C> typeInfo) {
		return typeInfo
				.acceptTypeInfoVisitor(new CreateTypeInfoDeclarationVisitor<T, C>(
						this, this.codeModel, module));
	}

	public JSArrayLiteral compileClassInfos(JsonixModule module,
			MPackageInfo packageInfo, Collection<MClassInfo<T, C>> classInfos) {
		final JSArrayLiteral classInfoMappings = this.codeModel.array();
		for (MClassInfo<T, C> classInfo : classInfos) {
			if (packageInfo == classInfo.getPackageInfo()) {
				classInfoMappings.append(compileClassInfo(module, classInfo));
			}
		}
		return classInfoMappings;
	}

	public JSArrayLiteral compileEnumLeafInfos(JsonixModule module,
			MPackageInfo packageInfo,
			Collection<MEnumLeafInfo<T, C>> enumLeafInfos) {
		final JSArrayLiteral mappings = this.codeModel.array();
		for (MEnumLeafInfo<T, C> enumLeafInfo : enumLeafInfos) {
			if (packageInfo == enumLeafInfo.getPackageInfo()) {
				mappings.append(compileEnumLeafInfo(module, enumLeafInfo));
			}
		}
		return mappings;
	}

	public JSObjectLiteral compileClassInfo(JsonixModule module,
			MClassInfo<T, C> classInfo) {
		final JSObjectLiteral classInfoMapping = this.codeModel.object();
		classInfoMapping.append(module.getOutput().getNaming().localName(),
				this.codeModel.string(classInfo
						.getContainerLocalName(DEFAULT_SCOPED_NAME_DELIMITER)));

		final MClassTypeInfo<T, C> baseTypeInfo = classInfo.getBaseTypeInfo();
		if (baseTypeInfo != null) {
			classInfoMapping.append(module.getOutput().getNaming()
					.baseTypeInfo(),
					getTypeInfoDeclaration(module, baseTypeInfo));
		}
		final JSArrayLiteral ps = compilePropertyInfos(module, classInfo);
		if (!ps.getElements().isEmpty()) {
			classInfoMapping.append(module.getOutput().getNaming()
					.propertyInfos(), ps);
		}
		module.registerTypeInfo(classInfoMapping);
		return classInfoMapping;
	}

	public JSObjectLiteral compileEnumLeafInfo(JsonixModule module,
			MEnumLeafInfo<T, C> enumLeafInfo) {
		final JSObjectLiteral mapping = this.codeModel.object();
		mapping.append(module.getOutput().getNaming().type(), this.codeModel
				.string(module.getOutput().getNaming().enumInfo()));
		mapping.append(module.getOutput().getNaming().localName(),
				this.codeModel.string(enumLeafInfo
						.getContainerLocalName(DEFAULT_SCOPED_NAME_DELIMITER)));

		final MTypeInfo<T, C> baseTypeInfo = enumLeafInfo.getBaseTypeInfo();
		if (baseTypeInfo != null) {
			final JSAssignmentExpression baseTypeInfoDeclaration = getTypeInfoDeclaration(
					module, baseTypeInfo);
			if (!baseTypeInfoDeclaration
					.acceptExpressionVisitor(new CheckValueStringLiteralExpressionVisitor(
							"String"))) {
				mapping.append(module.getOutput().getNaming().baseTypeInfo(),
						baseTypeInfoDeclaration);
			}
		}
		mapping.append(module.getOutput().getNaming().values(),
				compileEnumConstrantInfos(enumLeafInfo));
		module.registerTypeInfo(mapping);
		return mapping;
	}

	public JSArrayLiteral compilePropertyInfos(final JsonixModule module,
			MClassInfo<T, C> classInfo) {
		final JSArrayLiteral propertyInfoMappings = this.codeModel.array();
		for (MPropertyInfo<T, C> propertyInfo : classInfo.getProperties()) {
			propertyInfoMappings.append(propertyInfo
					.acceptPropertyInfoVisitor(new PropertyInfoVisitor<T, C>(
							JsonixCompiler.this,
							JsonixCompiler.this.codeModel,
							module)));
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

	public JSArrayLiteral compileElementInfos(JsonixModule module,
			MPackageInfo packageInfo,
			Collection<MElementInfo<T, C>> elementInfos) {
		final JSArrayLiteral elementInfoMappings = this.codeModel.array();
		for (MElementInfo<T, C> elementInfo : elementInfos) {
			if (packageInfo == elementInfo.getPackageInfo()) {
				// JsonixModule module = getModule(elementInfo);
				elementInfoMappings.append(compileElementInfo(module,
						elementInfo));
			}
		}
		return elementInfoMappings;
	}

	public JSObjectLiteral compileElementInfo(JsonixModule module,
			MElementInfo<T, C> elementInfo) {
		MTypeInfo<T, C> typeInfo = elementInfo.getTypeInfo();
		MTypeInfo<T, C> scope = elementInfo.getScope();
		QName substitutionHead = elementInfo.getSubstitutionHead();

		final JSObjectLiteral value = this.codeModel.object();
		module.registerElementInfo(value);
		JSAssignmentExpression typeInfoDeclaration = getTypeInfoDeclaration(
				module, typeInfo);
		QName elementName = elementInfo.getElementName();
		value.append(module.getOutput().getNaming().elementName(),
				module.createElementNameExpression(elementName));
		if (typeInfoDeclaration != null) {
			if (!typeInfoDeclaration
					.acceptExpressionVisitor(new CheckValueStringLiteralExpressionVisitor(
							"String"))) {
				value.append(module.getOutput().getNaming().typeInfo(),
						typeInfoDeclaration);
			}
		}

		if (scope != null) {
			value.append(module.getOutput().getNaming().scope(),
					getTypeInfoDeclaration(module, scope));
		}
		if (substitutionHead != null) {
			value.append(module.getOutput().getNaming().substitutionHead(),
					module.createElementNameExpression(substitutionHead));
		}
		return value;
	}
}
