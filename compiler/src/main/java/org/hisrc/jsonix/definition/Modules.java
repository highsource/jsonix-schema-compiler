package org.hisrc.jsonix.definition;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.hisrc.jsonix.configuration.AmbiguousPackageMappingNameException;
import org.hisrc.jsonix.configuration.AmbiguousPackageSchemaIdException;
import org.hisrc.jsonix.context.JsonixContext;
import org.jvnet.jaxb2_commons.xml.bind.model.MModelInfo;
import org.slf4j.Logger;

public class Modules<T, C extends T> {

	private final Logger logger;
	private final Collection<Module<T, C>> modules;
	private final Map<String, String> packageMappingNameMap = new HashMap<String, String>();
	private final Map<String, String> packageSchemaIdMap = new HashMap<String, String>();
	private final MModelInfo<T, C> modelInfo;

	public Modules(JsonixContext context, MModelInfo<T, C> modelInfo,
			Collection<Module<T, C>> modules) {
		Validate.notNull(modelInfo);
		Validate.noNullElements(modules);
		this.logger = Validate.notNull(context).getLoggerFactory()
				.getLogger(Modules.class.getName());
		this.modelInfo = modelInfo;
		this.modules = modules;

		for (Module<T, C> module : modules) {
			for (Mapping<T, C> mapping : module.getMappings()) {

				final String packageName = mapping.getPackageName();
				{
					final String mappingName = mapping.getMappingName();
					final String knownMappingName = packageMappingNameMap
							.get(packageName);
					if (knownMappingName == null) {
						this.packageMappingNameMap
								.put(packageName, mappingName);
					} else if (!knownMappingName.equals(mappingName)) {
						logger.warn(MessageFormat
								.format("Package [{0}] is mapped using at least two different mapping names [{1}] and [{2}]. Packages may be mapped by several mappings but they have to have equal names.",
										packageName, knownMappingName,
										mappingName));
						throw new AmbiguousPackageMappingNameException(
								packageName, knownMappingName, mappingName);
					}
				}
				{
					final String schemaId = mapping.getSchemaId();
					final String knownSchemaId = packageSchemaIdMap
							.get(packageName);
					if (knownSchemaId == null) {
						this.packageSchemaIdMap.put(packageName, schemaId);
					} else if (!knownSchemaId.equals(schemaId)) {
						logger.warn(MessageFormat
								.format("Package [{0}] is mapped using at least two different schema ids [{1}] and [{2}]. Packages may be mapped by several mappings but they have to have equal schema ids.",
										packageName, knownSchemaId, schemaId));
						throw new AmbiguousPackageSchemaIdException(
								packageName, knownSchemaId, schemaId);
					}
				}
			}
		}
	}

	public MModelInfo<T, C> getModelInfo() {
		return modelInfo;
	}

	public String getMappingName(String packageName) {
		return this.packageMappingNameMap.get(packageName);
	}

	public String getSchemaId(String packageName) {
		return this.packageSchemaIdMap.get(packageName);
	}

	public Collection<Module<T, C>> getModules() {
		return modules;
	}

	@Override
	public String toString() {
		return this.modules.toString();
	}

}
