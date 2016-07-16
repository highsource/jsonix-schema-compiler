package org.hisrc.jsonix.compilation.typeinfo;

import org.apache.commons.lang3.Validate;
import org.hisrc.jscm.codemodel.JSCodeModel;
import org.hisrc.jscm.codemodel.expression.JSAssignmentExpression;
import org.jvnet.jaxb2_commons.xml.bind.model.MEnumLeafInfo;

import com.sun.xml.xsom.XmlString;

public class EnumLeafInfoCompiler<T, C extends T> extends PackagedTypeInfoCompiler<T, C> {

	private final MEnumLeafInfo<T, C> typeInfo;
	private final TypeInfoCompiler<T, C> baseTypeInfoCompiler;

	public EnumLeafInfoCompiler(MEnumLeafInfo<T, C> typeInfo, TypeInfoCompiler<T, C> baseTypeInfoCompiler) {
		super(Validate.notNull(typeInfo));
		Validate.notNull(baseTypeInfoCompiler);
		this.typeInfo = typeInfo;
		this.baseTypeInfoCompiler = baseTypeInfoCompiler;
	}
	
	@Override
	public JSAssignmentExpression createValue(JSCodeModel codeModel, XmlString item) {
		return baseTypeInfoCompiler.createValue(codeModel, item);
	}
	@Override
	public JSAssignmentExpression createValue(JSCodeModel codeModel, String item) {
		return baseTypeInfoCompiler.createValue(codeModel, item);
	}
}
