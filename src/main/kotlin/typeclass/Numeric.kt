package typeclass

import arrow.extension

interface Numeric<T> {
    fun plus(x: T, y: T): T
    fun minus(x: T, y: T): T
    fun times(x: T, y: T): T
    fun negate(x: T): T
    fun fromInt(x: Int): T
    fun toInt(x: T): Int
    fun toLong(x: T): Long
    fun toFloat(x: T): Float
    fun toDouble(x: T): Double

    fun zero() = fromInt(0)
    fun one() = fromInt(1)

}

