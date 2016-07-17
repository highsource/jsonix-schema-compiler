package org.hisrc.jsonix.compilation.mapping.typeinfo;

import org.apache.commons.lang3.Validate;
import org.hisrc.jscm.codemodel.expression.JSObjectLiteral;
import org.hisrc.jsonix.compilation.mapping.MappingCompiler;
import org.jvnet.jaxb2_commons.xml.bind.model.MClassRef;

public class ClassRefCompiler<T, C extends T> extends PackagedTypeInfoCompiler<T, C> {

	private MClassRef<T, C> classRef;

	public ClassRefCompiler(MClassRef<T, C> classRef) {
		super(Validate.notNull(classRef));
		this.classRef = classRef;
	}

	@Override
	public JSObjectLiteral compile(MappingCompiler<T, C> mappingCompiler) {
		throw new UnsupportedOperationException();
	}

}
