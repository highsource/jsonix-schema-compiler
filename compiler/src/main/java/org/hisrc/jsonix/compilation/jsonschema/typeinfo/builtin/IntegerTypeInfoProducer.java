package org.hisrc.jsonix.compilation.jsonschema.typeinfo.builtin;

import javax.xml.namespace.QName;

import org.hisrc.jscm.codemodel.JSCodeModel;
import org.hisrc.jscm.codemodel.expression.JSAssignmentExpression;
import org.hisrc.jsonix.compilation.jsonschema.typeinfo.BuiltinLeafInfoProducer;
import org.hisrc.jsonix.compilation.mapping.MappingCompiler;

public class IntegerTypeInfoProducer<T, C extends T, O> extends BuiltinLeafInfoProducer<T, C, O> {

	public IntegerTypeInfoProducer(QName qualifiedName) {
		super(qualifiedName);
	}
/*
	@Override
	public JSAssignmentExpression createValue(MappingCompiler<T, C> mappingCompiler, String item) {
		final JSCodeModel codeModel = mappingCompiler.getCodeModel();
		return codeModel.decimal(item);
	}
	
	*/
}
