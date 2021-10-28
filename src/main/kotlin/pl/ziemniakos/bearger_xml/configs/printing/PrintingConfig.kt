package pl.ziemniakos.bearger_xml.configs.printing

import pl.ziemniakos.bearger_xml.configs.NewLineCharacter

class PrintingConfig(
	val indentSize: Int = 4,
	val useTabs: Boolean = false,
	val newLineCharacter: NewLineCharacter = NewLineCharacter.NORMAL,
	val printingOrder: Map<String, List<String>> = mapOf(),
	val sortingOrder: Map<String, NodesPrintingOrderConfig> = mapOf(),
)
