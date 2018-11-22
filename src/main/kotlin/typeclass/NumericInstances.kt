package typeclass

import arrow.extension

@extension interface IntNumeric: Numeric<Int> {
    override fun plus(x: Int, y: Int): Int = x + y
    override fun minus(x: Int, y: Int): Int = x - y
    override fun times(x: Int, y: Int): Int = x * y
    override fun negate(x: Int): Int = -x
    override fun fromInt(x: Int): Int = x
    override fun toInt(x: Int): Int = x
    override fun toLong(x: Int): Long = x.toLong()
    override fun toFloat(x: Int): Float = x.toFloat()
    override fun toDouble(x: Int): Double = x.toDouble()
}


