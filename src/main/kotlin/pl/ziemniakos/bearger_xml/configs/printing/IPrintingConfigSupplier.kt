package pl.ziemniakos.bearger_xml.configs.printing

interface IPrintingConfigSupplier {
	fun getPrintingConfig(topNodeName: String): PrintingConfig
}
