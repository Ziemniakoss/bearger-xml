package pl.ziemniakos.bearger_xml

import pl.ziemniakos.bearger_xml.configs.merging.MergingConfigSupplier
import pl.ziemniakos.bearger_xml.configs.printing.PrintingConfigSupplier
import java.nio.file.Paths

/**
 * Prints diff of 2 xml files(if they have same top tag name) in
 */
class Differ(
	private val file1: String,
	private val file2: String,
	private val outputFile: String
) : IProgram {
	override fun run() {
		val node1 = XmlNode.fromFile(file1)
		val node2 = XmlNode.fromFile(file2)
		XmlNodeDiffWriter(MergingConfigSupplier(), PrintingConfigSupplier()).createDiffFile(node1, node2, Paths.get(outputFile))
	}
}
