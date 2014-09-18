package org.hisrc.jsonix.compilation;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.hisrc.jsonix.log.Log;
import org.hisrc.jsonix.log.SystemLog;

public class Modules {

	private Log log = new SystemLog();
	private final Collection<Module> modules;
	private final Map<String, String> packageMappingNameMap = new HashMap<String, String>();

	public Modules(Collection<Module> modules) {
		Validate.noNullElements(modules);
		this.modules = modules;

		for (Module module : modules) {
			for (Mapping mapping : module.getMappings()) {

				final String packageName = mapping.getPackageName();
				final String mappingName = mapping.getMappingName();
				final String knownMappingName = packageMappingNameMap
						.get(packageName);
				if (knownMappingName == null) {
					this.packageMappingNameMap.put(packageName,
							mappingName);
				} else if (!knownMappingName.equals(mappingName)) {
					log.warn(MessageFormat
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

	public String getMappingName(String packageName) {
		return this.packageMappingNameMap.get(packageName);
	}

	public Collection<Module> getModules() {
		return modules;
	}

	@Override
	public String toString() {
		return this.modules.toString();
	}

}
