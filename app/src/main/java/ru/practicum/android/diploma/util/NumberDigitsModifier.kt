package ru.practicum.android.diploma.util

fun modifyNumberWithDigits(
    number: Int?,
    separator: String = " "
): String {
    if (number == null) return ""
    if (number.toString().length < 4) return number.toString()

    val digitSize = 3

    var sample = number.toString()
    val digitsCount: Int = (sample.length -1)/digitSize

    var result = sample.dropLast(digitsCount * digitSize)
    sample = sample.drop(result.length)

    for (i in 1 .. digitsCount) {
        result = result + " " + sample.take(digitSize)
        sample = sample.drop(digitSize)
    }

    return result
}