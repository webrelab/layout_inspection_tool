package ru.webrelab.layout_inspection_tool.enums

import ru.webrelab.layout_inspection_tool.ifaces.IScreenSize

enum class BrowserScreenSize(
    override val width: Int,
    override val height: Int
) : IScreenSize {
    LARGE_2K(2500, 1100),
    FULL_HD(1900, 950),
    HD_READY(1670, 780),
    TABLET_LANDSCAPE(1000, 640),
    TABLET_PORTRAIT(760, 900)
}