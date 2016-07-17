package org.hisrc.jsonix.compilation.mapping.typeinfo.builtin;

import javax.xml.namespace.QName;

import org.hisrc.jscm.codemodel.JSCodeModel;
import org.hisrc.jscm.codemodel.expression.JSAssignmentExpression;
import org.hisrc.jsonix.compilation.mapping.MappingCompiler;
import org.hisrc.jsonix.compilation.mapping.typeinfo.BuiltinLeafInfoCompiler;

public class DecimalTypeInfoCompiler<T, C extends T, O> extends BuiltinLeafInfoCompiler<T, C, O> {

	public DecimalTypeInfoCompiler(String name, QName qualifiedName) {
		super(name, qualifiedName);
	}

	@Override
	public JSAssignmentExpression createValue(MappingCompiler<T, C> mappingCompiler, String item) {
		final JSCodeModel codeModel = mappingCompiler.getCodeModel();
		// Hack to make -INF an INF work
		if ("-INF".equals(item)) {
			return codeModel.globalVariable("Infinity").negative();
		} else if ("INF".equals(item)) {
			return codeModel.globalVariable("Infinity");
		} else {
			return codeModel.decimal(item);
		}
	}
}
