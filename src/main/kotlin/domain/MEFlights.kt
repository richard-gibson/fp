package domain

import arrow.Kind
import domain.repository.flightManafests
import domain.repository.flights
import domain.repository.users
import arrow.core.*
import arrow.data.*
import arrow.effects.DeferredK
import arrow.effects.ForDeferredK
import arrow.effects.deferredk.monadError.monadError
import arrow.effects.fix
import arrow.instances.`try`.monadError.monadError
import arrow.instances.either.monadError.monadError
import arrow.instances.option.monadError.monadError
import arrow.typeclasses.MonadError

class MEFlights<F, E>(val ME: MonadError<F, E>, val FunctK: FunctionK<ForOption, F>) :
        MonadError<F, E> by ME {

    fun <T> Nel<Kind<F, T>>.flip(): Kind<F, Nel<T>> =
            this.sequence(ME)

    fun userByName(name: String): Kind<F, User> =
            FunctK(users.firstOrNull { it.name == name }.toOption())

    fun manifestsContainingUser(user: User): Kind<F, Nel<FlightManafest>> =
            FunctK(Nel.fromList(flightManafests.filter { fm -> fm.passengers.contains(user.id) }))


    fun flightById(flightNo: Int): Kind<F, Flight> =
            FunctK(flights.firstOrNull { it.flightNo == flightNo }.toOption())

    fun flightsFromManifests(manifests: Nel<FlightManafest>): Kind<F, Nel<Flight>> =
            manifests.map { flightById(it.flightNo) }.flip()

    fun userFlights(name: String): Kind<F, Nel<Flight>> =
            binding {
                val user = userByName(name).bind()
                val manifests = manifestsContainingUser(user).bind()
                flightsFromManifests(manifests).bind()
            }

    fun userFlightsFM(name: String): Kind<F, Nel<Flight>> =
            userByName(name).flatMap { user ->
                manifestsContainingUser(user).flatMap { manifests ->
                    flightsFromManifests(manifests)
                }
            }
}


object OptionToTry : FunctionK<ForOption, ForTry> {
    override fun <A> invoke(fa: Kind<ForOption, A>): Kind<ForTry, A> =
            fa.fix().fold({Try.raise(EmptyReponseException("field not found"))}){
                Try.just(it)
            }
}
typealias EitherEmpty = EitherPartialOf<Failure>
object OptionToEither : FunctionK<ForOption, EitherEmpty> {
    override fun <A> invoke(fa: Kind<ForOption, A>): Kind<EitherEmpty, A> =
            fa.fix().toEither { NotFound("field not found") }

}

object OptionIdentity : FunctionK<ForOption, ForOption> {
    override fun <A> invoke(fa: Kind<ForOption, A>): Kind<ForOption, A> = fa
}

object OptionToDeferredK : FunctionK<ForOption, ForDeferredK> {
    override fun <A> invoke(fa: Kind<ForOption, A>): Kind<ForDeferredK, A> =
            fa.fix().fold( { DeferredK.failed(EmptyReponseException("field not found")) }){
                DeferredK.just(it)
            }

}
