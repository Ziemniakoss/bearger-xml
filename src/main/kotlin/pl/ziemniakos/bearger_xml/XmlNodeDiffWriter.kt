package pl.ziemniakos.bearger_xml

import pl.ziemniakos.bearger_xml.configs.*
import java.io.BufferedWriter
import java.nio.file.Path
import kotlin.io.path.bufferedWriter

class XmlNodeDiffWriter(
	private val mergingConfigSupplier: IMergingConfigSupplier,
	private val printingConfigSupplier: IPrintingConfigSupplier,
	private val originalBranchName: String = "ORIGINAL",
	private val mergedBranchName: String = "MERGED",
) : IXmlNodeDiffWriter {
	override fun createDiffFile(originalNode: XmlNode, mergedNode: XmlNode, outputPath: Path) {
		if (originalNode.nodeName != mergedNode.nodeName) {
			throw IllegalArgumentException("""Top xml nodes names does not match, original "${originalNode.nodeName}", merged "${mergedNode.nodeName}"""")
		}
		val printingConfig = printingConfigSupplier.getPrintingConfig(originalNode.nodeName)
		val mergingConfig = mergingConfigSupplier.getMergingConfig(originalNode.nodeName)
		val output = outputPath.bufferedWriter()

		writeXmlDefinition(output, originalNode, mergedNode, printingConfig)
		writeNodeDiff(output, originalNode, mergedNode, 0, printingConfig, mergingConfig)
		output.close()
	}

	private fun writeNodeDiff(output: BufferedWriter, originalNode: XmlNode?, mergedNode: XmlNode?, indent: Int, printingConfig: PrintingConfig, mergingConfig: MergingConfig) {
		if(originalNode == null && mergedNode == null) {
			return
		}
		if(originalNode == null) {
			writeStartOfOriginalVersionTag(output, originalBranchName, printingConfig)
			writeVersionSeparationTag(output, printingConfig)
			writeNode(output, mergedNode!!, indent, printingConfig, mergingConfig)
			writeEndOfMergedVersionTag(output, mergedBranchName, printingConfig)
			return
		} else if(mergedNode == null) {
			writeStartOfOriginalVersionTag(output, originalBranchName, printingConfig)
			writeNode(output, originalNode, indent, printingConfig, mergingConfig)
			writeVersionSeparationTag(output,printingConfig)
			writeEndOfMergedVersionTag(output, mergedBranchName, printingConfig)
			return
		}
		indent(output, indent, printingConfig)
		output.apply {
			write("<")
			write(originalNode.nodeName)
			write(">")
		}
		writeNewLine(output, printingConfig)

		val allPropertiesAndNodeNames = mutableSetOf<String>()
		allPropertiesAndNodeNames.apply {
			addAll(originalNode.children.keys)
			addAll(originalNode.properties.keys)
			addAll(mergedNode.properties.keys)
			addAll(mergedNode.children.keys)
		}

		for(propertyOrNodeName in printingConfig.printingOrder[originalNode.nodeName] ?: listOf()) {
			writePropertiesWithNameInDiff(output, propertyOrNodeName, originalNode, mergedNode, indent + 1, printingConfig)
			writeChildNodesWithName(output, propertyOrNodeName, originalNode, mergedNode, indent, printingConfig, mergingConfig)
			allPropertiesAndNodeNames.remove(propertyOrNodeName)
		}


		for(propertyOrNodeName in allPropertiesAndNodeNames) {
			writePropertiesWithNameInDiff(output, propertyOrNodeName, originalNode, mergedNode, indent + 1, printingConfig)
			writeChildNodesWithName(output, propertyOrNodeName, originalNode, mergedNode, indent, printingConfig, mergingConfig)
		}

		indent(output, indent, printingConfig)
		output.apply {
			write("</")
			write(originalNode.nodeName)
			write(">")
		}
		writeNewLine(output, printingConfig)
	}

	private fun warmAboutNodeDuplicates(nodesMap: Map<String, List<XmlNode>>) {
		for((key, nodes) in nodesMap.entries) {
			if(nodes.size > 1) {
				println("WARN: ${nodes.size} nodes for key $key, taking last value")
			}
		}
	}

	private fun createKey(xmlNode: XmlNode, keyFields: List<String>): String{
		return keyFields
			.map { xmlNode.properties[it] }
			.joinToString("<<>>")
	}

	private fun writeChildNodesWithName(
		output: BufferedWriter,
		propertyOrNodeName: String,
		originalNode: XmlNode,
		mergedNode: XmlNode,
		indent: Int,
		printingConfig: PrintingConfig,
		mergingConfig: MergingConfig
	){

		val originalNodeChildrenWithThatName = originalNode.children[propertyOrNodeName] ?: listOf()
		val mergedNodeChildrenWithThatName = mergedNode.children[propertyOrNodeName] ?: listOf()
		if(originalNodeChildrenWithThatName.isEmpty()) {
			mergedNodeChildrenWithThatName.forEach { writeNode(output, it, indent, printingConfig, mergingConfig) }
		} else if(mergedNodeChildrenWithThatName.isEmpty()) {
			originalNodeChildrenWithThatName.forEach { writeNode(output, it, indent, printingConfig, mergingConfig) }
		} else if(mergedNodeChildrenWithThatName.size == originalNodeChildrenWithThatName.size && originalNodeChildrenWithThatName.size == 1) {
			writeNodeDiff(output, originalNodeChildrenWithThatName[0], mergedNodeChildrenWithThatName[0], indent + 1, printingConfig, mergingConfig)
		} else {
			val mergingStrategy = mergingConfig.mergingStrategies[propertyOrNodeName] ?: throw RuntimeException("Can\t merge nodes ${propertyOrNodeName}, please specify key fields for this node in config file")
			val originalChildrenNodesGroupedByKeyFields = originalNodeChildrenWithThatName.groupBy { createKey(it, mergingStrategy.keyFields) }
			val mergedChildrenNodesGroupedByKeyFields = mergedNodeChildrenWithThatName.groupBy { createKey(it, mergingStrategy.keyFields) }
			warmAboutNodeDuplicates(originalChildrenNodesGroupedByKeyFields)
			warmAboutNodeDuplicates(mergedChildrenNodesGroupedByKeyFields)
			val allKeys = setOf(*originalChildrenNodesGroupedByKeyFields.keys.toTypedArray(), *mergedChildrenNodesGroupedByKeyFields.keys.toTypedArray())
			for(key in allKeys) {
				writeNodeDiff(output, originalChildrenNodesGroupedByKeyFields[key]?.last(), mergedChildrenNodesGroupedByKeyFields[key]?.last(), indent + 1, printingConfig, mergingConfig)
			}
		}
	}

	private fun writePropertiesWithNameInDiff(
		output: BufferedWriter,
		propertyOrNodeName: String,
		originalNode: XmlNode,
		mergedNode: XmlNode,
		indent: Int,
		printingConfig: PrintingConfig
	){
		if(originalNode.properties[propertyOrNodeName] != mergedNode.properties[propertyOrNodeName]) {
			writeStartOfOriginalVersionTag(output, originalBranchName, printingConfig)
			writeProperty(output, propertyOrNodeName, originalNode.properties[propertyOrNodeName], indent, printingConfig)
			writeVersionSeparationTag(output, printingConfig)
			writeProperty(output,propertyOrNodeName, mergedNode.properties[propertyOrNodeName], indent, printingConfig)
			writeEndOfMergedVersionTag(output, mergedBranchName, printingConfig)
		} else {
			writeProperty(output, propertyOrNodeName, originalNode.properties[propertyOrNodeName], indent, printingConfig)
		}
	}

	private fun writeNode(output: BufferedWriter, xmlNode: XmlNode, indent: Int, printingConfig: PrintingConfig, mergingConfig: MergingConfig) {
		val allPropertiesAndNodesNames = mutableSetOf(*xmlNode.properties.keys.toTypedArray(), *xmlNode.children.keys.toTypedArray())
		indent(output, indent, printingConfig)
		output.apply {
			write("<")
			write(xmlNode.nodeName)
			write(">")
		}
		writeNewLine(output, printingConfig)

		val sortedRemainingPropertiesAndNodeNames = allPropertiesAndNodesNames.sorted()
		for (propertyOrNodeName in sortedRemainingPropertiesAndNodeNames) {
			val propertyValue = xmlNode.properties[propertyOrNodeName]
			if (propertyValue != null) {
				writeProperty(output, propertyOrNodeName, propertyValue, indent + 1, printingConfig)
			}
			val childNodes = xmlNode.children[propertyOrNodeName]
			childNodes?.forEach { writeNode(output, it, indent + 1, printingConfig, mergingConfig) }
		}
		indent(output, indent, printingConfig)
		output.apply {
			write("</")
			write(xmlNode.nodeName)
			write(">")
		}
		writeNewLine(output, printingConfig)
	}

	private fun writeProperty(output: BufferedWriter, propertyName: String, propertyValue: String?, indent: Int, printingConfig: PrintingConfig) {
		if(propertyValue == null) {
			return
		}
		indent(output, indent, printingConfig)
		output.apply {
			write("<")
			write(propertyName)
			write(">")

			write(propertyValue)

			write("</")
			write(propertyName)
			write(">")
		}
		writeNewLine(output, printingConfig)
	}

	private fun writeXmlDefinition(output: BufferedWriter, originalNode: XmlNode, mergedNode: XmlNode, printingConfig: PrintingConfig) {
		//TODO actual xml definition
		output.write("""<?xml version="1.0" encoding="UTF-8"?>""")
		writeNewLine(output, printingConfig)
	}


	private fun writeStartOfOriginalVersionTag(output: BufferedWriter, originalBranchName: String, printingConfig: PrintingConfig) {
		output.write("<<<<<<< ")
		output.write(originalBranchName)
		writeNewLine(output, printingConfig)
	}

	private fun writeVersionSeparationTag(output: BufferedWriter, printingConfig: PrintingConfig) {
		output.write("=======")
		writeNewLine(output, printingConfig)
	}

	private fun writeEndOfMergedVersionTag(output: BufferedWriter, mergedBranchName: String, printingConfig: PrintingConfig) {
		output.write(">>>>>>> ")
		output.write(mergedBranchName)
		writeNewLine(output, printingConfig)
	}

	private fun writeNewLine(output: BufferedWriter, printingConfig: PrintingConfig) {
		when (printingConfig.newLineCharacter) {
			NewLineCharacter.NORMAL -> output.write("\n")
			NewLineCharacter.WINDOWS -> output.write("\r\n")
		}
	}

	private fun indent(output: BufferedWriter, indent: Int, printingConfig: PrintingConfig) {
		val indentCharacter = if (printingConfig.useTabs) {
			"\t"
		} else {
			" "
		}
		for (i in 0 until indent * printingConfig.indentSize) {
			output.write(indentCharacter)
		}
	}
}