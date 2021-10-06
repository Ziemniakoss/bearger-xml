package pl.ziemniakos.bearger_xml

class Bearger(private val args: Array<String>) {
	fun run() {
		for(fileName in args) {
			println(XmlNode.fromFile(fileName))
		}
	}
}
