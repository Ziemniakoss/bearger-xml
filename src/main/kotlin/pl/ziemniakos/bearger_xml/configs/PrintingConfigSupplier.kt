package pl.ziemniakos.bearger_xml.configs

class PrintingConfigSupplier : IPrintingConfigSupplier {
	override fun getPrintingConfig(topNodeName: String): PrintingConfig {
		//TODO read actual config files
		return PrintingConfig()
	}
}