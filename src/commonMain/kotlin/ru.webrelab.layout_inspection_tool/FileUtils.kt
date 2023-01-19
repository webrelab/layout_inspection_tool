package ru.webrelab.layout_inspection_tool

import okio.Path
import ru.webrelab.layout_inspection_tool.enums.Environment
import ru.webrelab.layout_inspection_tool.ifaces.IScreenSize

expect fun getPath(
    fileName: String,
    fileExtension: String,
    environment: Environment,
    screenSize: IScreenSize,
    vararg pathToFile: String
): Path
expect fun isFileExist(path: Path): Boolean
expect fun readFile(path: Path): String
expect fun writeData(path: Path, data: String)