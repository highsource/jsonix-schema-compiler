package org.hisrc.jsonix.compilation.mapping.typeinfo;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

import org.apache.commons.lang3.Validate;
import org.hisrc.jscm.codemodel.JSCodeModel;
import org.hisrc.jscm.codemodel.expression.JSArrayLiteral;
import org.hisrc.jscm.codemodel.expression.JSAssignmentExpression;
import org.hisrc.jscm.codemodel.expression.JSObjectLiteral;
import org.hisrc.jsonix.compilation.mapping.MappingCompiler;
import org.hisrc.jsonix.compilation.mapping.PropertyInfoVisitor;
import org.hisrc.jsonix.definition.Mapping;
import org.hisrc.jsonix.naming.Naming;
import org.jvnet.jaxb2_commons.xml.bind.model.MClassInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MClassTypeInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MPropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.origin.MClassInfoOrigin;
import org.jvnet.jaxb2_commons.xml.bind.model.origin.MOriginated;

public class ClassInfoCompiler<T, C extends T> extends PackagedTypeInfoCompiler<T, C> {

	private MClassInfo<T, C> classInfo;

	public ClassInfoCompiler(MClassInfo<T, C> classInfo) {
		super(Validate.notNull(classInfo));
		this.classInfo = classInfo;
	}

	@Override
	public JSObjectLiteral compile(MappingCompiler<T, C> mappingCompiler) {
		final JSCodeModel codeModel = mappingCompiler.getCodeModel();
		final Mapping<T, C> mapping = mappingCompiler.getMapping();
		final Naming naming = mappingCompiler.getNaming();

		final JSObjectLiteral classInfoMapping = codeModel.object();
		final String localName = classInfo.getContainerLocalName(MappingCompiler.DEFAULT_SCOPED_NAME_DELIMITER);
		classInfoMapping.append(naming.localName(), codeModel.string(localName));
		final String targetNamespace = mapping.getTargetNamespaceURI();
		final QName defaultTypeName = new QName(targetNamespace, localName);
		final QName typeName = classInfo.getTypeName();

		if (!defaultTypeName.equals(typeName)) {
			final JSAssignmentExpression typeNameExpression;
			if (typeName == null) {
				typeNameExpression = codeModel._null();
			} else if (defaultTypeName.getNamespaceURI().equals(typeName.getNamespaceURI())) {
				typeNameExpression = codeModel.string(typeName.getLocalPart());
			} else {
				final JSObjectLiteral typeNameObject = codeModel.object();
				typeNameObject.append(naming.namespaceURI(), codeModel.string(typeName.getNamespaceURI()));
				typeNameObject.append(naming.localPart(), codeModel.string(typeName.getLocalPart()));
				if (!XMLConstants.DEFAULT_NS_PREFIX.equals(typeName.getPrefix())) {
					typeNameObject.append(naming.prefix(), codeModel.string(typeName.getPrefix()));
				}
				typeNameExpression = typeNameObject;
			}
			classInfoMapping.append(naming.typeName(), typeNameExpression);

		}

		final MClassTypeInfo<T, C, ?> baseTypeInfo = classInfo.getBaseTypeInfo();
		if (baseTypeInfo != null) {
			classInfoMapping.append(naming.baseTypeInfo(), mappingCompiler.getTypeInfoCompiler(classInfo, baseTypeInfo)
					.createTypeInfoDeclaration(mappingCompiler));
		}
		final JSArrayLiteral ps = compilePropertyInfos(mappingCompiler);
		if (!ps.getElements().isEmpty()) {
			classInfoMapping.append(naming.propertyInfos(), ps);
		}
		return classInfoMapping;
	}

	private JSArrayLiteral compilePropertyInfos(MappingCompiler<T, C> mappingCompiler) {
		final JSCodeModel codeModel = mappingCompiler.getCodeModel();
		final Mapping<T, C> mapping = mappingCompiler.getMapping();
		final JSArrayLiteral propertyInfoMappings = codeModel.array();
		for (MPropertyInfo<T, C> propertyInfo : classInfo.getProperties()) {
			if (mapping.getPropertyInfos().contains(propertyInfo)) {
				propertyInfoMappings
						.append(propertyInfo.acceptPropertyInfoVisitor(new PropertyInfoVisitor<T, C>(mappingCompiler)));
			}
		}
		return propertyInfoMappings;
	}
}
