package org.hisrc.jsonix.compilation.typeinfo.builtin;

import org.hisrc.jscm.codemodel.JSCodeModel;
import org.hisrc.jscm.codemodel.expression.JSAssignmentExpression;
import org.hisrc.jsonix.compilation.typeinfo.BuiltinLeafInfoCompiler;

public class StringTypeInfoCompiler<T, C extends T, O> extends BuiltinLeafInfoCompiler<T, C, O> {

	public StringTypeInfoCompiler(String name) {
		super(name);
	}

	@Override
	public JSAssignmentExpression createValue(JSCodeModel codeModel, String item) {
		return codeModel.string(item);
	}
}
