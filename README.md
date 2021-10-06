# Bearger XML

Tool for creating useful diffs in xml files.

[LOGO HERE]

## Why?

Git can create useless diffs when resolving conflicts in xml files.
What's more, it can duplicate one part of file and drop another which makes it dangerous when using xmls to store configuration.
This tool fetches version of file from two refs (branches, commit hashes...), builds xml tree and diffs it.

## Warning

This tool(at least for now) does not support xml attributes and namespaces.
They will not be printed out in diff file and should be added manually.

I should probably state that you are using this tool at your own risk and this tool (like any other) may contain bugs which can lead to data loss.

## How to use

### Merging configuration files

Configuration files are stored in 
```
~/.config/bearger-xml/merging/
```
In this folder each top xml tag should have its config file with names like
```
TopTagName.json
```

Example if we want to create configuration file for merging [salesforce labels files](https://developer.salesforce.com/docs/atlas.en-us.api_meta.meta/api_meta/meta_customlabels.htm):
```xml
<?xml version="1.0" encoding="UTF-8"?>
<CustomLabels xmlns="http://soap.sforce.com/2006/04/metadata">
    <labels>
        <fullName>quoteManual</fullName>
        <value>This is a manual quote.</value>
        <language>en_US</language>
        <protected>false</protected>
        <shortDescription>Manual Quote</shortDescription>
    </labels>
    <labels>
        <fullName>quoteAuto</fullName>
        <value>This is an automatically generated quote.</value>
        <language>en_US</language>
        <protected>false</protected>
        <shortDescription>Automatic Quote</shortDescription>
    </labels>
</CustomLabels>
```
configuration should be placed in file
```
~/.config/bearger-xml/merging/CustomLabels.json
```
JSON Schema for configuration files is specified in [this file](merging-config.schema.json).
Example config files are stored in [this folder](configs)

### Main config file (TODO)

Main config file is optional and is stored in file
```
~/.config/bearger-xml/config.json
```

JSON Schema for main config file is specified in [this file](printing-config.schema.json).
### Running

```
java -jar pathToJarWithCompiledBearger.jar sourceBranchName mergedBranchName fileNames
```

# TODO

- Main config file support HEHEHEHE
- support for namespaces and attributes
