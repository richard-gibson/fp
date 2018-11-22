package intro


fun fib() =
        generateSequence(0 to 1) { (a, b) -> a + b to a + b * 2 }
                .flatMap { (a, b) -> sequenceOf(a, b) }
//or

fun fib2() =
        generateSequence(0 to 1) { (a, b) -> b to a + b }
                .map { it.first }

/*fun main(args: Array<String>) {
    println(fib().take(20).toList())
    println(fib2().take(20).toList())
}*/

//[0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987, 1597, 2584, 4181]
//[0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987, 1597, 2584, 4181]


fun main(args: Array<String>) {
    invokeNTimes(2,::foo)
    printLine()
    invokeNTimes(3,::bar)
    printLine()
    invokeNTimes(4, {
        println("\t first lambda called with $it")
    })
    printLine()
    invokeNTimes(5) {
        println("\t second lambda called with $it")
    }
}
fun invokeNTimes(times: Int, func: (Int)->Unit) =
    (1..times).forEach(func)

fun foo(input: Int) = println("\t foo called with $input")
fun bar(input: Int) = println("\t bar called with $input")
fun printLine() = println("---------")