package pl.ziemniakos.bearger_xml

import pl.ziemniakos.bearger_xml.configs.NodesPrintingOrderConfig

class XmlNodeComparator(private val nodesPrintingOrderConfig: NodesPrintingOrderConfig) : Comparator<XmlNode>{
	override fun compare(first: XmlNode?, second: XmlNode?): Int {
		if(first == null && second == null) {
			return 0
		}
		if(first == null) {
			return -1
		} else if(second == null) {
			return 1;
		}
		for(field in nodesPrintingOrderConfig.sortByFields) {
			var valueInFirst = first.properties[field]
			var valueInSecond = second.properties[field]
			if(!nodesPrintingOrderConfig.caseSensitive) {
				valueInFirst = valueInFirst?.lowercase()
				valueInSecond = valueInSecond?.lowercase()
			}
			if(valueInFirst == null) {
				return -1
			}
			if(valueInSecond == null) {
				return 1
			}
			val c = valueInFirst.compareTo(valueInSecond)
			if(c != 0) {
				return c
			}
		}
		return 0
	}
}