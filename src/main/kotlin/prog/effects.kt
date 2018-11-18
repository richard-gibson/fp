package prog.effects

import arrow.core.Try
import arrow.effects.IO
import arrow.effects.fix
import arrow.effects.instances.io.monad.monad
import java.util.*


sealed class Command {
    object Add: Command()
    object Get: Command()
    object Random: Command()
    object Print: Command()
    object Remove: Command()
    object Quit: Command()
    object Unkown: Command()
    companion object {
       operator fun invoke(cmd: String) = when(cmd.trim().toLowerCase()) {
           "add"    -> Add
           "get"    -> Get
           "random" -> Random
           "print"  -> Print
           "quit"   -> Quit
           else     -> Unkown
       }
    }
}
object effects {
    fun putStrLn(s: String): IO<Unit> = IO { println(s) }

    fun getStrLn(): IO<String> = IO { readLine() ?: "" }

    fun parseInt(s: String): Try<Int> = Try { s.toInt() }


    fun printMenu(): IO<Unit>  = IO.monad().binding {
        putStrLn("Choose an option:").bind()
        putStrLn("\t [Add] a string to the list").bind()
        putStrLn("\t [Get] a string at a specified position").bind()
        putStrLn("\t [Random] a string at a Random position").bind()
        putStrLn("\t [Remove] a string from a specified position").bind()
        putStrLn("\t [Print] the current contents of the list").bind()
        putStrLn("\t [Quit] the program").bind()
        putStrLn("").bind()
    }.fix()

    object random: Random()

    fun nextInt(upper: Int): IO<Int> = IO {random.nextInt(upper)}


    fun commandLoop(state: List<String>): IO<Unit> = IO.monad().binding {
        printMenu().bind()
        val choice = getStrLn().bind()
        when(Command(choice)) {
            Command.Add -> {
                putStrLn("Enter a string to Add").bind()
                val item = getStrLn().bind()
                commandLoop(state + item)
            }
            Command.Get -> {
                putStrLn("Enter an index").bind()
                val index = getStrLn().bind()
                parseInt(index).fold({ putStrLn("$index is not a number")}){
                    putStrLn("item at $index is ${state[it]}")
                }.bind()
                commandLoop(state)
            }
            Command.Random -> {
                val rndIdx = nextInt(state.size).bind()
                putStrLn("Random item at $rndIdx is ${state[rndIdx]}").bind()
                commandLoop(state)
            }
            Command.Print -> {
                putStrLn("Contents of list are: ${state.map { "\n\t $it" }}").bind()
                commandLoop(state)
            }
            Command.Remove -> {
                putStrLn("Enter an index").bind()
                val index = getStrLn().bind()
                val idx = parseInt(index)
                            .fold({ IO.raiseError<Int>(it) }) { IO.just(it) }.bind()

                val bfrIdx = state.filterIndexed { i, _ -> i < idx }
                val aftIdx = state.filterIndexed { i, _ -> i > idx }

                putStrLn("Item ${state[idx]} removed from position $idx").bind()
                commandLoop(bfrIdx + aftIdx)
            }
            Command.Quit -> {
                putStrLn("Thanks for playing!")
                IO.unit
            }
            Command.Unkown -> {
               putStrLn("Unknown command").bind()
               commandLoop(state)
            }

        }.bind()
    }.fix()


    fun ioMain(): IO<Unit> = IO.monad().binding {
        commandLoop(emptyList()).bind()
    }.fix()

}

fun main(args: Array<String>) {
    effects.ioMain().unsafeRunSync()
}