package org.hisrc.jsonix.compilation.mapping.typeinfo.builtin;

import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.namespace.QName;

import org.hisrc.jscm.codemodel.expression.JSAssignmentExpression;
import org.hisrc.jsonix.compilation.mapping.MappingCompiler;

public class NormalizedStringTypeInfoCompiler<T, C extends T, O> extends StringTypeInfoCompiler<T, C, O> {

	private final NormalizedStringAdapter normalizedStringAdapter = new NormalizedStringAdapter();

	public NormalizedStringTypeInfoCompiler(String name, QName qualifiedName) {
		super(name, qualifiedName);
	}

	@Override
	public JSAssignmentExpression createValue(MappingCompiler<T, C> mappingCompiler, String item) {
		return mappingCompiler.getCodeModel().string(normalizedStringAdapter.unmarshal(item));
	}
}
