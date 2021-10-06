package pl.ziemniakos.bearger_xml

import org.junit.jupiter.api.Test
import pl.ziemniakos.bearger_xml.configs.*
import java.nio.file.Paths
import kotlin.io.path.createTempFile
import kotlin.io.path.readText

internal class XmlNodeDiffWriterTest {
	@Test
	fun createDiffFile_onlyProperties() {
		val utils = TestingUtils()
		val original = XmlNode.fromFile(utils.getTestResourcesPath("onlyProperties", "original.xml"))
		val merged = XmlNode.fromFile(utils.getTestResourcesPath("onlyProperties", "merged.xml"))

		val resultFile = createTempFile()

		val diffWriter = XmlNodeDiffWriter(
			UsersMergingConfigSupplier(),
			UsersPrintingConfigSupplier()
		)
		diffWriter.createDiffFile(original, merged, resultFile)
		val result = resultFile.readText()
		val expected = Paths.get(utils.getTestResourcesPath("onlyProperties", "expected.xml")).readText()
		utils.assertEquals(expected, result)
	}

	@Test
	fun createDiffFile_childrenAndProperties() {
		val utils = TestingUtils()
		val original = XmlNode.fromFile(utils.getTestResourcesPath("childrenAndProperties", "original.xml"))
		val merged = XmlNode.fromFile(utils.getTestResourcesPath("childrenAndProperties", "merged.xml"))

		val resultFile = createTempFile()
		val diffWriter = XmlNodeDiffWriter(
			UsersMergingConfigSupplier(),
			UsersPrintingConfigSupplier()
		)
		diffWriter.createDiffFile(original, merged, resultFile)
		val result = resultFile.readText()
		val expected = Paths.get(utils.getTestResourcesPath("childrenAndProperties", "expected.xml")).readText()
		utils.assertEquals(expected, result)
	}
}

class UsersMergingConfigSupplier : IMergingConfigSupplier {
	override fun getMergingConfig(topNodeName: String): MergingConfig {
		return MergingConfig(mapOf(
			"User" to MergingStrategy(listOf("id")),
			"BodyPart" to MergingStrategy(listOf("name", "side"))
		))
	}
}

class UsersPrintingConfigSupplier : IPrintingConfigSupplier {
	override fun getPrintingConfig(topNodeName: String): PrintingConfig {
		return PrintingConfig(printingOrder = mapOf(
			"User" to listOf("id", "name", "surname", "age"),
			"BodyPart" to listOf("name", "side", "description", "status")
		))
	}
}