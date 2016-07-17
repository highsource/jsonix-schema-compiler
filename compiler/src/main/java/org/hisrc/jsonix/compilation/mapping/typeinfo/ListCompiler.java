package org.hisrc.jsonix.compilation.mapping.typeinfo;

import org.apache.commons.lang3.Validate;
import org.hisrc.jscm.codemodel.JSCodeModel;
import org.hisrc.jscm.codemodel.expression.JSArrayLiteral;
import org.hisrc.jscm.codemodel.expression.JSAssignmentExpression;
import org.hisrc.jscm.codemodel.expression.JSObjectLiteral;
import org.hisrc.jsonix.compilation.mapping.IsLiteralEquals;
import org.hisrc.jsonix.compilation.mapping.MappingCompiler;
import org.hisrc.jsonix.naming.Naming;
import org.jvnet.jaxb2_commons.xml.bind.model.MList;

import com.sun.xml.xsom.XmlString;

public class ListCompiler<T, C extends T> implements TypeInfoCompiler<T, C> {

	private final MList<T, C> typeInfo;
	private final TypeInfoCompiler<T, C> itemTypeInfoCompiler;

	public ListCompiler(MList<T, C> typeInfo, TypeInfoCompiler<T, C> itemTypeInfoCompiler) {
		Validate.notNull(typeInfo);
		Validate.notNull(itemTypeInfoCompiler);
		this.typeInfo = typeInfo;
		this.itemTypeInfoCompiler = itemTypeInfoCompiler;
	}

	@Override
	public JSAssignmentExpression createTypeInfoDeclaration(MappingCompiler<T, C> mappingCompiler) {
		Validate.notNull(mappingCompiler);
		final JSCodeModel codeModel = mappingCompiler.getCodeModel();
		final Naming naming = mappingCompiler.getNaming();

		final JSObjectLiteral list = codeModel.object();
		list.append(naming.type(), codeModel.string(naming.list()));
		final JSAssignmentExpression typeInfoDeclaration = this.itemTypeInfoCompiler
				.createTypeInfoDeclaration(mappingCompiler);
		if (!typeInfoDeclaration.acceptExpressionVisitor(new IsLiteralEquals("String"))) {
			list.append(naming.baseTypeInfo(), typeInfoDeclaration);
		}
		return list;
	}

	@Override
	public JSAssignmentExpression createValue(MappingCompiler<T, C> mappingCompiler, XmlString item) {
		final JSCodeModel codeModel = mappingCompiler.getCodeModel();
		final String[] values = item.value.split(" ");
		final JSArrayLiteral result = codeModel.array();
		for (String value : values) {
			final JSAssignmentExpression v = itemTypeInfoCompiler.createValue(mappingCompiler,
					new XmlString(value, item.context));
			if (v == null) {
				return null;
			} else {
				result.append(v);
			}
		}
		return result;
	}

	@Override
	public JSAssignmentExpression createValue(MappingCompiler<T, C> mappingCompiler, String item) {
		final JSCodeModel codeModel = mappingCompiler.getCodeModel();
		final String[] values = item.split(" ");
		final JSArrayLiteral result = codeModel.array();
		for (String value : values) {
			final JSAssignmentExpression v = itemTypeInfoCompiler.createValue(mappingCompiler, value);
			if (v == null) {
				return null;
			} else {
				result.append(v);
			}
		}
		return result;
	}

	@Override
	public JSObjectLiteral compile(MappingCompiler<T, C> mappingCompiler) {
		throw new UnsupportedOperationException();
	}
}
