package org.hisrc.jsonix.compilation;

import javax.xml.namespace.QName;

import org.apache.commons.lang3.Validate;
import org.hisrc.jscm.codemodel.JSCodeModel;
import org.hisrc.jscm.codemodel.expression.JSArrayLiteral;
import org.hisrc.jscm.codemodel.expression.JSAssignmentExpression;
import org.hisrc.jscm.codemodel.expression.JSMemberExpression;
import org.hisrc.jscm.codemodel.expression.JSObjectLiteral;
import org.hisrc.jsonix.naming.Naming;
import org.jvnet.jaxb2_commons.xml.bind.model.MAnyAttributePropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MAnyElementPropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MAttributePropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MBuiltinLeafInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MElementPropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MElementRefPropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MElementRefsPropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MElementTypeInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MElementTypeInfos;
import org.jvnet.jaxb2_commons.xml.bind.model.MElementsPropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MMixable;
import org.jvnet.jaxb2_commons.xml.bind.model.MPropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MPropertyInfoVisitor;
import org.jvnet.jaxb2_commons.xml.bind.model.MTypeInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MTyped;
import org.jvnet.jaxb2_commons.xml.bind.model.MValuePropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MWildcard;
import org.jvnet.jaxb2_commons.xml.bind.model.MWrappable;
import org.jvnet.jaxb2_commons.xml.bind.model.util.DefaultTypeInfoVisitor;

