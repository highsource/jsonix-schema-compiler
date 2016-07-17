package org.hisrc.jsonix.compilation.mapping.typeinfo.builtin;

import javax.xml.namespace.QName;

import org.hisrc.jscm.codemodel.JSCodeModel;
import org.hisrc.jscm.codemodel.expression.JSAssignmentExpression;
import org.hisrc.jsonix.compilation.mapping.MappingCompiler;
import org.hisrc.jsonix.compilation.mapping.typeinfo.BuiltinLeafInfoCompiler;

public class StringTypeInfoCompiler<T, C extends T, O> extends BuiltinLeafInfoCompiler<T, C, O> {

	public StringTypeInfoCompiler(String name,QName qualifiedName) {
		super(name, qualifiedName);
	}

	@Override
	public JSAssignmentExpression createValue(MappingCompiler<T, C> mappingCompiler, String item) {
		final JSCodeModel codeModel = mappingCompiler.getCodeModel();
		return codeModel.string(item);
	}
}
