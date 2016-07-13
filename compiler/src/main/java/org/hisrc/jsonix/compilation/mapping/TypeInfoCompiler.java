package org.hisrc.jsonix.compilation.mapping;

import org.hisrc.jscm.codemodel.expression.JSAssignmentExpression;

public interface TypeInfoCompiler<T, C extends T> {

	public JSAssignmentExpression createTypeInfoDeclaration(MappingCompiler<T, C> mappingCompiler);
}
