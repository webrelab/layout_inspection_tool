package ru.webrelab.layout_inspection_tool

import ru.webrelab.layout_inspection_tool.ifaces.IConfiguration
import ru.webrelab.layout_inspection_tool.ifaces.ILayoutInspectionBehavior
import ru.webrelab.layout_inspection_tool.ifaces.IMeasuringType
import ru.webrelab.layout_inspection_tool.ifaces.IScreenSize

object LitConfig {
    private var c: IConfiguration<Any> = object : IConfiguration<Any> {
        override val measuringTypes: List<IMeasuringType>
            get() = throw RuntimeException("Configuration does not set")
        override val screenSizes: List<IScreenSize>
            get() = throw RuntimeException("Configuration does not set")
        override val behavior: ILayoutInspectionBehavior<Any>
            get() = throw RuntimeException("Configuration does not set")
        override val violation: Int
            get() = 5
        override val pathToDataset: String
            get() = throw RuntimeException("Configuration does not set")

    }
    @Suppress("UNCHECKED_CAST")
    fun <E> set(c: IConfiguration<E>) {this.c = c as IConfiguration<Any>}
    @Suppress("UNCHECKED_CAST")
    fun <E> config() = c as IConfiguration<E>
}