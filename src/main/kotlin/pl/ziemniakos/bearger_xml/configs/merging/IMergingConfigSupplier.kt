package pl.ziemniakos.bearger_xml.configs.merging

interface IMergingConfigSupplier {
	/**
	 * Returns merging config for specified tag name.
	 * If merging config does not exist, this will return empty Merging Config.
	 * Merging configs are fetched from directory
	 * ```
	 * ~/.config/bearger-xml/merging/topNodeName.json
	 * ```
	 * More info in README
	 *
	 * @return config specified for this top node name in configs file or empty MergingConfig
	 */
	fun getMergingConfig(topNodeName: String): MergingConfig
}