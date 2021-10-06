package pl.ziemniakos.bearger_xml

import com.sun.org.apache.xerces.internal.dom.DeferredCommentImpl
import com.sun.org.apache.xerces.internal.dom.DeferredTextImpl
import org.w3c.dom.Document
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import javax.xml.parsers.DocumentBuilderFactory

class XmlNode(
	val properties: Map<String, String>,
	val children: Map<String, List<XmlNode>>,
	val nodeName: String
) {
	companion object {
		fun fromFile(fileName: String): XmlNode {
			val parsedXml = DocumentBuilderFactory
				.newInstance()
				.newDocumentBuilder()
				.parse(fileName)

			return fromNode(parsedXml.getChildren().first())

		}

		fun fromNode(node: Node): XmlNode {
			val properties = mutableMapOf<String, String>()
			val childNodes = mutableListOf<XmlNode>()
			for (childNode in node.getChildren().dropCommentsAndEmptyTexts()) {
				if(childNode.isProperty()) {
					if(properties.containsKey(childNode.nodeName)) {
						println("Duplicate property ${childNode.nodeName} with value ${childNode.textContent} found, overriting")
					}
					properties[childNode.nodeName] = childNode.textContent.trim()
				} else {
					childNodes.add(fromNode(childNode))
				}
			}

			return XmlNode(properties, childNodes.groupBy { it.nodeName }, node.nodeName)
		}
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