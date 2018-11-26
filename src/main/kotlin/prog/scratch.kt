package prog

import arrow.effects.IO
import arrow.effects.fix
import arrow.effects.instances.io.monadDefer.monadDefer
import prog.tagless.ConsoleInstance
import prog.tagless.FRandomInstance
import prog.tagless.MonadConsoleRandom
import prog.tagless.tagless.fMain


fun main(args: Array<String>) {
    val effectModule = IO.monadDefer()
    val mcr = MonadConsoleRandom(effectModule,
            ConsoleInstance(effectModule),
            FRandomInstance(effectModule))
    val prog = mcr.fMain().fix()
    prog.unsafeRunSync()
}