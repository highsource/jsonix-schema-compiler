package org.hisrc.jsonix.compilation.mapping;

import org.apache.commons.lang3.Validate;
import org.hisrc.jscm.codemodel.expression.JSExpression;
import org.hisrc.jscm.codemodel.expression.impl.DefaultExpressionVisitor;
import org.hisrc.jscm.codemodel.literal.JSBooleanLiteral;
import org.hisrc.jscm.codemodel.literal.JSDecimalIntegerLiteral;
import org.hisrc.jscm.codemodel.literal.JSDecimalNonIntegerLiteral;
import org.hisrc.jscm.codemodel.literal.JSLiteral;
import org.hisrc.jscm.codemodel.literal.JSLiteralVisitor;
import org.hisrc.jscm.codemodel.literal.JSNullLiteral;
import org.hisrc.jscm.codemodel.literal.JSStringLiteral;

public class IsLiteralEquals extends DefaultExpressionVisitor<Boolean, RuntimeException>
		implements JSLiteralVisitor<Boolean, RuntimeException> {

	private final String value;

	public IsLiteralEquals(String value) {
		Validate.notNull(value);
		this.value = value;
	}

	@Override
	public Boolean visitLiteral(JSLiteral value) throws RuntimeException {
		return value.acceptLiteralVisitor(this);
	}

	@Override
	public Boolean visitExpression(JSExpression value) throws RuntimeException {
		return Boolean.FALSE;
	}

	@Override
	public Boolean visit(JSStringLiteral value) {
		return IsLiteralEquals.this.value.equals(value.asString());
	}

	@Override
	public Boolean visit(JSNullLiteral value) {
		return Boolean.FALSE;
	}

	@Override
	public Boolean visit(JSBooleanLiteral value) {
		return Boolean.FALSE;
	}

	@Override
	public Boolean visit(JSDecimalIntegerLiteral value) {
		return Boolean.FALSE;
	}

	@Override
	public Boolean visit(JSDecimalNonIntegerLiteral value) {
		return Boolean.FALSE;
	}
}
