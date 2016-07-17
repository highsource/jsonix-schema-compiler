package org.hisrc.jsonix.compilation.jsonschema.typeinfo.builtin;

import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.hisrc.jsonix.compilation.jsonschema.JsonSchemaMappingCompiler;
import org.hisrc.jsonix.compilation.jsonschema.typeinfo.BuiltinLeafInfoProducer;

public class XMLGregorianCalendarTypeInfoProducer<T, C extends T, O> extends BuiltinLeafInfoProducer<T, C, O> {

	public XMLGregorianCalendarTypeInfoProducer(QName qualifiedName) {
		super(qualifiedName);
	}

	@Override
	public JsonValue createValue(JsonSchemaMappingCompiler<T, C> mappingCompiler, String item) {
		final JsonObjectBuilder objectBuilder = mappingCompiler.getJsonBuilderFactory().createObjectBuilder();
		final XMLGregorianCalendar calendar = datatypeFactory.newXMLGregorianCalendar(item);
		if (calendar.getYear() != DatatypeConstants.FIELD_UNDEFINED) {
			objectBuilder.add("year", calendar.getYear());
		}
		if (calendar.getMonth() != DatatypeConstants.FIELD_UNDEFINED) {
			objectBuilder.add("month", calendar.getMonth());
		}
		if (calendar.getDay() != DatatypeConstants.FIELD_UNDEFINED) {
			objectBuilder.add("day", calendar.getDay());
		}
		if (calendar.getHour() != DatatypeConstants.FIELD_UNDEFINED) {
			objectBuilder.add("hour", calendar.getHour());
		}
		if (calendar.getMinute() != DatatypeConstants.FIELD_UNDEFINED) {
			objectBuilder.add("minute", calendar.getMinute());
		}
		if (calendar.getSecond() != DatatypeConstants.FIELD_UNDEFINED) {
			objectBuilder.add("second", calendar.getSecond());
		}
		if (calendar.getFractionalSecond() != null) {
			objectBuilder.add("second", calendar.getFractionalSecond());
		}
		if (calendar.getTimezone() != DatatypeConstants.FIELD_UNDEFINED) {
			objectBuilder.add("timeZone", calendar.getTimezone());
		}
		return objectBuilder.build();
	}
}
