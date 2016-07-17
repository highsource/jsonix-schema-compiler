package org.hisrc.jsonix.compilation.jsonschema.typeinfo.builtin;

import java.util.Iterator;

import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.xml.bind.DatatypeConverter;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;

import org.apache.commons.lang3.StringUtils;
import org.hisrc.jsonix.compilation.jsonschema.JsonSchemaMappingCompiler;
import org.hisrc.jsonix.compilation.jsonschema.typeinfo.BuiltinLeafInfoProducer;
import org.jvnet.jaxb2_commons.xmlschema.XmlSchemaConstants;
import org.relaxng.datatype.ValidationContext;

import com.sun.xml.xsom.XmlString;

public class QNameTypeInfoProducer<T, C extends T, O> extends BuiltinLeafInfoProducer<T, C, O> {

	public QNameTypeInfoProducer() {
		super(XmlSchemaConstants.QNAME);
	}

	@Override
	public JsonValue createValue(JsonSchemaMappingCompiler<T, C> mappingCompiler, XmlString item) {
		final JsonObjectBuilder objectBuilder = mappingCompiler.getJsonBuilderFactory().createObjectBuilder();
		final ValidationContext context = item.context;
		final QName value = DatatypeConverter.parseQName(item.value, new NamespaceContext() {

			@SuppressWarnings("rawtypes")
			@Override
			public Iterator getPrefixes(String namespaceURI) {
				throw new UnsupportedOperationException();
			}

			@Override
			public String getPrefix(String namespaceURI) {
				throw new UnsupportedOperationException();
			}

			@Override
			public String getNamespaceURI(String prefix) {
				return context.resolveNamespacePrefix(prefix);
			}
		});
		if (!StringUtils.isEmpty(value.getPrefix())) {
			objectBuilder.add("prefix", value.getPrefix());
		}
		if (!StringUtils.isEmpty(value.getNamespaceURI())) {
			objectBuilder.add("namespaceURI", value.getNamespaceURI());
		}
		objectBuilder.add("localPart", value.getLocalPart());
		return objectBuilder.build();
	}
}
