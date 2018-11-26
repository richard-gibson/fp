package intro



infix fun <A, B, C> ((B) -> C).compose(f: (A) -> B): (A) -> C =
        { a -> this(f(a)) }

infix fun <A, B, C> ((A) -> B).andThen(f: (B) -> C): (A) -> C =
        { a -> f(this(a)) }


object Functions {



    //create a function that takes an int and incrememnts by 1
    val incrementBy1: (Int) -> Int = {x -> x + 1}

    //create a function that takes an int and decrements by 1
    val decrementBy1: (Int) -> Int = {it + 1}

    //create a function that takes 2 ints and produces the sum
    val plus: (Int, Int) -> Int = { x, y -> x + y }

    /**
     * create a function that takes an a int and returns a function that
     * will take another int and return the sum of the 2
     */
    val plusCurried: (Int) -> (Int) -> Int = { x -> { y -> x + y } }
    /**
     * create a function that takes an a int and returns a function that
     * will take another int and return the product of the 2
     */
    val multiply: (Int) -> (Int) -> Int = { x -> { y -> x * y } }
    /**
     * create a function that takes an a int and returns a function that
     * will take another int and return the first Int minus the second
     */
    val divide: (Int) -> (Int) -> Int = { x -> { y -> y / x } }
    /**
     * create a function that takes an a int and returns a function that
     * will take another int and return the first Int divided the second
     */
    val subtract: (Int) -> (Int) -> Int = { x -> { y -> y - x } }



    // use subtract to give the answer for 4 - 2
    val twoTimesFour = subtract(4)(2)

    //use divide to give answer for 8 /4
    val eightByFour = divide(8)(4)

    //create a new function from plus that will increment by 2
    val incrementBy2 = plusCurried(2)

    //create a new function from subtract that will decrement by 3
    val decrementBy3: (Int) -> Int = subtract(3)

    //create a new function from multiply that will times by 4
    val times4 = multiply(4)


    /**
     * create a function machine that will take any number,
     * increment by 2 then times by 4
     */
    val functionMachine = incrementBy2 andThen times4



    object Circle {

        //create a function that calculates the area of a circle given its radius

        val plus: (Double) -> (Double) -> Double = { x -> { y -> x + y } }
        val multiply: (Double) -> (Double) -> Double = { x -> { y -> x * y } }


        val byPi = multiply(Math.PI)
        val sqrd: (Double) -> Double = {x -> multiply(x)(x)}

        val circleArea  = sqrd compose byPi
        val n = circleArea(2.0)

    }


    //Create a function that returns the number of vowels in a given string

    val vowels = setOf('a', 'e', 'i', 'o', 'u')
    val filterVowels: (String) -> String = {word ->
        word.filter {char -> vowels.contains(char) }
    }


    val numberOfVowels: (String) -> Int = filterVowels andThen {it.length}



    //Create a calculator that will carry out a set list of calculations on any int
    val commands: List<(Int) -> Int> = listOf(incrementBy2, times4, decrementBy3)

    val calculator: (List<(Int) -> Int>) -> (Int) -> Int = { cmds ->
        { x ->
            cmds.fold(x) { acc, elem -> elem(acc) }
        }
    }

    val myCalculator = calculator(commands)



}




