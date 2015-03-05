myrddin
=======

[![Build Status](https://api.travis-ci.org/livingobjects/myrddin.png)](https://travis-ci.org/livingobjects/myrddin)

Swagger yaml (v2.0 format) file parser in Java.

This library allow you to read API REST documentation written in Swagger YAML format (spec 2.0).

Not all specification is implemented yet.

The library is an OSGI bundle which depends on snakeyaml and guava.

# Format

See the following examples to see supported format : https://github.com/livingobjects/myrddin/tree/develop/src/test/resources

# Example

The supported Json schema types are:

## Base for all schema:

```yaml
schema:
  type | $ref | oneOf | allOf
  title: XXX
  description: XXX
```

## Basic json types

### array

```yaml
schema:
  type: array
  items:
    <schema>
```

### object

```yaml
schema:
  type: object
  properties:
    <property_name>:
      <schema>
  required:
    - <required_prop_name>
```

### number, integer

```yaml
schema:
  type: number
  format: <format_as_string> (ex: "int32", "double")
  multipleOf: <integer>
  minimum: <integer>
  maximum: <integer>
  exclusiveMinimum: <boolean>
  exclusiveMaximum: <boolean>
```

### string

```yaml
schema:
  type: string
  format: <format_as_string> (ex: "email")
  pattern: <regexp_pattern_as_string>
  minLength: <integer>
  maxLength: <integer>
```

### boolean

```yaml
schema:
  type: boolean
```
  
## Reference a defined type:

```yaml
schema:
  $ref: <the_reference_model_as_string> (Ex: '#/definitions/TypeName')
```

## Type combinaisons:

### anyOf, oneOf

```yaml
schema:
  anyOf:
    - subtype1
    - subtype2
```

```yaml
schema:
  oneOf:
    - subtype1
    - subtype2
```

