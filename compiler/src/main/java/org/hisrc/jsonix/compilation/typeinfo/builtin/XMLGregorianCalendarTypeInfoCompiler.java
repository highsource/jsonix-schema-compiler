package org.hisrc.jsonix.compilation.typeinfo.builtin;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;

import org.hisrc.jscm.codemodel.JSCodeModel;
import org.hisrc.jscm.codemodel.expression.JSAssignmentExpression;
import org.hisrc.jscm.codemodel.expression.JSObjectLiteral;
import org.hisrc.jsonix.compilation.typeinfo.BuiltinLeafInfoCompiler;

public class XMLGregorianCalendarTypeInfoCompiler<T, C extends T, O> extends BuiltinLeafInfoCompiler<T, C, O> {

	public XMLGregorianCalendarTypeInfoCompiler(String name) {
		super(name);
	}

	@Override
	public JSAssignmentExpression createValue(JSCodeModel codeModel, String item) {
		final JSObjectLiteral result = codeModel.object();
		final XMLGregorianCalendar calendar = datatypeFactory.newXMLGregorianCalendar(item);
		if (calendar.getYear() != DatatypeConstants.FIELD_UNDEFINED) {
			result.append("year", codeModel.integer(calendar.getYear()));
		}
		if (calendar.getMonth() != DatatypeConstants.FIELD_UNDEFINED) {
			result.append("month", codeModel.integer(calendar.getMonth()));
		}
		if (calendar.getDay() != DatatypeConstants.FIELD_UNDEFINED) {
			result.append("day", codeModel.integer(calendar.getDay()));
		}
		if (calendar.getHour() != DatatypeConstants.FIELD_UNDEFINED) {
			result.append("hour", codeModel.integer(calendar.getHour()));
		}
		if (calendar.getMinute() != DatatypeConstants.FIELD_UNDEFINED) {
			result.append("minute", codeModel.integer(calendar.getMinute()));
		}
		if (calendar.getSecond() != DatatypeConstants.FIELD_UNDEFINED) {
			result.append("second", codeModel.integer(calendar.getSecond()));
		}
		if (calendar.getFractionalSecond() != null) {
			result.append("second", codeModel.decimal(calendar.getFractionalSecond().toString()));
		}
		if (calendar.getTimezone() != DatatypeConstants.FIELD_UNDEFINED) {
			result.append("timeZone", codeModel.integer(calendar.getTimezone()));
		}
		return result;
	}
}
