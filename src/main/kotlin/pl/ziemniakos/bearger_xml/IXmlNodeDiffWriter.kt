package pl.ziemniakos.bearger_xml

import java.nio.file.Path

interface IXmlNodeDiffWriter {
	fun createDiffFile(originalNode: XmlNode, mergedNode: XmlNode, outputPath: Path)
}
