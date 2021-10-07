import pl.ziemniakos.bearger_xml.ArgumentParser
import pl.ziemniakos.bearger_xml.IArgumentsParser
import kotlin.system.exitProcess

fun main(args: Array<String>) {
	val map = mapOf(
		listOf("Ala", "ma", "kota") to 123,
		listOf("pies") to 90,
	)
	println(map[listOf("pies")])
	val argParser: IArgumentsParser = ArgumentParser()
	try {
		argParser.parseArguments(args).run()
	} catch (_: IllegalArgumentException) {
		exitProcess(1)
	}
}
