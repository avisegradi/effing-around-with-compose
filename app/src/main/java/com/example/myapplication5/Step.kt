package com.example.myapplication5


val String.hello: String
    get() = "«${this}» Ooøof hello"

val String.bye: String
    get() = "#${this.hashCode().toString(16)}"

class Step(private val data: String) {
    val title = data
        get() = field.hello
    val description = data
        get() = field.bye
}
