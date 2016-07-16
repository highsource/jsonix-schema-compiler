package org.hisrc.jsonix.compilation.typeinfo.builtin;

import org.hisrc.jscm.codemodel.JSCodeModel;
import org.hisrc.jscm.codemodel.expression.JSArrayLiteral;
import org.hisrc.jscm.codemodel.expression.JSAssignmentExpression;
import org.hisrc.jsonix.compilation.typeinfo.BuiltinLeafInfoCompiler;

public abstract class BinaryTypeInfoCompiler<T, C extends T, O> extends BuiltinLeafInfoCompiler<T, C, O> {

	public BinaryTypeInfoCompiler(String name) {
		super(name);
	}

	@Override
	public JSAssignmentExpression createValue(JSCodeModel codeModel, String item) {
		final byte[] value = parse(item);
		final JSArrayLiteral result = codeModel.array();
		for (final byte b : value) {
			final int v = b >= 0 ? b : b + 256;
			result.append(codeModel.integer(v));
		}
		return result;
	}

	public abstract byte[] parse(String item);
}
