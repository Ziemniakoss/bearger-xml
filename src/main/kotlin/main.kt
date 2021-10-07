import pl.ziemniakos.bearger_xml.ArgumentParser
import pl.ziemniakos.bearger_xml.IArgumentsParser
import java.io.FileNotFoundException
import kotlin.system.exitProcess

fun main(args: Array<String>) {
	val argParser: IArgumentsParser = ArgumentParser()
	try {
		argParser.parseArguments(args).run()
	} catch (_: IllegalArgumentException) {
		exitProcess(1)
	} catch (e: NotImplementedError) {
		println(e.message)
		exitProcess(2)
	} catch (e: FileNotFoundException) {
		println(e.message)
		exitProcess(3)
	}
}
