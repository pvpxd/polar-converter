package dev.akkih

import net.hollowcube.polar.AnvilPolar
import net.hollowcube.polar.PolarWriter
import net.kyori.adventure.key.Key
import net.minestom.server.MinecraftServer
import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.Callable
import kotlin.system.exitProcess

@Command(name = "polarconverter", aliases = ["polarc", "polar"], description = ["Converts a world folder into a polar file."])
class PolarConverter : Callable<Int> {
    @Option(names = ["-i", "--input"], required = true, description = ["Select the input folder you want to convert into a polar file. Accepts both the pre-26.1 layout (region/ at the root) and the new layout (dimensions/<namespace>/<dimension>/region)."])
    lateinit var input: File

    @Option(names = ["-o", "--output"], required = true, description = ["Select the output folder you want the converted file to be."])
    lateinit var output: File

    @Option(names = ["-fN", "--fileName"], required = true, description = ["Select the name of the converted file."])
    lateinit var fileName: String

    @Option(names = ["-d", "--dimension"], description = ["Select which dimension to convert when the world uses the new (26.1+) layout, e.g. minecraft:overworld, minecraft:the_nether, minecraft:the_end. Ignored for the pre-26.1 layout. Defaults to \${DEFAULT-VALUE}."])
    var dimension: String = "minecraft:overworld"

    override fun call(): Int {
        val inputPath = input.toPath()
        val outputPath = output.toPath()

        if (!input.exists()) {
            error("The specified folder input does not exist.")
        }

        if (!Files.isDirectory(inputPath)) {
            error("The specified folder input is not a folder.")
        }

        if (!output.exists()) {
            error("The specified folder output does not exist.")
        }

        if (!Files.isDirectory(outputPath)) {
            error("The specified folder output is not a folder.")
        }

        val worldPath = resolveWorldPath(inputPath)

        MinecraftServer.init()

        val world = AnvilPolar.anvilToPolar(worldPath)
        val bytes = PolarWriter.write(world)
        Files.write(outputPath.resolve("$fileName.polar"), bytes)

        MinecraftServer.process().stop()

        println("Successfully created ${fileName}.polar in ${outputPath.toAbsolutePath()}!")
        return 0
    }

    private fun resolveWorldPath(root: Path): Path {
        if (Files.isDirectory(root.resolve("region"))) {
            return root
        }

        val key = Key.key(dimension)
        val dimensionPath = root.resolve("dimensions").resolve(key.namespace()).resolve(key.value())
        if (Files.isDirectory(dimensionPath.resolve("region"))) {
            return dimensionPath
        }

        error("Could not find a region folder in '$root'. Checked '$root/region' (pre-26.1 layout) and '$dimensionPath/region' (26.1+ layout).")
    }
}

fun main(args: Array<String>): Unit = exitProcess(CommandLine(PolarConverter()).execute(*args))