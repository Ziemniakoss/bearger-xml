package pl.ziemniakos.bearger_xml

interface IXmlNodeComparator {
	fun createComparison(original: XmlNode, modified: XmlNode): Map<List<String>, List<XmlNodeChange>>
}