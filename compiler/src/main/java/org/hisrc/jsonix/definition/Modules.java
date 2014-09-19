package org.hisrc.jsonix.definition;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.hisrc.jsonix.log.Log;
import org.jvnet.jaxb2_commons.xml.bind.model.MModelInfo;

public class Modules<T, C extends T> {

	private final Log log;
	private final Collection<Module<T, C>> modules;
	private final Map<String, String> packageMappingNameMap = new HashMap<String, String>();
	private final MModelInfo<T, C> modelInfo;

	public Modules(Log log, MModelInfo<T, C> modelInfo, Collection<Module<T, C>> modules) {
		Validate.notNull(log);
		Validate.notNull(modelInfo);
		Validate.noNullElements(modules);
		this.log = log;
		this.modelInfo = modelInfo;
		this.modules = modules;

		for (Module<T, C> module : modules) {
			for (Mapping<T, C> mapping : module.getMappings()) {

				final String packageName = mapping.getPackageName();
				final String mappingName = mapping.getMappingName();
				final String knownMappingName = packageMappingNameMap
						.get(packageName);
				if (knownMappingName == null) {
					this.packageMappingNameMap.put(packageName, mappingName);
				} else if (!knownMappingName.equals(mappingName)) {
					this.log.warn(MessageFormat
							.format("Package [{0}] is mapped using at least two different mapping names [{1}] and [{2}]. Packages may be mapped by several mappings but they have to have equal names.",
									packageName, knownMappingName, mappingName));
					throw new IllegalArgumentException(
							MessageFormat
									.format("Package [{0}] is mapped using at least two different mapping names [{1}] and [{2}].",
											packageName, knownMappingName,
											mappingName));
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

	public Collection<Module<T, C>> getModules() {
		return modules;
	}

	@Override
	public String toString() {
		return this.modules.toString();
	}

}
