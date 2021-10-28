package pl.ziemniakos.bearger_xml.sorting

import pl.ziemniakos.bearger_xml.configs.printing.IPrintingConfigSupplier
import java.nio.file.Path

class XmlSorter(private val sortingConfigSupplier: IPrintingConfigSupplier)  :IXmlSorter{
	override fun sortXml(inputFile: Path, outputPath: Path) {
		TODO("Not yet implemented")
	}
}
