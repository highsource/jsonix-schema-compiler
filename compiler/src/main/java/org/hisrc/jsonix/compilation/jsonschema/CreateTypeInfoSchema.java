package org.hisrc.jsonix.compilation.jsonschema;

import java.util.List;

import javax.json.JsonValue;

import org.hisrc.jsonix.compilation.jsonschema.typeinfo.TypeInfoProducer;
import org.hisrc.jsonix.jsonschema.JsonSchemaBuilder;
import org.hisrc.jsonix.xml.xsom.CollectEnumerationValuesVisitor;
import org.hisrc.xml.xsom.SchemaComponentAware;
import org.jvnet.jaxb2_commons.xml.bind.model.MBuiltinLeafInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MTypeInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.origin.MOriginated;
import org.jvnet.jaxb2_commons.xml.bind.model.util.DefaultTypeInfoVisitor;

import com.sun.xml.xsom.XSComponent;
import com.sun.xml.xsom.XmlString;

public class CreateTypeInfoSchema<T, C extends T, M extends MOriginated<O>, O>
		extends DefaultTypeInfoVisitor<T, C, JsonSchemaBuilder> {
	private final M info;
	private JsonSchemaMappingCompiler<T, C> mappingCompiler;

	public CreateTypeInfoSchema(JsonSchemaMappingCompiler<T, C> mappingCompiler, M info) {
		this.mappingCompiler = mappingCompiler;
		this.info = info;
	}

	@Override
	public JsonSchemaBuilder visitTypeInfo(MTypeInfo<T, C> typeInfo) {
		final TypeInfoProducer<T, C> typeInfoCompiler = mappingCompiler.getTypeInfoProducer(info, typeInfo);
		return typeInfoCompiler.createTypeInfoSchemaRef(mappingCompiler);
	}

	@Override
	public JsonSchemaBuilder visitBuiltinLeafInfo(MBuiltinLeafInfo<T, C> typeInfo) {
		final TypeInfoProducer<T, C> typeInfoCompiler = mappingCompiler.getTypeInfoProducer(info, typeInfo);
		final JsonSchemaBuilder typeInfoSchemaRef = typeInfoCompiler.createTypeInfoSchemaRef(mappingCompiler);

		if (info instanceof MOriginated) {
			MOriginated<?> originated = (MOriginated<?>) info;
			Object origin = originated.getOrigin();
			if (origin instanceof SchemaComponentAware) {
				final XSComponent component = ((SchemaComponentAware) origin).getSchemaComponent();
				if (component != null) {

					final CollectEnumerationValuesVisitor collectEnumerationValuesVisitor = new CollectEnumerationValuesVisitor();
					component.visit(collectEnumerationValuesVisitor);
					final List<XmlString> enumerationValues = collectEnumerationValuesVisitor.getValues();
					if (enumerationValues != null && !enumerationValues.isEmpty()) {
						final JsonSchemaBuilder values = new JsonSchemaBuilder();
						boolean valueSupported = true;
						for (XmlString enumerationValue : enumerationValues) {
							final JsonValue value = typeInfoCompiler.createValue(this.mappingCompiler,
									enumerationValue);
							if (value == null) {
								valueSupported = false;
								break;
							} else {
								values.addEnum(value);
							}
						}
						if (valueSupported) {
							final JsonSchemaBuilder typeInfoSchemaRefWithEnums = new JsonSchemaBuilder();
							typeInfoSchemaRefWithEnums.addAllOf(typeInfoSchemaRef);
							typeInfoSchemaRefWithEnums.addAllOf(values);
							return typeInfoSchemaRefWithEnums;
						}
					}
				}
			}
		}
		return typeInfoSchemaRef;
	}
}
