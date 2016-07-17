package org.hisrc.jsonix.compilation.mapping.typeinfo;

import org.hisrc.jscm.codemodel.expression.JSAssignmentExpression;
import org.hisrc.jscm.codemodel.expression.JSObjectLiteral;
import org.hisrc.jsonix.compilation.mapping.MappingCompiler;

import com.sun.xml.xsom.XmlString;

public interface TypeInfoCompiler<T, C extends T> {

	public JSAssignmentExpression createTypeInfoDeclaration(MappingCompiler<T, C> mappingCompiler);
	
	public JSAssignmentExpression createValue(MappingCompiler<T, C> mappingCompiler, XmlString item);

	public JSAssignmentExpression createValue(MappingCompiler<T, C> mappingCompiler, String item);

	public JSObjectLiteral compile(MappingCompiler<T, C> mappingCompiler);
}
