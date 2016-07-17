package org.hisrc.jsonix.compilation.mapping.typeinfo.builtin;

import org.hisrc.jscm.codemodel.JSCodeModel;
import org.hisrc.jscm.codemodel.expression.JSAssignmentExpression;
import org.hisrc.jsonix.compilation.mapping.MappingCompiler;
import org.hisrc.jsonix.compilation.mapping.typeinfo.BuiltinLeafInfoCompiler;
import org.jvnet.jaxb2_commons.xmlschema.XmlSchemaConstants;

public class BooleanTypeInfoCompiler<T, C extends T, O> extends BuiltinLeafInfoCompiler<T, C, O> {

	public BooleanTypeInfoCompiler() {
		super("Boolean", XmlSchemaConstants.BOOLEAN);
	}

	@Override
	public JSAssignmentExpression createValue(MappingCompiler<T, C> mappingCompiler, String item) {
		final JSCodeModel codeModel = mappingCompiler.getCodeModel();
		return codeModel._boolean(Boolean.valueOf(item));
	}
}
