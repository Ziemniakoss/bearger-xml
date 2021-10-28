package pl.ziemniakos.bearger_xml.sorting

import java.nio.file.Path

interface IXmlSorter {
	/**
	 * Sorts xml from input file and outputs result to outputPath.
	 * For more info about sorting rules read README.md
	 */
	fun sortXml(inputFile: Path, outputPath: Path)

	/**
	 * Sorts xml in place.
	 * For more info about sorting rules read README.md
	 */
	fun sortXml(file: Path) = sortXml(file, file)
}
