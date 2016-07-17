package org.hisrc.jsonix.compilation.jsonschema.typeinfo.builtin;

import javax.xml.namespace.QName;

import org.hisrc.jscm.codemodel.JSCodeModel;
import org.hisrc.jscm.codemodel.expression.JSAssignmentExpression;
import org.hisrc.jsonix.compilation.jsonschema.typeinfo.BuiltinLeafInfoProducer;
import org.hisrc.jsonix.compilation.mapping.MappingCompiler;

public class DecimalTypeInfoProducer<T, C extends T, O> extends BuiltinLeafInfoProducer<T, C, O> {

	public DecimalTypeInfoProducer(QName qualifiedName) {
		super(qualifiedName);
	}
/*
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
	*/
}
