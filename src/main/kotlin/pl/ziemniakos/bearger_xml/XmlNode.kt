package pl.ziemniakos.bearger_xml

import com.sun.org.apache.xerces.internal.dom.DeferredCommentImpl
import com.sun.org.apache.xerces.internal.dom.DeferredTextImpl
import org.w3c.dom.Document
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import pl.ziemniakos.bearger_xml.configs.MergingConfig
import javax.xml.parsers.DocumentBuilderFactory

class XmlNode(
	val properties: Map<String, String>,
	val children: Map<String, List<XmlNode>>,
	val nodeName: String,
	parent: XmlNode?,
) {
	var parent: XmlNode?
		private set

	init {
		this.parent = parent
	}

	companion object {
		fun fromFile(fileName: String): XmlNode {
			val parsedXml = DocumentBuilderFactory
				.newInstance()
				.newDocumentBuilder()
				.parse(fileName)

			return fromNode(parsedXml.getChildren().first(), null)

		}

		private fun fromNode(node: Node, parent: XmlNode? = null): XmlNode {
			val properties = mutableMapOf<String, String>()
			val childNodes = mutableListOf<XmlNode>()
			for (childNode in node.getChildren().dropCommentsAndEmptyTexts()) {
				if (childNode.isProperty()) {
					if (properties.containsKey(childNode.nodeName)) {
						println("Duplicate property ${childNode.nodeName} with value ${childNode.textContent} found, overriting")
					}
					properties[childNode.nodeName] = childNode.textContent.trim()
				} else {
					childNodes.add(fromNode(childNode))
				}
			}
			val resultingNode = XmlNode(properties, childNodes.groupBy { it.nodeName }, node.nodeName, parent)
			for (childNode in childNodes) {
				childNode.parent = resultingNode
			}
			return resultingNode
		}
	}

	fun getParentKey(mergingConfig: MergingConfig): List<String> {
		val key = mutableListOf<String>()
		generateParentKey(mergingConfig, key)
		return  key;
	}

	private fun generateParentKey(mergingConfig: MergingConfig, key: MutableList<String>) {
		if(parent == null) {
			return
		}
		key.add(0, parent!!.generateKey(mergingConfig))

	}

	fun generateKey(mergingConfig: MergingConfig): String {
		val mergingStrategy = mergingConfig.mergingStrategies[nodeName] ?: throw IllegalArgumentException("No merging strategy defined for node $nodeName")
		return mergingStrategy.keyFields.joinToString("<<>>") { properties[it] ?: "" }
	}
}

fun Document.getChildren(): List<Node> = convertToKotlinList(childNodes)

fun Node.getChildren(): List<Node> = convertToKotlinList(childNodes)

fun Node.isProperty(): Boolean = getChildren().dropCommentsAndEmptyTexts().isEmpty()

fun List<Node>.dropCommentsAndEmptyTexts(): List<Node> {
	return filter { it !is DeferredCommentImpl && it !is DeferredTextImpl }
}

fun convertToKotlinList(nodeList: NodeList): List<Node> {
	val nodes = mutableListOf<Node>()
	for (i in 0 until nodeList.length) {
		nodes.add(nodeList.item(i))
	}
	return nodes
}