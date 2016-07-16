package org.hisrc.jsonix.compilation.typeinfo;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.apache.commons.lang3.Validate;
import org.hisrc.jscm.codemodel.JSCodeModel;
import org.hisrc.jscm.codemodel.expression.JSAssignmentExpression;
import org.hisrc.jsonix.compilation.mapping.MappingCompiler;

import com.sun.xml.xsom.XmlString;

public class BuiltinLeafInfoCompiler<T, C extends T, O> implements TypeInfoCompiler<T, C> {

	private final String name;

	protected final DatatypeFactory datatypeFactory;

	public BuiltinLeafInfoCompiler(String name) {
		Validate.notNull(name);
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
	public JSAssignmentExpression createValue(JSCodeModel codeModel, XmlString item) {
		return createValue(codeModel, item.value);
	}

	@Override
	public JSAssignmentExpression createValue(JSCodeModel codeModel, String item) {
		return codeModel.string(item);
	}
}
