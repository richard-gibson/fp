package intro

import intro.Recursion.sumRec
import intro.Recursion.sumTailRec
import java.time.OffsetDateTime
import java.time.ZoneOffset

object Recursion {

    /**
     * complete function sum to total
     */
    fun sumRec(l:List<Int>): Int =
        if(l.isEmpty())  0
        else l.first() + sumRec(l.drop(1))


    fun sumTailRec(l:List<Int>): Int {
        tailrec fun _accumulate(_l: List<Int>, acc: Int = 0): Int =
        if(_l.isEmpty())  acc
        else _accumulate(l.drop(1), acc + l.first())

        return _accumulate(l)
    }

    fun productRec(l:List<Int>): Int {
        tailrec fun _accumulate(_l: List<Int>, acc: Int = 0): Int =
                if(_l.isEmpty())  acc
                else _accumulate(l.drop(1), acc * l.first())

        return _accumulate(l)
    }


//    fun accumulate

    val numerals = listOf("M" to 1000, "D" to 500, "C" to 100, "L" to 50, "X" to 10, "V" to 5, "I" to 1)
    tailrec fun toRomanTailRec(number: Int, numerals: List<Pair<String, Int>>, numAcc: String = ""): String =
            if (numerals.isEmpty()) numAcc
            else toRomanTailRec(number = number % numerals[0].second,
                    numerals = numerals.drop(1),
                    numAcc = numAcc + numerals[0].first.repeat(number / numerals[0].second))




}
fun Int.asRomanNum() = Recursion.toRomanTailRec(this, Recursion.numerals)
fun main(args: Array<String>) {
    println(sumRec(listOf(1,2,3,4)))
//    println(sumTailRec(listOf(1,2,3,4)))

    println(50.asRomanNum())
    println(553.asRomanNum())
    println(2553.asRomanNum())
}