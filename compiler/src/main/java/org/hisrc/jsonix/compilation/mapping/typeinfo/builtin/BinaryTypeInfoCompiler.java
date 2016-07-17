package org.hisrc.jsonix.compilation.mapping.typeinfo.builtin;

import javax.xml.namespace.QName;

import org.hisrc.jscm.codemodel.JSCodeModel;
import org.hisrc.jscm.codemodel.expression.JSArrayLiteral;
import org.hisrc.jscm.codemodel.expression.JSAssignmentExpression;
import org.hisrc.jsonix.compilation.mapping.MappingCompiler;
import org.hisrc.jsonix.compilation.mapping.typeinfo.BuiltinLeafInfoCompiler;

public abstract class BinaryTypeInfoCompiler<T, C extends T, O> extends BuiltinLeafInfoCompiler<T, C, O> {

	public BinaryTypeInfoCompiler(String name, QName qualifiedName) {
		super(name, qualifiedName);
	}

	@Override
	public JSAssignmentExpression createValue(MappingCompiler<T, C> mappingCompiler, String item) {
		final JSCodeModel codeModel = mappingCompiler.getCodeModel();
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
