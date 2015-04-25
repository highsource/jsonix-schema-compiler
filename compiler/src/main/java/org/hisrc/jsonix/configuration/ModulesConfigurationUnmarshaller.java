/**
 * Jsonix is a JavaScript library which allows you to convert between XML
 * and JavaScript object structures.
 *
 * Copyright (c) 2010 - 2014, Alexey Valikov, Highsource.org
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice, this
 *   list of conditions and the following disclaimer in the documentation and/or
 *   other materials provided with the distribution.
 *
 * * Neither the name of the copyright holder nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.hisrc.jsonix.configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.apache.commons.lang3.Validate;
import org.hisrc.jsonix.context.JsonixContext;
import org.jvnet.jaxb2_commons.util.CustomizationUtils;
import org.slf4j.Logger;

import com.sun.tools.xjc.model.CPluginCustomization;
import com.sun.tools.xjc.model.Model;

public class ModulesConfigurationUnmarshaller {

	private final Logger logger;
	@SuppressWarnings("unused")
	private final JsonixContext context;
	private final JAXBContext jaxbContext;

	public ModulesConfigurationUnmarshaller(JsonixContext context) {
		this.context = Validate.notNull(context);
		this.logger = this.context.getLoggerFactory().getLogger(
				ModuleConfiguration.class.getName());
		try {
			jaxbContext = JAXBContext.newInstance(ModulesConfiguration.class,
					ModuleConfiguration.class, MappingConfiguration.class,
					OutputConfiguration.class, JsonSchemaConfiguration.class,
					PackageMapping.class, IncludesConfiguration.class,
					ExcludesConfiguration.class, TypeInfoConfiguration.class,
					ElementInfoConfiguration.class,
					PropertyInfoConfiguration.class,
					DependenciesOfMappingConfiguration.class);
		} catch (JAXBException jaxbex) {
			throw new ExceptionInInitializerError(jaxbex);
		}
	}

	public static final List<String> CUSTOMIZATION_URIS = Collections
			.singletonList(ModulesConfiguration.NAMESPACE_URI);

	private static final Set<String> CUSTOMIZATION_LOCAL_ELEMENT_NAMES = new HashSet<String>(
			Arrays.asList(PackageMapping.LOCAL_ELEMENT_NAME,
					ModulesConfiguration.LOCAL_ELEMENT_NAME,
					ModuleConfiguration.LOCAL_ELEMENT_NAME,
					MappingConfiguration.LOCAL_ELEMENT_NAME,
					OutputConfiguration.LOCAL_ELEMENT_NAME,
					JsonSchemaConfiguration.LOCAL_ELEMENT_NAME,
					IncludesConfiguration.LOCAL_ELEMENT_NAME,
					ExcludesConfiguration.LOCAL_ELEMENT_NAME,
					DependenciesOfMappingConfiguration.LOCAL_ELEMENT_NAME,
					TypeInfoConfiguration.LOCAL_ELEMENT_NAME,
					ElementInfoConfiguration.LOCAL_ELEMENT_NAME,
					PropertyInfoConfiguration.LOCAL_ELEMENT_NAME));

	public List<String> getCustomizationURIs() {
		return CUSTOMIZATION_URIS;
	}

	public boolean isCustomizationTagName(String nsUri, String localName) {
		return CUSTOMIZATION_URIS.contains(nsUri)
				&& CUSTOMIZATION_LOCAL_ELEMENT_NAMES.contains(localName);
	}

	private <T> T unmarshal(CPluginCustomization customization,
			String description) {
		try {
			@SuppressWarnings("unchecked")
			final T value = (T) jaxbContext.createUnmarshaller().unmarshal(
					customization.element);
			return value;
		} catch (JAXBException jaxbex) {
			throw (customization.locator == null ? new ConfigurationUnmarshallingException(
					description) : new ConfigurationUnmarshallingException(
					description, customization.locator));
		}
	}

	public ModulesConfiguration unmarshal(Model model,
			OutputConfiguration defaultOutputConfiguration,
			JsonSchemaConfiguration defaultJsonSchemaConfiguration) {
		Validate.notNull(model);
		Validate.notNull(defaultOutputConfiguration);
		final ModulesConfiguration modulesConfiguration = new ModulesConfiguration();
		for (CPluginCustomization customization : CustomizationUtils
				.findCustomizations(model, PackageMapping.PACKAGE_MAPPING_NAME)) {
			modulesConfiguration.getMappingConfigurations().add(
					unmarshalPackageMapping(customization));
		}
		for (CPluginCustomization customization : CustomizationUtils
				.findCustomizations(model, ModuleConfiguration.MODULE_NAME)) {
			modulesConfiguration.getModuleConfigurations().add(
					unmarshalModuleConfiguration(customization));
		}
		for (CPluginCustomization customization : CustomizationUtils
				.findCustomizations(model, MappingConfiguration.MAPPING_NAME)) {
			modulesConfiguration.getMappingConfigurations().add(
					unmarshalMappingConfiguration(customization));
		}
		for (CPluginCustomization customization : CustomizationUtils
				.findCustomizations(model, OutputConfiguration.OUTPUT_NAME)) {
			modulesConfiguration.getOutputConfigurations().add(
					unmarshalOutputConfiguration(customization));
		}
		for (CPluginCustomization customization : CustomizationUtils
				.findCustomizations(model,
						JsonSchemaConfiguration.JSON_SCHEMA_NAME)) {
			modulesConfiguration.getJsonSchemaConfigurations().add(
					unmarshalJsonSchemaConfiguration(customization));
		}

		if (modulesConfiguration.getOutputConfigurations().isEmpty()) {
			modulesConfiguration.getOutputConfigurations().add(
					defaultOutputConfiguration);
		}
		return modulesConfiguration;
	}

	private MappingConfiguration unmarshalPackageMapping(
			CPluginCustomization customization) {
		final PackageMapping packageMapping = unmarshal(customization,
				"package mapping");
		logger.warn("The [packageMapping] customization is deprecated, please use the [mapping] customization in the future.");

		final MappingConfiguration mappingConfiguration = new MappingConfiguration();
		mappingConfiguration.setName(packageMapping.getSpaceName());
		mappingConfiguration.setPackage(packageMapping.getPackageName());
		mappingConfiguration.setDefaultElementNamespaceURI(packageMapping
				.getDefaultElementNamespaceURI());
		mappingConfiguration.setDefaultAttributeNamespaceURI(packageMapping
				.getDefaultAttributeNamespaceURI());
		return mappingConfiguration;
	}

	private ModuleConfiguration unmarshalModuleConfiguration(
			CPluginCustomization customization) {
		return unmarshal(customization, "module configuration");
	}

	private MappingConfiguration unmarshalMappingConfiguration(
			CPluginCustomization customization) {
		return unmarshal(customization, "mapping configuration");
	}

	private OutputConfiguration unmarshalOutputConfiguration(
			CPluginCustomization customization) {
		return unmarshal(customization, "output configuration");
	}

	private JsonSchemaConfiguration unmarshalJsonSchemaConfiguration(
			CPluginCustomization customization) {
		return unmarshal(customization, "JSON Schema configuration");
	}
}
