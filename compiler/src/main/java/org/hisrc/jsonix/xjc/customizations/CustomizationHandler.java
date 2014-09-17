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

package org.hisrc.jsonix.xjc.customizations;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.apache.commons.lang3.Validate;
import org.hisrc.jsonix.compiler.log.Log;
import org.hisrc.jsonix.configuration.MappingConfiguration;
import org.hisrc.jsonix.configuration.ModuleConfiguration;
import org.hisrc.jsonix.configuration.ModulesConfiguration;
import org.hisrc.jsonix.configuration.OutputConfiguration;
import org.hisrc.jsonix.configuration.PackageMapping;
import org.hisrc.jsonix.xml.sax.LocatorUtils;
import org.jvnet.jaxb2_commons.util.CustomizationUtils;

import com.sun.tools.xjc.model.CPluginCustomization;
import com.sun.tools.xjc.model.Model;

public class CustomizationHandler {

	private final Log log;
	private final JAXBContext context;

	public CustomizationHandler(final Log log) {
		Validate.notNull(log);
		this.log = log;
		try {
			context = JAXBContext.newInstance(ModulesConfiguration.class,
					ModuleConfiguration.class, MappingConfiguration.class,
					OutputConfiguration.class, PackageMapping.class);
		} catch (JAXBException jaxbex) {
			throw new ExceptionInInitializerError(jaxbex);
		}
	}

	private static final List<String> CUSTOMIZATION_URIS = Collections
			.singletonList(ModulesConfiguration.NAMESPACE_URI);

	private static final Set<String> CUSTOMIZATION_LOCAL_ELEMENT_NAMES = new HashSet<String>(
			Arrays.asList(PackageMapping.LOCAL_ELEMENT_NAME,
					ModulesConfiguration.LOCAL_ELEMENT_NAME,
					ModuleConfiguration.LOCAL_ELEMENT_NAME,
					MappingConfiguration.LOCAL_ELEMENT_NAME,
					OutputConfiguration.LOCAL_ELEMENT_NAME));

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
			final T value = (T) context.createUnmarshaller().unmarshal(
					customization.element);
			return value;
		} catch (JAXBException jaxbex) {
			final String location = LocatorUtils
					.toString(customization.locator);
			final String message = location == null ? MessageFormat.format(
					"Could not unmarshal the {0}.", description)
					: MessageFormat.format(
							"Could not unmarshal the {0} located at [{1}].",
							description, location);
			throw new IllegalArgumentException(message, jaxbex);
		}
	}

	public ModulesConfiguration unmarshal(Model model) {
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
		return modulesConfiguration;
	}

	private MappingConfiguration unmarshalPackageMapping(
			CPluginCustomization customization) {
		final PackageMapping packageMapping = unmarshal(customization,
				"package mapping");
		log.warn("The [packageMapping] customization is deprecated, please use the [mapping] customization in the future.");

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
}
