package intro

import arrow.core.compose
import arrow.instances.list.foldable.foldLeft
import collections.andThen


object util {
    infix fun <A, B, C> ((B) -> C).compose(f: (A) -> B): (A) -> C =
            { a -> this(f(a)) }

    infix fun <A, B, C> ((A) -> B).andThen(f: (B) -> C): (A) -> C =
            { a -> f(this(a)) }
}

object Functions {

    //Higher order funcions

    val listInts : List<Int> = listOf(1,2,3,4,5,6).map{x ->  x + 1}

    //increment each element in the list using Lists map function


    val incrementBy1: (Int) -> Int = {x -> x + 1}
    val decrementBy1: (Int) -> Int = {it + 1}

    val plus: (Int) -> (Int) -> Int = { x -> { y -> x + y } }
    val multiply: (Int) -> (Int) -> Int = { x -> { y -> x * y } }
    val divide: (Int) -> (Int) -> Int = { x -> { y -> y / x } }
    val subtract: (Int) -> (Int) -> Int = { x -> { y -> y - x } }

    val pi = Math.PI
    // use multiply to give the answer for 4 - 2
    val twoTimesFour = subtract(4)(2)

    //use divide to give answer for 8 /4
    val eightByFour = divide(8)(4)

    //create a new function from plus that will increment by 2
    val incrementBy2 = plus(2)

    //create a new function from plus that will times by 4
    val decrementBy3: (Int) -> Int = subtract(3)


    //create a function machine that will take any number,
    // increment by 2 then times by 4

    val times4 = multiply(4)
    val plus2 = plus(2)

    val functionMachine = plus2 andThen times4

    object Circle {

        //create a function that calculates the area of a circle given its radius
        val plus: (Double) -> (Double) -> Double = { x -> { y -> x + y } }
        val multiply: (Double) -> (Double) -> Double = { x -> { y -> x * y } }


        val byPi = multiply(Math.PI)
        val sqrd: (Double) -> Double = {x -> multiply(x)(x)}

        val circleArea  = sqrd compose byPi
        val n = circleArea(2.0)

    }

    val vowels = setOf('a', 'e', 'i', 'o', 'u')
    val filterVowels: (String) -> String = {word ->
        word.filter {char -> vowels.contains(char) }
    }
    val numberOfVowels: (String) -> Int = filterVowels andThen {it.length}



    //Create a calculator that will carry out a set list of calculations on any int

    val commands: List<(Int) -> Int> = listOf(incrementBy2, times4, decrementBy3)



    val calculator: (List<(Int) -> Int>) -> (Int) -> Int = { cmds ->
        { x ->
            cmds.foldLeft(x) { acc, elem ->
                elem(acc)
            }
        }
    }

    val myCalculator = calculator(commands)



}


