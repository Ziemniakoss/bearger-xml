package pl.ziemniakos.bearger_xml.configs.merging

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import java.nio.file.Paths
import kotlin.io.path.notExists

class MergingConfigSupplier : IMergingConfigSupplier {
	private val mapper = ObjectMapper().registerModule(KotlinModule())
	private val cache = mutableMapOf<String, MergingConfig>()

	override fun getMergingConfig(topNodeName: String): MergingConfig {
		if(cache.containsKey(topNodeName)) {
			return cache[topNodeName]!!
		}
		val pathWithConfig = Paths.get(System.getProperty("user.home"), ".config", "bearger-xml", "merging", "$topNodeName.json")
		if(pathWithConfig.notExists()) {
			return MergingConfig(mapOf())
		}
		return mapper.readValue(pathWithConfig.toFile())
	}
}
