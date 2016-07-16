package org.hisrc.jsonix.compilation.typeinfo.builtin;

import org.hisrc.jscm.codemodel.JSCodeModel;
import org.hisrc.jscm.codemodel.expression.JSAssignmentExpression;
import org.hisrc.jsonix.compilation.typeinfo.BuiltinLeafInfoCompiler;

public class DecimalTypeInfoCompiler<T, C extends T, O> extends BuiltinLeafInfoCompiler<T, C, O> {

	public DecimalTypeInfoCompiler(String name) {
		super(name);
	}

	@Override
	public JSAssignmentExpression createValue(JSCodeModel codeModel, String item) {
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
