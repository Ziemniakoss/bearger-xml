import pl.ziemniakos.bearger_xml.Bearger

fun main(args: Array<String>) {

	val set1 = listOf("Test", "ala","lota")
	val set2 = listOf("Test", "ala", "lota")
	println(set1 == set2)
	Bearger(args).run()
}