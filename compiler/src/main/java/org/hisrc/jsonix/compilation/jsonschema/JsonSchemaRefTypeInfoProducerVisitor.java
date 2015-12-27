package org.hisrc.jsonix.compilation.jsonschema;

import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.commons.lang3.Validate;
import org.hisrc.jsonix.compilation.mapping.MappingCompiler;
import org.hisrc.jsonix.definition.Mapping;
import org.hisrc.jsonix.definition.Module;
import org.hisrc.jsonix.definition.Modules;
import org.hisrc.jsonix.jsonschema.JsonSchemaBuilder;
import org.hisrc.jsonix.jsonschema.JsonSchemaConstants;
import org.hisrc.jsonix.jsonschema.JsonSchemaKeywords;
import org.hisrc.jsonix.xml.xsom.CollectSimpleTypeNamesVisitor;
import org.hisrc.xml.xsom.SchemaComponentAware;
import org.jvnet.jaxb2_commons.xml.bind.model.MBuiltinLeafInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MClassInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MClassRef;
import org.jvnet.jaxb2_commons.xml.bind.model.MEnumLeafInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MID;
import org.jvnet.jaxb2_commons.xml.bind.model.MIDREF;
import org.jvnet.jaxb2_commons.xml.bind.model.MIDREFS;
import org.jvnet.jaxb2_commons.xml.bind.model.MList;
import org.jvnet.jaxb2_commons.xml.bind.model.MPackagedTypeInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MTypeInfoVisitor;
import org.jvnet.jaxb2_commons.xml.bind.model.MWildcardTypeInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.origin.MOriginated;

import com.sun.xml.xsom.XSComponent;

public class JsonSchemaRefTypeInfoProducerVisitor<T, C extends T, O> implements
		MTypeInfoVisitor<T, C, JsonSchemaBuilder> {

	private final JsonSchemaMappingCompiler<T, C> mappingCompiler;
	private final MOriginated<O> originated;
	private final Modules<T, C> modules;
	private final Module<T, C> module;
	private final Mapping<T, C> mapping;
	private final Map<QName, String> typeNameSchemaRefs;

	public JsonSchemaRefTypeInfoProducerVisitor(
			JsonSchemaMappingCompiler<T, C> mappingCompiler,
			MOriginated<O> originated) {
		Validate.notNull(mappingCompiler);
		Validate.notNull(originated);
		this.mappingCompiler = mappingCompiler;
		this.originated = originated;
		this.modules = mappingCompiler.getModules();
		this.module = mappingCompiler.getModule();
		this.mapping = mappingCompiler.getMapping();
		this.typeNameSchemaRefs = XmlSchemaJsonSchemaConstants.TYPE_NAME_SCHEMA_REFS;
	}

	public JsonSchemaMappingCompiler<T, C> getMappingCompiler() {
		return mappingCompiler;
	}

	public Modules<T, C> getModules() {
		return modules;
	}

	public Module<T, C> getModule() {
		return module;
	}

	public Mapping<T, C> getMapping() {
		return mapping;
	}

	@Override
	public JsonSchemaBuilder visitClassInfo(MClassInfo<T, C> info) {
		return createTypeInfoSchemaRef(info);
	}

	@Override
	public JsonSchemaBuilder visitClassRef(MClassRef<T, C> info) {
		return createTypeInfoSchemaRef(info);
	}

	@Override
	public JsonSchemaBuilder visitList(MList<T, C> info) {
		return new JsonSchemaBuilder().addType(JsonSchemaConstants.ARRAY_TYPE)
				.addItem(info.getItemTypeInfo().acceptTypeInfoVisitor(this));
	}

	@Override
	public JsonSchemaBuilder visitID(MID<T, C> info) {
		return new JsonSchemaBuilder()
				.addRef(XmlSchemaJsonSchemaConstants.ID_TYPE_INFO_SCHEMA_REF);
	}

	@Override
	public JsonSchemaBuilder visitIDREF(MIDREF<T, C> info) {
		return new JsonSchemaBuilder()
				.addRef(XmlSchemaJsonSchemaConstants.IDREF_TYPE_INFO_SCHEMA_REF);
	}

	@Override
	public JsonSchemaBuilder visitIDREFS(MIDREFS<T, C> info) {
		return new JsonSchemaBuilder()
				.addRef(XmlSchemaJsonSchemaConstants.IDREFS_TYPE_INFO_SCHEMA_REF);
	}

	@Override
	public JsonSchemaBuilder visitBuiltinLeafInfo(MBuiltinLeafInfo<T, C> info) {

		final O origin = this.originated.getOrigin();

		final List<QName> simpleTypeNames = new LinkedList<QName>();
		if (origin instanceof SchemaComponentAware) {
			final XSComponent component = ((SchemaComponentAware) origin)
					.getSchemaComponent();
			if (component != null) {
				final CollectSimpleTypeNamesVisitor visitor = new CollectSimpleTypeNamesVisitor();
				component.visit(visitor);
				simpleTypeNames.addAll(visitor.getTypeNames());
			}
		}

		simpleTypeNames.add(info.getTypeName());

		final JsonSchemaBuilder schema = new JsonSchemaBuilder();
		for (QName candidateName : simpleTypeNames) {
			final String $ref = this.typeNameSchemaRefs.get(candidateName);
			if ($ref != null) {
				return schema.addRef($ref);
			}
		}
		return schema
				.addDescription(MessageFormat
						.format("WARNING, the type [{0}] is not supported, using the lax schema {}.",
								info.getTypeName()));
	}

	@Override
	public JsonSchemaBuilder visitEnumLeafInfo(MEnumLeafInfo<T, C> info) {
		return createTypeInfoSchemaRef(info);
	}

	@Override
	public JsonSchemaBuilder visitWildcardTypeInfo(MWildcardTypeInfo<T, C> info) {
		return new JsonSchemaBuilder()
				.addRef(JsonixJsonSchemaConstants.WILDCARD_TYPE_INFO_SCHEMA_REF);
	}

	private JsonSchemaBuilder createTypeInfoSchemaRef(
			MPackagedTypeInfo<T, C> info) {

		final String typeInfoSchemaId = getModules().getSchemaId(
				info.getPackageInfo().getPackageName());

		final String schemaId = getMapping().getSchemaId();

		final String mappingSchemaId = typeInfoSchemaId.equals(schemaId) ? "#"
				: typeInfoSchemaId;
		final String typeInfoRef = mappingSchemaId
				+ "/"
				+ JsonSchemaKeywords.definitions
				+ "/"
				+ info.getContainerLocalName(MappingCompiler.DEFAULT_SCOPED_NAME_DELIMITER);
		return new JsonSchemaBuilder().addRef(typeInfoRef);
	}
}
