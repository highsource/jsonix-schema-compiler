package org.hisrc.jsonix.compilation.jsonschema.typeinfo.builtin;

import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.xml.datatype.Duration;

import org.hisrc.jsonix.compilation.jsonschema.JsonSchemaMappingCompiler;
import org.hisrc.jsonix.compilation.jsonschema.typeinfo.BuiltinLeafInfoProducer;
import org.jvnet.jaxb2_commons.xmlschema.XmlSchemaConstants;

public class DurationTypeInfoProducer<T, C extends T, O> extends BuiltinLeafInfoProducer<T, C, O> {

	public DurationTypeInfoProducer() {
		super(XmlSchemaConstants.DURATION);
	}

	@Override
	public JsonValue createValue(JsonSchemaMappingCompiler<T, C> mappingCompiler, String item) {
		final JsonObjectBuilder objectBuilder = mappingCompiler.getJsonBuilderFactory().createObjectBuilder();
		final Duration duration = datatypeFactory.newDuration(item);
		if (duration.getSign() <= 0) {
			objectBuilder.add("sign", duration.getSign());
		}
		if (duration.getYears() > 0) {
			objectBuilder.add("years", duration.getYears());
		}
		if (duration.getMonths() > 0) {
			objectBuilder.add("months", duration.getMonths());
		}
		if (duration.getDays() > 0) {
			objectBuilder.add("days", duration.getDays());
		}
		if (duration.getHours() > 0) {
			objectBuilder.add("hours", duration.getHours());
		}
		if (duration.getMinutes() > 0) {
			objectBuilder.add("minutes", duration.getMinutes());
		}
		if (duration.getSeconds() > 0) {
			objectBuilder.add("seconds", duration.getSeconds());
		}
		return objectBuilder.build();
	}
}
