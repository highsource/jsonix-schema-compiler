package org.hisrc.jsonix.compilation.mapping.typeinfo;

import org.apache.commons.lang3.Validate;
import org.hisrc.jscm.codemodel.JSCodeModel;
import org.hisrc.jscm.codemodel.expression.JSArrayLiteral;
import org.hisrc.jscm.codemodel.expression.JSAssignmentExpression;
import org.hisrc.jscm.codemodel.expression.JSObjectLiteral;
import org.hisrc.jsonix.compilation.mapping.IsLiteralEquals;
import org.hisrc.jsonix.compilation.mapping.MappingCompiler;
import org.hisrc.jsonix.naming.Naming;
import org.jvnet.jaxb2_commons.xml.bind.model.MEnumConstantInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MEnumLeafInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MTypeInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.origin.MEnumLeafInfoOrigin;
import org.jvnet.jaxb2_commons.xml.bind.model.origin.MOriginated;

import com.sun.xml.xsom.XmlString;

public class EnumLeafInfoCompiler<T, C extends T> extends PackagedTypeInfoCompiler<T, C> {

	private final MEnumLeafInfo<T, C> enumLeafInfo;

	public EnumLeafInfoCompiler(MEnumLeafInfo<T, C> enumLeafInfo) {
		super(Validate.notNull(enumLeafInfo));
		this.enumLeafInfo = enumLeafInfo;
	}

	@Override
	public JSAssignmentExpression createValue(MappingCompiler<T, C> mappingCompiler, XmlString item) {
		final MTypeInfo<T, C> baseTypeInfo = enumLeafInfo.getBaseTypeInfo();
		final TypeInfoCompiler<T, C> baseTypeInfoCompiler = mappingCompiler.getTypeInfoCompiler(enumLeafInfo,
				baseTypeInfo);
		return baseTypeInfoCompiler.createValue(mappingCompiler, item);
	}

	@Override
	public JSAssignmentExpression createValue(MappingCompiler<T, C> mappingCompiler, String item) {
		final MTypeInfo<T, C> baseTypeInfo = enumLeafInfo.getBaseTypeInfo();
		final TypeInfoCompiler<T, C> baseTypeInfoCompiler = mappingCompiler.getTypeInfoCompiler(enumLeafInfo,
				baseTypeInfo);
		return baseTypeInfoCompiler.createValue(mappingCompiler, item);
	}

	@Override
	public JSObjectLiteral compile(MappingCompiler<T, C> mappingCompiler) {
		final JSCodeModel codeModel = mappingCompiler.getCodeModel();
		final JSObjectLiteral mapping = codeModel.object();
		final Naming naming = mappingCompiler.getNaming();
		mapping.append(naming.type(), codeModel.string(naming.enumInfo()));
		mapping.append(naming.localName(),
				codeModel.string(enumLeafInfo.getContainerLocalName(MappingCompiler.DEFAULT_SCOPED_NAME_DELIMITER)));

		final MTypeInfo<T, C> baseTypeInfo = enumLeafInfo.getBaseTypeInfo();
		final TypeInfoCompiler<T, C> baseTypeInfoCompiler = mappingCompiler.getTypeInfoCompiler(enumLeafInfo,
				baseTypeInfo);
		final JSAssignmentExpression baseTypeInfoDeclaration = baseTypeInfoCompiler
				.createTypeInfoDeclaration(mappingCompiler);
		if (!baseTypeInfoDeclaration.acceptExpressionVisitor(new IsLiteralEquals("String"))) {
			mapping.append(naming.baseTypeInfo(), baseTypeInfoDeclaration);
		}
		final JSArrayLiteral values = codeModel.array();
		boolean valuesSupported = true;
		for (MEnumConstantInfo<T, C> enumConstantInfo : enumLeafInfo.getConstants()) {
			final JSAssignmentExpression value = baseTypeInfoCompiler.createValue(mappingCompiler,
					enumConstantInfo.getLexicalValue());
			if (value == null) {
				valuesSupported = false;
				break;
			} else {
				values.append(value);
			}
		}
		if (valuesSupported) {
			mapping.append(naming.values(), values);
		}
		return mapping;
	}

}