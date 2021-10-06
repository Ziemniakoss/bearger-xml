package pl.ziemniakos.bearger_xml

import java.nio.file.Paths
import kotlin.io.path.Path
import kotlin.io.path.bufferedWriter
import kotlin.math.max

class TestingUtils {
	fun getTestResourcesPath(vararg path: String): String {
		val joinedPath = "/${path.joinToString("/")}"
		return this::class.java.getResource(joinedPath).file.toString()
	}

	fun assertEquals(expected: String, result: String) {
		val splitedExpected = expected.split("\n")//.map { it.replace(" ", "<S>").replace("\n", "<N>").replace("\r", "<R>") }
		val splitedResult = result.split("\n")//.map { it.replace(" ", "<S>").replace("\n", "<N>").replace("\r", "<R>") }
		var mismatched = false
		matchIntoPairs(splitedExpected, splitedResult)
			.forEachIndexed { index, it ->
				if (it.first != it.second) {
					println("Mismatch in line ${index + 1}:")
					println("EXPECTED: ${it.first}")
					println("RESULT:   ${it.second}")
					mismatched = true
				}
			}
		if(mismatched) {
			val output = Paths.get("test-result.xml").bufferedWriter()
			output.write(result)
			output.close()
			throw AssertionError("Content mismatched, see log for more info")
		}

	}

	private fun matchIntoPairs(first: List<String>, second: List<String>): List<Pair<String?, String?>> {
		val maxLength = max(first.size, second.size)
		val linesPairs = mutableListOf<Pair<String?, String?>>()
		for (i in 0 until maxLength) {
			linesPairs.add(Pair(
				first.getOrElse(i) { null },
				second.getOrElse(i) { null }
			))
		}
		return linesPairs
	}
}
