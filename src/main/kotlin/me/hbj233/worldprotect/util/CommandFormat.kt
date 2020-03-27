package me.hbj233.worldprotect.util

fun String.commandFormat(): String {
    val regex = """\s+(\s?+\s?)(\s+|\s?${'$'})""".toRegex()
    return regex.replace(this,"""$1""")
}