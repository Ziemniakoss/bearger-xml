package pl.ziemniakos.bearger_xml.configs

class PrintingConfig(
	val indentSize: Int = 4,
	val useTabs: Boolean = false,
	val newLineCharacter: NewLineCharacter = NewLineCharacter.NORMAL,
	val printingOrder: Map<String, List<String>> = mapOf()
) {
}