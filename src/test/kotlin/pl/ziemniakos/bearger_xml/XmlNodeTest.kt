package pl.ziemniakos.bearger_xml

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

internal class XmlNodeTest {
	@Test
	fun fromFile() {
		val path = XmlNodeTest::class.java.getResource("/sampleSfObject.xml").file.toString()
		val xmlNode = XmlNode.fromFile(path)

		assertEquals(3, xmlNode.properties.size)
		assert(xmlNode.properties.containsKey("label"))
		assert(xmlNode.properties.containsKey("pluralLabel"))
		assert(xmlNode.properties.containsKey("visibility"))

		assertEquals(1, xmlNode.children.size)
		assert(xmlNode.children.containsKey("fields"))

		val parsedField = xmlNode.children["fields"]!!.first()
		assertEquals(0, parsedField.children.size)
		assertEquals(xmlNode, parsedField.parent)

		val parsedFieldProperties = parsedField.properties
		assertEquals(5, parsedFieldProperties.size)
	}
}
