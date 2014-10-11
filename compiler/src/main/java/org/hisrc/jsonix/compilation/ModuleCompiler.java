package org.hisrc.jsonix.compilation;

import org.apache.commons.lang3.Validate;
import org.hisrc.jscm.codemodel.JSCodeModel;
import org.hisrc.jscm.codemodel.JSProgram;
import org.hisrc.jscm.codemodel.expression.JSFunctionExpression.Function;
import org.hisrc.jscm.codemodel.expression.JSGlobalVariable;
import org.hisrc.jscm.codemodel.expression.JSObjectLiteral;
import org.hisrc.jscm.codemodel.statement.JSBlock;
import org.hisrc.jscm.codemodel.statement.JSIfStatement;
import org.hisrc.jscm.codemodel.statement.JSVariableStatement;
import org.hisrc.jsonix.definition.Mapping;
import org.hisrc.jsonix.definition.Module;
import org.hisrc.jsonix.definition.Modules;
import org.hisrc.jsonix.definition.Output;

public class ModuleCompiler<T, C extends T> {

	private final JSCodeModel codeModel;

	private Output output;
	private Modules<T, C> modules;
	private Module<T, C> module;
	private final String moduleName;

	public ModuleCompiler(JSCodeModel codeModel, Modules<T, C> modules,
			Module<T, C> module, Output output) {
		Validate.notNull(codeModel);
		Validate.notNull(modules);
		Validate.notNull(module);
		Validate.notNull(output);
		this.codeModel = codeModel;
		this.modules = modules;
		this.module = module;
		this.moduleName = module.getName();
		this.output = output;
	}

	public JSProgram compile() {
		final JSProgram moduleProgram = codeModel.program();

		final Function moduleFactoryFunction = codeModel.function();

		final JSVariableStatement moduleFactoryVariable = moduleProgram.var(
				this.moduleName + "_Module_Factory", moduleFactoryFunction);

		final JSObjectLiteral moduleFactoryResult = codeModel.object();
		for (Mapping<T, C> mapping : this.module.getMappings()) {
			if (!mapping.isEmpty()) {
				final String mappingName = mapping.getMappingName();
				final MappingCompiler<T, C> mappingCompiler = new MappingCompiler<T, C>(
						codeModel, modules, module, output, mapping);

				final JSObjectLiteral mappingBody = mappingCompiler.compile();

				final JSVariableStatement mappingVariable = moduleFactoryFunction
						.getBody().var(mappingName, mappingBody);
				moduleFactoryResult.append(mappingName,
						mappingVariable.getVariable());
			}
		}
		moduleFactoryFunction.getBody()._return(moduleFactoryResult);

		final JSGlobalVariable define = this.codeModel.globalVariable("define");
		final JSIfStatement ifDefine = moduleProgram._if(define.typeof()
				.eeq(codeModel.string("function")).and(define.p("amd")));

		ifDefine._then()
				.block()
				.expression(
						define.i().args(codeModel.array(),
								moduleFactoryVariable.getVariable()));

		final JSGlobalVariable module = this.codeModel.globalVariable("module");
		final JSBlock ifNotDefineBlock = ifDefine._else().block();

		final JSVariableStatement moduleInstance = ifNotDefineBlock
				.var(moduleName + "_Module", moduleFactoryVariable
						.getVariable().i());
		final JSIfStatement ifModuleExports = ifNotDefineBlock._if(module
				.typeof().nee(codeModel.string("undefined"))
				.and(module.p("exports")));

		final JSBlock moduleExportsThenBlock = ifModuleExports._then().block();

		for (Mapping<T, C> mapping : this.module.getMappings()) {
			if (!mapping.isEmpty()) {
				final String mappingName = mapping.getMappingName();
				moduleExportsThenBlock.expression(module.p("exports")
						.p(mappingName)
						.assign(moduleInstance.getVariable().p(mappingName)));
			}
		}
		final JSBlock moduleExportsElseBlock = ifModuleExports._else().block();
		for (Mapping<T, C> mapping : this.module.getMappings()) {
			if (!mapping.isEmpty()) {
				final String mappingName = mapping.getMappingName();
				moduleExportsElseBlock.var(mappingName, moduleInstance
						.getVariable().p(mappingName));
			}
		}
		return moduleProgram;
	}
}
