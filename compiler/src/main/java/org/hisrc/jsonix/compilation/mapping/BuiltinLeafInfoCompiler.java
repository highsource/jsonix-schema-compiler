package org.hisrc.jsonix.compilation.mapping;

import org.apache.commons.lang3.Validate;
import org.hisrc.jscm.codemodel.expression.JSAssignmentExpression;

public class BuiltinLeafInfoCompiler<T, C extends T, O> implements TypeInfoCompiler<T, C> {

	private final String name;
	
	public BuiltinLeafInfoCompiler(String name) {
		Validate.notNull(name);
		this.name = name;
	}

	@Override
	public JSAssignmentExpression createTypeInfoDeclaration(MappingCompiler<T, C> mappingCompiler) {
		return mappingCompiler.getCodeModel().string(name);
	}
}
