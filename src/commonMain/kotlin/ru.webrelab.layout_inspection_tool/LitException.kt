package ru.webrelab.layout_inspection_tool

class LitException: RuntimeException {
    constructor(message: String): super(message)
    constructor(throwable: Throwable): super(throwable)
}