package org.hisrc.jsonix.compilation.mapping;

import java.util.List;

import javax.xml.namespace.NamespaceContext;

import org.hisrc.jscm.codemodel.expression.JSArrayLiteral;
import org.hisrc.jscm.codemodel.expression.JSAssignmentExpression;
import org.hisrc.jscm.codemodel.expression.JSObjectLiteral;
import org.hisrc.jsonix.compilation.mapping.typeinfo.TypeInfoCompiler;
import org.hisrc.jsonix.xml.xsom.CollectEnumerationValuesVisitor;
import org.hisrc.xml.xsom.SchemaComponentAware;
import org.jvnet.jaxb2_commons.xml.bind.model.MBuiltinLeafInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MDefaultValue;
import org.jvnet.jaxb2_commons.xml.bind.model.MTypeInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.origin.MOriginated;
import org.jvnet.jaxb2_commons.xml.bind.model.util.DefaultTypeInfoVisitor;
import org.relaxng.datatype.ValidationContext;

import com.sun.xml.xsom.XSComponent;
import com.sun.xml.xsom.XmlString;

public final class CreateTypeInfoDelaration<T, C extends T, M extends MOriginated<O>, O>
		extends DefaultTypeInfoVisitor<T, C, TypeInfoCompiler<T, C>> {
	private final M info;
	private final JSObjectLiteral options;
	private MappingCompiler<T, C> mappingCompiler;

	public CreateTypeInfoDelaration(MappingCompiler<T, C> mappingCompiler, M info, JSObjectLiteral options) {
		this.mappingCompiler = mappingCompiler;
		this.info = info;
		this.options = options;
	}

	@Override
	public TypeInfoCompiler<T, C> visitTypeInfo(MTypeInfo<T, C> typeInfo) {
		final TypeInfoCompiler<T, C> typeInfoCompiler = mappingCompiler.getTypeInfoCompiler(info, typeInfo);
		final JSAssignmentExpression typeInfoDeclaration = typeInfoCompiler.createTypeInfoDeclaration(mappingCompiler);
		if (!typeInfoDeclaration.acceptExpressionVisitor(new IsLiteralEquals("String"))) {
			options.append(mappingCompiler.getNaming().typeInfo(), typeInfoDeclaration);
		}
		return typeInfoCompiler;
	}

	@Override
	public TypeInfoCompiler<T, C> visitBuiltinLeafInfo(MBuiltinLeafInfo<T, C> typeInfo) {
		final TypeInfoCompiler<T, C> typeInfoCompiler = visitTypeInfo(typeInfo);

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
						final JSArrayLiteral values = mappingCompiler.getCodeModel().array();
						boolean valueSupported = true;
						for (XmlString enumerationValue : enumerationValues) {
							final JSAssignmentExpression value = typeInfoCompiler.createValue(this.mappingCompiler,
									enumerationValue);
							if (value == null) {
								valueSupported = false;
								break;
							} else {
								values.append(value);
							}
						}
						if (valueSupported) {
							options.append(mappingCompiler.getNaming().values(), values);
						}
					}
				}
			}
		}
		if (info instanceof MDefaultValue) {
			MDefaultValue defaultValue = (MDefaultValue) info;

			String defaultValueString = defaultValue.getDefaultValue();
			final NamespaceContext defaultValueNamespaceContext = defaultValue.getDefaultValueNamespaceContext();
			if (defaultValueString != null) {
				final XmlString defaultValueXmlString;
				if (defaultValueNamespaceContext == null) {
					defaultValueXmlString = new XmlString(defaultValueString);
				} else {
					defaultValueXmlString = new XmlString(defaultValueString, new ValidationContext() {

						@Override
						public String resolveNamespacePrefix(String prefix) {
							return defaultValueNamespaceContext.getNamespaceURI(prefix);
						}

						@Override
						public boolean isUnparsedEntity(String entityName) {
							throw new UnsupportedOperationException();
						}

						@Override
						public boolean isNotation(String notationName) {
							throw new UnsupportedOperationException();
						}

						@Override
						public String getBaseUri() {
							throw new UnsupportedOperationException();
						}
					});
				}
				JSAssignmentExpression defaultValueExpression = typeInfoCompiler.createValue(mappingCompiler,
						defaultValueXmlString);
				if (defaultValueExpression != null) {
					options.append(mappingCompiler.getNaming().defaultValue(), defaultValueExpression);
				}
			}
		}
		return typeInfoCompiler;
	}
}