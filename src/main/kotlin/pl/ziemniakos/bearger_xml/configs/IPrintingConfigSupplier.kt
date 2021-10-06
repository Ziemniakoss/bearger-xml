package pl.ziemniakos.bearger_xml.configs

interface IPrintingConfigSupplier {
	fun getPrintingConfig(topNodeName: String): PrintingConfig
}
