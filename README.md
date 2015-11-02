# Jsonix Schema Compiler #

Generates [Jsonix](https://github.com/highsource/jsonix) mappings for XML Schemas.

Please refer to [Wiki](https://github.com/highsource/jsonix-schema-compiler/wiki) for documentation.

## Using in command-line

```
npm install jsonix-schema-compiler
java -jar node_modules/jsonix-schema-compiler/lib/jsonix-schema-compiler-full.jar
  [-compact -logLevel TRACE -generateJsonJsonChema]
  schema.xsd
  [-b bindings.xjb]
```

See [Command-Line Usage](https://github.com/highsource/jsonix-schema-compiler/wiki/Command-Line-Usage).

## Using with Ant

TBD

See [Ant Usage](https://github.com/highsource/jsonix-schema-compiler/wiki/Ant-Usage).

## Using with Maven

```xml
<plugin>
	<groupId>org.jvnet.jaxb2.maven2</groupId>
	<artifactId>maven-jaxb2-plugin</artifactId>
	<configuration>
		<extension>true</extension>
		<args>
			<arg>-Xjsonix</arg>
			<arg>-Xjsonix-compact</arg>
		</args>
		<plugins>
			<plugin>
				<groupId>org.hisrc.jsonix</groupId>
				<artifactId>jsonix-schema-compiler</artifactId>
				<version>${jsonix-schema-compiler.version}</version>
			</plugin>
		</plugins>
	</configuration>
</plugin>
```

See [Maven Usage](https://github.com/highsource/jsonix-schema-compiler/wiki/Maven-Usage).
