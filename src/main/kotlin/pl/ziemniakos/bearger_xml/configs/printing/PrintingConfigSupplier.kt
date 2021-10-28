package pl.ziemniakos.bearger_xml.configs.printing

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import java.nio.file.Paths
import kotlin.io.path.exists

class PrintingConfigSupplier : IPrintingConfigSupplier {
	private val cache = mutableMapOf<String, PrintingConfig>()
	private val mapper = ObjectMapper().registerModule(KotlinModule())

	override fun getPrintingConfig(topNodeName: String): PrintingConfig {
		if(cache.containsKey(topNodeName)) {
			return cache[topNodeName]!!
		}
		val pathWithConfig = Paths.get(System.getProperty("user.home"), ".config", "bearger-xml", "printing", "$topNodeName.json")
		if(pathWithConfig.exists()) {
			return mapper.readValue(pathWithConfig.toFile())
		}
		return PrintingConfig()
	}
}