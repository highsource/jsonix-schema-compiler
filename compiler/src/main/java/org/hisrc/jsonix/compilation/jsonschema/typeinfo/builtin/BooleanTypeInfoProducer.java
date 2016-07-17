package org.hisrc.jsonix.compilation.jsonschema.typeinfo.builtin;

import org.hisrc.jscm.codemodel.JSCodeModel;
import org.hisrc.jscm.codemodel.expression.JSAssignmentExpression;
import org.hisrc.jsonix.compilation.jsonschema.typeinfo.BuiltinLeafInfoProducer;
import org.hisrc.jsonix.compilation.mapping.MappingCompiler;
import org.jvnet.jaxb2_commons.xmlschema.XmlSchemaConstants;

public class BooleanTypeInfoProducer<T, C extends T, O> extends BuiltinLeafInfoProducer<T, C, O> {

	public BooleanTypeInfoProducer() {
		super(XmlSchemaConstants.BOOLEAN);
	}
/*
	@Override
	public JSAssignmentExpression createValue(MappingCompiler<T, C> mappingCompiler, String item) {
		final JSCodeModel codeModel = mappingCompiler.getCodeModel();
		return codeModel._boolean(Boolean.valueOf(item));
	}
	*/
}
