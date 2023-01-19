package ru.webrelab.layout_inspection_tool

import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import ru.webrelab.layout_inspection_tool.enums.Environment
import ru.webrelab.layout_inspection_tool.ifaces.IScreenSize

actual fun getPath(
    fileName: String,
    fileExtension: String,
    environment: Environment,
    screenSize: IScreenSize,
    vararg pathToFile: String
): Path {
    var path = Executor.config.pathToDataset.toPath()
        .resolve(environment.toString())
        .resolve(screenSize.toString())
    pathToFile.forEach { path = path.resolve(it) }
    return path.resolve("${fileName}.{$fileExtension}")
}

actual fun isFileExist(path: Path): Boolean = FileSystem.SYSTEM.exists(path)

actual fun readFile(path: Path): String {
    val data = mutableListOf<String>()
    FileSystem.SYSTEM.read(path) {
        while (true) data.add(readUtf8Line() ?: break)
    }
    return data.joinToString("\n")
}

actual fun writeData(path: Path, data: String) {
    FileSystem.SYSTEM.write(path) {
        writeUtf8(data)
    }
}