final class PropertyInfoVisitor<T, C extends T> implements
		MPropertyInfoVisitor<T, C, JSObjectLiteral> {

	private final JSCodeModel codeModel;
	private final MappingCompiler<T, C> mappingCompiler;
	private final Naming naming;

	public PropertyInfoVisitor(MappingCompiler<T, C> mappingCompiler) {
		Validate.notNull(mappingCompiler);
		this.codeModel = mappingCompiler.getCodeModel();
		this.mappingCompiler = mappingCompiler;
		this.naming = mappingCompiler.getNaming();
	}

	private void createPropertyInfoOptions(MPropertyInfo<T, C> propertyInfo,
			JSObjectLiteral options) {
		options.append(naming.name(),
				this.codeModel.string(propertyInfo.getPrivateName()));
		if (propertyInfo.isCollection()) {
			options.append(naming.collection(), this.codeModel._boolean(true));
		}
	}

	private void createTypedOptions(MTyped<T, C> info,
			final JSObjectLiteral options) {
		final MTypeInfo<T, C> typeInfo = info.getTypeInfo();

		typeInfo.acceptTypeInfoVisitor(new DefaultTypeInfoVisitor<T, C, Void>() {
			@Override
			public Void visitTypeInfo(MTypeInfo<T, C> typeInfo) {
				final JSAssignmentExpression typeInfoDeclaration = PropertyInfoVisitor.this.mappingCompiler
						.getTypeInfoDeclaration(typeInfo);
				if (!typeInfoDeclaration
						.acceptExpressionVisitor(new CheckValueStringLiteralExpressionVisitor(
								"String"))) {
					options.append(naming.typeInfo(), typeInfoDeclaration);
				}
				return null;
			}

			@Override
			public Void visitBuiltinLeafInfo(MBuiltinLeafInfo<T, C> info) {
				return super.visitBuiltinLeafInfo(info);
			}
		});

	}

	private void createWrappableOptions(MWrappable info, JSObjectLiteral options) {
		final QName wrapperElementName = info.getWrapperElementName();
		if (wrapperElementName != null) {
			options.append(naming.wrapperElementName(), mappingCompiler
					.createElementNameExpression(wrapperElementName));
		}
	}

	private void createElementTypeInfoOptions(MElementTypeInfo<T, C> info,
			JSObjectLiteral options) {
		final QName elementName = info.getElementName();
		options.append(naming.elementName(),
				mappingCompiler.createElementNameExpression(elementName));
		createTypedOptions(info, options);
	}

	private void createElementTypeInfoOptions(MElementTypeInfo<T, C> info,
			String privateName, QName elementName, JSObjectLiteral options) {
		JSMemberExpression elementNameExpression = mappingCompiler
				.createElementNameExpression(elementName);
		if (!elementNameExpression
				.acceptExpressionVisitor(new CheckValueStringLiteralExpressionVisitor(
						privateName))) {
			options.append(naming.elementName(), elementNameExpression);
		}
		createTypedOptions(info, options);
	}

	private void createWildcardOptions(MWildcard info, JSObjectLiteral options) {
		if (!info.isDomAllowed()) {
			options.append(naming.allowDom(), this.codeModel._boolean(false));
		}
		if (!info.isTypedObjectAllowed()) {
			options.append(naming.allowTypedObject(),
					this.codeModel._boolean(false));
		}
	}

	private void createMixableOptions(MMixable info, JSObjectLiteral options) {
		if (!info.isMixed()) {
			options.append(naming.mixed(), this.codeModel._boolean(false));
		}
	}

	public JSObjectLiteral visitElementPropertyInfo(
			MElementPropertyInfo<T, C> info) {
		JSObjectLiteral options = this.codeModel.object();
		// options.append(naming.type(),
		// this.codeModel.string(naming.element()));
		createPropertyInfoOptions(info, options);
		createWrappableOptions(info, options);
		createElementTypeInfoOptions(info, info.getPrivateName(),
				info.getElementName(), options);
		return options;
	}

	public JSObjectLiteral visitElementsPropertyInfo(
			MElementsPropertyInfo<T, C> info) {
		JSObjectLiteral options = this.codeModel.object();
		createPropertyInfoOptions(info, options);
		createWrappableOptions(info, options);
		createElementTypeInfosOptions(info, options);
		options.append(naming.type(), this.codeModel.string(naming.elements()));
		return options;
	}

	private void createElementTypeInfosOptions(MElementTypeInfos<T, C> info,
			JSObjectLiteral options) {
		if (!info.getElementTypeInfos().isEmpty()) {
			final JSArrayLiteral elementTypeInfos = this.codeModel.array();
			options.append(naming.elementTypeInfos(), elementTypeInfos);
			for (MElementTypeInfo<T, C> elementTypeInfo : info
					.getElementTypeInfos()) {
				final JSObjectLiteral elementTypeInfoOptions = this.codeModel
						.object();
				createElementTypeInfoOptions(elementTypeInfo,
						elementTypeInfoOptions);
				elementTypeInfos.append(elementTypeInfoOptions);
			}
		}
	}

	public JSObjectLiteral visitAnyElementPropertyInfo(
			MAnyElementPropertyInfo<T, C> info) {
		JSObjectLiteral options = this.codeModel.object();
		createPropertyInfoOptions(info, options);
		createWildcardOptions(info, options);
		createMixableOptions(info, options);
		options.append(naming.type(),
				this.codeModel.string(naming.anyElement()));
		return options;
	}

	public JSObjectLiteral visitAttributePropertyInfo(
			MAttributePropertyInfo<T, C> info) {
		JSObjectLiteral options = this.codeModel.object();
		createPropertyInfoOptions(info, options);
		createTypedOptions(info, options);

		final JSMemberExpression attributeNameExpression = mappingCompiler
				.createAttributeNameExpression(info.getAttributeName());
		if (!attributeNameExpression
				.acceptExpressionVisitor(new CheckValueStringLiteralExpressionVisitor(
						info.getPrivateName()))) {
			options.append(naming.attributeName(), attributeNameExpression);
		}
		options.append(naming.type(), this.codeModel.string(naming.attribute()));
		return options;
	}

	public JSObjectLiteral visitAnyAttributePropertyInfo(
			MAnyAttributePropertyInfo<T, C> info) {
		JSObjectLiteral options = this.codeModel.object();
		createPropertyInfoOptions(info, options);
		options.append(naming.type(),
				this.codeModel.string(naming.anyAttribute()));
		return options;
	}

	public JSObjectLiteral visitValuePropertyInfo(MValuePropertyInfo<T, C> info) {
		JSObjectLiteral options = this.codeModel.object();
		createPropertyInfoOptions(info, options);
		createTypedOptions(info, options);
		options.append(naming.type(), this.codeModel.string(naming.value()));
		return options;
	}

	public JSObjectLiteral visitElementRefPropertyInfo(
			MElementRefPropertyInfo<T, C> info) {
		JSObjectLiteral options = this.codeModel.object();
		createPropertyInfoOptions(info, options);
		createMixableOptions(info, options);
		createWrappableOptions(info, options);
		createWildcardOptions(info, options);
		createElementTypeInfoOptions(info, info.getPrivateName(),
				info.getElementName(), options);
		options.append(naming.type(),
				this.codeModel.string(naming.elementRef()));
		return options;
	}

	public JSObjectLiteral visitElementRefsPropertyInfo(
			MElementRefsPropertyInfo<T, C> info) {
		JSObjectLiteral options = this.codeModel.object();
		createPropertyInfoOptions(info, options);
		createMixableOptions(info, options);
		createWrappableOptions(info, options);
		createWildcardOptions(info, options);
		createElementTypeInfosOptions(info, options);
		options.append(naming.type(),
				this.codeModel.string(naming.elementRefs()));
		return options;
	}
}