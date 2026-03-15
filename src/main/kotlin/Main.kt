package dev.akkih

import net.hollowcube.polar.AnvilPolar
import net.hollowcube.polar.PolarWriter
import net.minestom.server.MinecraftServer
import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import java.io.File
import java.nio.file.Files
import java.util.concurrent.Callable
import kotlin.system.exitProcess

@Command(name = "polarconvert", aliases = ["polarc", "polar"], description = ["Converts a world folder into a polar file."])
class PolarConverter : Callable<Int> {
    @Option(names = ["-i", "--input"], required = true, description = ["Select the input folder you want to convert into a polar file. Remember that the folder needs to contain AT LEAST a /region folder."])
    lateinit var input: File

    @Option(names = ["-o", "--output"], required = true, description = ["Select the output folder you want the converted file to be."])
    lateinit var output: File

    @Option(names = ["-fN", "--fileName"], required = true, description = ["Select the name of the converted file."])
    lateinit var fileName: String

    override fun call(): Int {
        val inputPath = input.toPath()
        val outputPath = output.toPath()

        if (!input.exists()) {
            error("The specified folder input does not exist.")
        }

        if (!Files.exists(inputPath)) {
            error("The specified folder input is not a folder.")
        }

        if (!Files.isReadable(inputPath)) {
            error("The specified folder input is not readable.")
        }

        if (!output.exists()) {
            error("The specified folder output does not exist.")
        }

        if (!Files.isDirectory(outputPath)) {
            error("The specified folder output is not a folder.")
        }

        MinecraftServer.init()

        val bytes = AnvilPolar.anvilToPolar(inputPath)
        val world = PolarWriter.write(bytes)
        Files.write(outputPath.resolve("$fileName.polar"), world)

        MinecraftServer.process().stop()

        println("Successfully created ${fileName}.polar in ${outputPath.toAbsolutePath()}!")
        return 0
    }
}

fun main(args: Array<String>): Unit = exitProcess(CommandLine(PolarConverter()).execute(*args))