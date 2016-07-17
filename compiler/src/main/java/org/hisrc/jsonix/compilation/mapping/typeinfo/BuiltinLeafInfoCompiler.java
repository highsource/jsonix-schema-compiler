package org.hisrc.jsonix.compilation.mapping.typeinfo;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.namespace.QName;

import org.apache.commons.lang3.Validate;
import org.hisrc.jscm.codemodel.expression.JSAssignmentExpression;
import org.hisrc.jscm.codemodel.expression.JSObjectLiteral;
import org.hisrc.jsonix.compilation.jsonschema.typeinfo.TypeInfoProducer;
import org.hisrc.jsonix.compilation.mapping.MappingCompiler;

import com.sun.xml.xsom.XmlString;

public class BuiltinLeafInfoCompiler<T, C extends T, O> implements TypeInfoCompiler<T, C> {

	private final String name;

	protected final DatatypeFactory datatypeFactory;

	private QName qualifiedName;

	public BuiltinLeafInfoCompiler(String name, QName qualifiedName) {
		Validate.notNull(name);
		Validate.notNull(qualifiedName);
		this.qualifiedName = qualifiedName;
		this.name = name;
		try {
			this.datatypeFactory = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException dcex) {
			throw new ExceptionInInitializerError(dcex);
		}

	}

	@Override
	public JSAssignmentExpression createTypeInfoDeclaration(MappingCompiler<T, C> mappingCompiler) {
		return mappingCompiler.getCodeModel().string(name);
	}

	@Override
	public JSAssignmentExpression createValue(MappingCompiler<T, C> mappingCompiler, XmlString item) {
		return createValue(mappingCompiler, item.value);
	}

	@Override
	public JSAssignmentExpression createValue(MappingCompiler<T, C> mappingCompiler, String item) {
		return null;
	}

	@Override
	public JSObjectLiteral compile(MappingCompiler<T, C> mappingCompiler) {
		throw new UnsupportedOperationException();
	}
}
