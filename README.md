# polar-converter

A small Kotlin CLI tool that converts a Minecraft world folder (with at least a `/region` folder inside) into a `.polar` file.

It’s built with Gradle and uses:
- **[picocli](https://github.com/remkop/picocli)** for command-line parsing
- **[hollow-cube/polar](https://github.com/hollow-cube/polar)** for the Anvil -> Polar conversion
- **[Minestom](https://github.com/minestom/Minestom)** to make Polar conversion work

## Requirements

- **JDK 25**
- A world folder that contains at least a `region/` directory

## Releases

You can download the latest release from [here](https://github.com/awkkih/polar-converter/releases).

## Build

If you want to build the project, use the snippet below. This project produces a single runnable (fat) jar via the Shadow plugin.

```bash
./gradlew build
```

The jar will be in:

```bash
build/libs/polar-converter-1.0.0.jar
```

## Usage

Run the CLI from the built jar:

```bash
java -jar build/libs/polar-converter-1.0.0.jar \
  -i /path/to/world \
  -o /path/to/output \
  -fN myworld
```

This creates:

```text
/path/to/output/myworld.polar
```

### Options

- `-i`, `--input` (required): input world folder (must exist; should contain `region/`)
- `-o`, `--output` (required): output directory (must exist)
- `-fN`, `--fileName` (required): name of the output file (without extension)

Example:

```bash
java -jar build/libs/polar-converter-1.0.0.jar -i . -o ./out -fN converted
```

## Notes / Troubleshooting

- The output directory must already exist (the tool currently errors if it doesn’t).
- If you get toolchain/JDK errors, ensure JDK 25 is installed and Gradle can find it.
