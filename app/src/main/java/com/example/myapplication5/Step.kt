package com.example.myapplication5


val String.hello: String
    get() = "«${this}» Ooøof hello"

val String.bye: String
    get() = "#${this.hashCode().toString(16)}"

class Step(data: String) {
    val title = data
        get() = field.hello
    val description = data
        get() = field.bye
    val detailsPart1: String
        get() = "Deeeetails on ${title.reversed()}"
    val detailsPart2: String
        get() = ".... Extra stuff on ${description}"
}
