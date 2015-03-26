# Jsonix Schema Compiler

Generates [Jsonix](https://github.com/highsource/jsonix) mappings for XML Schemas.

Please refer to [Wiki](https://github.com/highsource/jsonix-schema-compiler/wiki) for documentation.

This package provides the Jsonix Schema Compiler (under `lib/jsonix-schema-compiler-full.jar`).
So you can invoke the schema compiler via [command line](https://github.com/highsource/jsonix-schema-compiler/wiki/Command-Line-Usage) as follows:

```
java -jar node_modules/jsonix-schema-compiler/lib/jsonix-schema-compiler-full.jar schema.xsd
```

# Usage

Typical usage is as follows:

* Make your package depend on `jsonix-schema-compiler`.
* Invoke the Jsonix Schema Compiler in `scripts/prepublish`.

## Example

```javascript
{
	"name": "mypackage",
	...
	"dependencies": {
		...
		"jsonix-schema-compiler": "~<VERSION>"
		...
	},
	...
	"scripts": {
		...
		"prepublish" : "java -jar node_modules/jsonix-schema-compiler/lib/jsonix-schema-compiler-full.jar schema.xsd"
		...
	}
	...
}
```