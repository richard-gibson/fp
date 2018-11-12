package prog

import java.util.*

object Program {

    @JvmStatic
    fun main(args: Array<String>) {
        val random = Random()
        val list = LinkedList<String>()
        var exec = true
        while (exec) {
            printMenu()

            when(readLine()?.toLowerCase()) {
                "Add" -> {
                    println("Enter a string to Add")
                    val item = readLine()?: ""
                    list.add(item)
                }
                "Get" -> {
                    println("Enter an index")
                    val index = readLine()?.toInt()?: -1
                    println("item at $index is ${list[index]}")
                }
                "Random" -> {
                    val index = random.nextInt(list.size)
                    println("Random item at $index is ${list[index]}")
                }
                "Remove" -> {
                    println("Enter an index")
                    val index = readLine()?.toInt()?: -1
                    println("Item ${list.removeAt(index)} removed from position $index")
                }
                "Print" -> {
                    println("Contents of list are: ${list.map { "\n\t $it" }}")
                }
                "Quit" -> {
                    println("Thanks for playing!")
                    exec = false
                }

            }
        }
    }

    private fun printMenu() {
        println("Choose an option:")
        println("\t [Add] a string to the list")
        println("\t [Get] a string at a specified position")
        println("\t [Random] a string at a Random position")
        println("\t [Remove] a string from a specified position")
        println("\t [Print] the current contents of the list")
        println("\t [Quit] the program")
        println()
    }
}