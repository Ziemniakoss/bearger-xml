package pl.ziemniakos.bearger_xml

import org.junit.jupiter.api.Test
import pl.ziemniakos.bearger_xml.configs.*
import java.nio.file.Paths
import kotlin.io.path.createTempFile
import kotlin.io.path.readText

internal class XmlNodeDiffWriterTest {
	@Test
	fun createDiffFile_onlyProperties() {
		testCreateDiffFile("onlyProperties")
	}

	@Test
	fun createDiffFile_childrenAndProperties() {
		testCreateDiffFile("childrenAndProperties")
	}

	@Test
	fun createDiffFile_sortingOrder() {
		testCreateDiffFile("sortingOrder")
	}

	private fun testCreateDiffFile(testName: String) {
		val utils = TestingUtils()
		val original = XmlNode.fromFile(utils.getTestResourcesPath(testName, "original.xml"))
		val merged = XmlNode.fromFile(utils.getTestResourcesPath(testName, "merged.xml"))

		val resultFile = createTempFile()
		val diffWriter = XmlNodeDiffWriter(
			UsersMergingConfigSupplier(),
			UsersPrintingConfigSupplier()
		)
		diffWriter.createDiffFile(original, merged, resultFile)
		val result = resultFile.readText()
		val expected = Paths.get(utils.getTestResourcesPath(testName, "expected.xml")).readText()
		utils.assertEquals(expected, result)

	}
}

class UsersMergingConfigSupplier : IMergingConfigSupplier {
	override fun getMergingConfig(topNodeName: String): MergingConfig {
		return MergingConfig(mapOf(
			"User" to MergingStrategy(listOf("id")),
			"BodyPart" to MergingStrategy(listOf("name", "side")),
			"labels" to MergingStrategy(listOf("fullName"))
		))
	}
}

class UsersPrintingConfigSupplier : IPrintingConfigSupplier {
	override fun getPrintingConfig(topNodeName: String): PrintingConfig {
		return PrintingConfig(
			printingOrder = mapOf(
				"User" to listOf("id", "name", "surname", "age", "description"),
				"BodyPart" to listOf("name", "side", "description", "status"),
				"labels" to listOf("fullName", "categories", "language", "protected", "shortDescription", "value")
			))
	}
}