package org.hisrc.jsonix.compilation.jsonschema.typeinfo.builtin;

import javax.xml.namespace.QName;

import org.hisrc.jscm.codemodel.JSCodeModel;
import org.hisrc.jscm.codemodel.expression.JSArrayLiteral;
import org.hisrc.jscm.codemodel.expression.JSAssignmentExpression;
import org.hisrc.jsonix.compilation.jsonschema.typeinfo.BuiltinLeafInfoProducer;
import org.hisrc.jsonix.compilation.mapping.MappingCompiler;

public abstract class BinaryTypeInfoProducer<T, C extends T, O> extends BuiltinLeafInfoProducer<T, C, O> {

	public BinaryTypeInfoProducer(QName qualifiedName) {
		super(qualifiedName);
	}
/*
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
*/
	public abstract byte[] parse(String item);
}
