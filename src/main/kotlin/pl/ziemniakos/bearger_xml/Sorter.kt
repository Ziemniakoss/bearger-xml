package pl.ziemniakos.bearger_xml

import pl.ziemniakos.bearger_xml.configs.MergingConfigSupplier
import pl.ziemniakos.bearger_xml.configs.PrintingConfigSupplier
import java.nio.file.Paths

class Sorter(
	private val fileNames: Set<String>,
	private val useOriginalFile: Boolean,
) : IProgram {
	override fun run() {
		fileNames.forEach { sortFile(it, useOriginalFile) }
	}

	private fun sortFile(fileName: String, useOriginalFile: Boolean) {
		val node = XmlNode.fromFile(fileName)
		val outputPath = if (useOriginalFile) {
			Paths.get(fileName)
		} else {
			Paths.get("$fileName.sorted")
		}
		XmlNodeDiffWriter(MergingConfigSupplier(), PrintingConfigSupplier()).createDiffFile(node, node, outputPath)
	}
}
