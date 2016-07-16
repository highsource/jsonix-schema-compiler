package org.hisrc.jsonix.compilation.typeinfo;

import org.hisrc.jscm.codemodel.JSCodeModel;
import org.hisrc.jscm.codemodel.expression.JSAssignmentExpression;
import org.hisrc.jsonix.compilation.mapping.MappingCompiler;

import com.sun.xml.xsom.XmlString;

public interface TypeInfoCompiler<T, C extends T> {

	public JSAssignmentExpression createTypeInfoDeclaration(MappingCompiler<T, C> mappingCompiler);
	
	public JSAssignmentExpression createValue(JSCodeModel codeModel, XmlString item);

	public JSAssignmentExpression createValue(JSCodeModel codeModel, String item);
}
