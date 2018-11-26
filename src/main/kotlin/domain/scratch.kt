package domain

import arrow.core.Either
import arrow.core.Option
import arrow.core.Try
import arrow.effects.DeferredK
import arrow.effects.deferredk.monadError.monadError
import arrow.effects.fix
import arrow.effects.unsafeAttemptSync
import arrow.instances.`try`.monadError.monadError
import arrow.instances.either.monadError.monadError
import arrow.instances.option.monadError.monadError
import domain.validation.empEitherFromApp
import domain.validation.empEitherFromMonad
import domain.validation.empValidatedFromApp


object OptFlightOps {
    @JvmStatic
    fun main(args: Array<String>) {
        println()
        println(OptFlights.userFlights("bob"))
        println(OptFlights.userFlights("Brian"))

        println()
        println(OptFlights.userFlightsFlatMap("bob"))
        println(OptFlights.userFlightsFlatMap("Brian"))
    }
}

object TryFlightOps {
    @JvmStatic
    fun main(args: Array<String>) {
        println()
        println(TryFlights.userFlights("bob"))
        println(TryFlights.userFlights("Brian"))

        println()
        println(TryFlights.userFlightsFlatMap("bob"))
        println(TryFlights.userFlightsFlatMap("Brian"))
    }
}

object EitherFlightOps {
    @JvmStatic
    fun main(args: Array<String>) {
        println()
        println(EithFlights.userFlights("bob"))
        println(EithFlights.userFlights("Brian"))

        println()
        println(EithFlights.userFlightsFlatMap("bob"))
        println(EithFlights.userFlightsFlatMap("Brian"))
    }
}


object Validation {

    @JvmStatic
    fun main(a: Array<String>) {
        println(empEitherFromApp("foo", "00111", "Dublin", 15))
        println(empEitherFromApp("", "00", "Washington", 17500))
        println(empEitherFromApp("foo", "00", "Washington", 17500))
        println()

        println()
        println(empEitherFromMonad("foo", "00111", "Dublin", 15))
        println(empEitherFromMonad("", "00", "Washington", 17500))
        println(empEitherFromMonad("foo", "00", "Washington", 17500))
        println()

        println()
        println(empValidatedFromApp("foo", "00111", "Dublin", 15))
        println(empValidatedFromApp("", "00", "Washington", 17500))
        println(empValidatedFromApp("foo", "00", "Washington", 17500))
    }

}

object MEFlightOps {
    @JvmStatic
    fun main(args: Array<String>) {
        val tryModule = MEFlights(Try.monadError(), OptionToTry )
        val optModule = MEFlights(Option.monadError(), OptionIdentity )
        val eithModule = MEFlights(Either.monadError<Failure>(), OptionToEither)
        val deferredKModule = MEFlights(DeferredK.monadError(), OptionToDeferredK)

        println(tryModule.userFlights("bob"))
        println(optModule.userFlights("bob"))
        println(eithModule.userFlights("bob"))
        println(deferredKModule.userFlights("bob").fix().unsafeAttemptSync())

        println(tryModule.userFlights("Brian"))
        println(optModule.userFlights("Brian"))
        println(eithModule.userFlights("Brian"))
        println(deferredKModule.userFlights("Brian").fix().unsafeAttemptSync())

    }
}