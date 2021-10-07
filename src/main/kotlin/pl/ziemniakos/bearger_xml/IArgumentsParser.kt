package pl.ziemniakos.bearger_xml

interface IArgumentsParser {
	fun parseArguments(args: Array<String>): IProgram
}
