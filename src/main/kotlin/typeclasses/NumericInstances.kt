package numeric.instances

import typeclasses.Numeric

fun Int.Companion.numeric() = object : Numeric<Int> {
    override fun Int.plus(y: Int): Int = this + y

    override fun Int.minus(y: Int): Int = this - y

    override fun Int.times(y: Int): Int = this * y

    override fun Int.negate(): Int = -this

    override fun fromInt(x: Int): Int = x

    override fun Int.toInt(): Int = this

    override fun Int.toLong(): Long = this.toLong()

    override fun Int.toFloat(): Float = this.toFloat()

    override fun Int.toDouble(): Double = this.toDouble()

}

fun Double.Companion.numeric() = object : Numeric<Double> {
    override fun Double.plus(y: Double): Double = this + y

    override fun Double.minus(y: Double): Double = this - y

    override fun Double.times(y: Double): Double = this * y

    override fun Double.negate(): Double = -this

    override fun fromInt(x: Int): Double = x.toDouble()

    override fun Double.toInt(): Int = this.toInt()

    override fun Double.toLong(): Long = this.toLong()

    override fun Double.toFloat(): Float = this.toFloat()

    override fun Double.toDouble(): Double = this.toDouble()

}

data class Money(val value: Double) {
    companion object
}


fun Money.Companion.numeric() = object : Numeric<Money> {
    override fun Money.plus(y: Money): Money = Money(this.value + y.value)
    override fun Money.minus(y: Money): Money = Money(this.value - y.value)
    override fun Money.times(y: Money): Money = Money(this.value * y.value)
    override fun Money.negate(): Money = Money(-this.value)
    override fun fromInt(x: Int): Money = Money(x.toDouble())

    override fun Money.toInt(): Int = this.value.toInt()

    override fun Money.toLong(): Long = this.value.toLong()

    override fun Money.toFloat(): Float = this.value.toFloat()

    override fun Money.toDouble(): Double = this.value.toDouble()
}


class Calculator<T>(N: Numeric<T>) : Numeric<T> by N {
    fun addInts(i: Int, j: Int) : T =
            fromInt(i).plus(fromInt(j))

    fun add(i: T, j: T) : T =
            i.plus(j)

    fun takeAway(i: T, j: T) : T =
            i.minus(j)

    fun sum(l: List<T>) =
            l.fold(zero()) { acc, elem ->
                acc.plus(elem)
            }
}


fun main(args: Array<String>) {
    val intCalc = Calculator(Int.numeric())
    val dblCalc = Calculator(Double.numeric())
    val moneyCalc = Calculator(Money.numeric())


    println(moneyCalc.sum(
            listOf(Money(1.40),
                    Money(6.00),
                    Money(10.00))))


    println(dblCalc.sum(listOf(1.40, 6.00, 10.00)))
    println(intCalc.sum(listOf(1, 6, 10)))

}