package pl.ziemniakos.bearger_xml

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import kotlinx.cli.vararg
import pl.ziemniakos.bearger_xml.sorting.Sorter

enum class Mode {
	BEARGER,
	SORTER,
	DIFFER
}

class ArgumentParser : IArgumentsParser {
	override fun parseArguments(args: Array<String>): IProgram {
		val parser = ArgParser("bearger-xml")
		val originalBranchName by parser.option(
			ArgType.String,
			fullName = "original-branch",
			shortName = "o",
			description = "Branch(or commit hash) from which we should fetch source version"
		)
		val mergedBranchName by parser.option(
			ArgType.String,
			fullName = "merged-branch",
			shortName = "m",
			description = "Merged branch(or commit hash)"
		)

		val useOriginalFile by parser.option(
			ArgType.Boolean,
			fullName = "use-original-file",
			description = """Use same file as output. If set to false, it will append ".bearged""""
		).default(false)
		val fileNames by parser.argument(
			ArgType.String,
			"fileNames",
			description = "Files to process. In Differ mode you have to specify 3 arguments: first and second are files ou want to compare and third is output file"
		).vararg()

		val mode by parser.option(
			ArgType.Choice<Mode>(),
			fullName = "mode",
			description = "Mode for berager"
		).default(Mode.BEARGER)

		parser.parse(args)
		return when (mode) {
			Mode.BEARGER -> {
				TODO("\uD83D\uDC3Bging is not implemented yet, please use differ mode (--mode differ), for more help please use --help")
				if (originalBranchName == null) {
					println("Please specify original branch name")
					throw IllegalArgumentException("Please specify original branch name (use -h or --help to get help)")
				} else if (mergedBranchName == null) {
					throw IllegalArgumentException("Please specify merged branch name (use -h or --help to get help)")
				}
				Bearger(fileNames.toSet(), originalBranchName!!, mergedBranchName!!, useOriginalFile)
			}
			Mode.SORTER -> Sorter(fileNames.toSet(), useOriginalFile)
			Mode.DIFFER -> {
				if(fileNames.size != 3) {
					throw IllegalArgumentException("Please specify only 3 arguments for differ mode(file1 file2 and output file)")
				}

				Differ(file1 = fileNames[0], file2 = fileNames[1], outputFile = fileNames[2])
			}
		}
	}
}
