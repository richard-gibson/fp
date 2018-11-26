package intro

import intro.Recursion.romanNumerals
import intro.Recursion.sumRec
import intro.Recursion.sumTailRec
import java.time.OffsetDateTime
import java.time.ZoneOffset

object Recursion {

    //create a function that will produce a string concatenated N times
    tailrec fun concatN(n: Int, str: String): String =
            if (n == 0) str
            else concatN(n - 1, str + str)

    //create a function that find the sum of all Ints in a list recursively
    fun sumRec(l: List<Int>): Int =
            if (l.isEmpty()) 0
            else l.first() + sumRec(l.drop(1))

    //create a function that find the sum of all Ints in a list using tail recursively
    fun sumTailRec(l: List<Int>): Int {
        tailrec fun _accumulate(_l: List<Int>, acc: Int = 0): Int =
                if (_l.isEmpty()) acc
                else _accumulate(l.drop(1), acc + l.first())

        return _accumulate(l)
    }

    //create a functin that find the product of all Ints in a list using tail recursively
    fun productRec(l: List<Int>): Int {
        tailrec fun _accumulate(_l: List<Int>, acc: Int = 1): Int =
                if (_l.isEmpty()) acc
                else _accumulate(l.drop(1), acc * l.first())

        return _accumulate(l)
    }

    //create a function that can calulate the sum or product of a list
    fun <A> HOFApplyRec(l: List<A>, zero: A, f:(A, A) -> A): A {
        tailrec fun _accumulate(_l: List<A>, acc: A = zero): A =
                if (_l.isEmpty()) acc
                else _accumulate(l.drop(1), f(acc, l.first()))

        return _accumulate(l)
    }

    //create a function that returns the factorial of a given number
    fun factorial(n: Int): Int {
        tailrec fun _fac(acc: Int, n: Int): Int =
            if (n == 0) acc
            else _fac(n * acc, n-1)
        return _fac(1, n)
    }

    //create a function that returns its roman numeral representation e.g. 1 -> I,  6 -> VI, 10 -> X
    fun romanNumerals(i: Int): String {
        val numerals = listOf("M" to 1000, "D" to 500, "C" to 100, "L" to 50, "X" to 10, "V" to 5, "I" to 1)
        tailrec fun _romanNumerals(number: Int, numerals: List<Pair<String, Int>>, numAcc: String = ""): String =
                if (numerals.isEmpty()) numAcc
                else _romanNumerals(number = number % numerals[0].second,
                        numerals = numerals.drop(1),
                        numAcc = numAcc + numerals[0].first.repeat(number / numerals[0].second))
        return _romanNumerals(i, numerals)
    }

}
