package pl.ziemniakos.bearger_xml

class Bearger(
	private val fileNames: Set<String>,
	private val originalBranchName: String,
	private val mergedBranchName: String,
	private val useOriginalFile: Boolean
	) : IProgram {
	override fun run() {
	}
}
