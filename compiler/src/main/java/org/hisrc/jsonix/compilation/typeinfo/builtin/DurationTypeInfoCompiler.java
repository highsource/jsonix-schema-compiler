package org.hisrc.jsonix.compilation.typeinfo.builtin;

import javax.xml.datatype.Duration;

import org.hisrc.jscm.codemodel.JSCodeModel;
import org.hisrc.jscm.codemodel.expression.JSAssignmentExpression;
import org.hisrc.jscm.codemodel.expression.JSObjectLiteral;
import org.hisrc.jsonix.compilation.typeinfo.BuiltinLeafInfoCompiler;

public class DurationTypeInfoCompiler<T, C extends T, O> extends BuiltinLeafInfoCompiler<T, C, O> {

	public DurationTypeInfoCompiler() {
		super("Duration");
	}

	@Override
	public JSAssignmentExpression createValue(JSCodeModel codeModel, String item) {
		final JSObjectLiteral result = codeModel.object();
		final Duration duration = datatypeFactory.newDuration(item);
		if (duration.getSign() <= 0) {
			result.append("sign", codeModel.integer(duration.getSign()));
		}
		if (duration.getYears() > 0) {
			result.append("years", codeModel.integer(duration.getYears()));
		}
		if (duration.getMonths() > 0) {
			result.append("months", codeModel.integer(duration.getMonths()));
		}
		if (duration.getDays() > 0) {
			result.append("days", codeModel.integer(duration.getDays()));
		}
		if (duration.getHours() > 0) {
			result.append("hours", codeModel.integer(duration.getHours()));
		}
		if (duration.getMinutes() > 0) {
			result.append("minutes", codeModel.integer(duration.getMinutes()));
		}
		if (duration.getSeconds() > 0) {
			result.append("seconds", codeModel.integer(duration.getSeconds()));
		}
		return result;
	}
}
