package typeclasses

interface Numeric<T> {
    fun T.plus(y: T): T
    fun T.minus(y: T): T
    fun T.times(y: T): T
    fun T.negate(): T
    fun fromInt(x: Int): T
    fun T.toInt(): Int
    fun T.toLong(): Long
    fun T.toFloat(): Float
    fun T.toDouble(): Double

    fun zero() = fromInt(0)
    fun one() = fromInt(1)

}

