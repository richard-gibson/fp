package error

import error.repository.flightManafests
import error.repository.flights
import error.repository.users
import arrow.core.*
import arrow.data.*
import arrow.instances.either.applicative.applicative
import arrow.instances.either.monad.monad

sealed class Failure
data class NotFound(val msg: String) : Failure()
data class FlightSystemException(val exception: Throwable) : Failure()

object EithFlights {
    data class EmptyReponseException(val msg: String) : Exception(msg)

    fun <T, L> T?.toEither(ifEmpty: () -> L): Either<L, T> =
            this?.right() ?: ifEmpty().left()

    fun <T> Nel<Either<Failure, T>>.flip(): Either<Failure, Nel<T>> =
            this.sequence(Either.applicative()).fix()

    fun userByName(name: String): Either<Failure, User> =
            users.firstOrNull { it.name == name }.toEither { NotFound("no user id for $name") }

    fun manifestsContainingUser(user: User): Either<Failure, Nel<FlightManafest>> =
            attemptFetchManafests(user)
                    .flatMap {
                        Nel.fromList(it)
                                .toEither { NotFound("no flight manafests found for user ${user.name}") }
                    }

    fun attemptFetchManafests(user: User): Either<Failure, List<FlightManafest>> =
            Try { flightManafests.filter { fm -> fm.passengers.contains(user.id) } }.toEither { FlightSystemException(it) }

    fun flightById(flightNo: Int): Either<Failure, Flight> =
            flights.firstOrNull { it.flightNo == flightNo }
                    .toEither { NotFound("no flights for $flightNo") }

    fun flightsFromManifests(manifests: Nel<FlightManafest>): Either<Failure, Nel<Flight>> =
            manifests.map { flightById(it.flightNo) }.flip()

    fun userFlights(name: String): Either<Failure, Nel<Flight>> =
            Either.monad<Failure>().binding {
                val user = userByName(name).bind()
                val manifests = manifestsContainingUser(user).bind()
                val tryFlights = manifests.map { flightById(it.flightNo) }
                tryFlights.flip().bind()
            }.fix()

    fun userFlightsFM(name: String): Either<Failure, Nel<Flight>> =
            userByName(name).flatMap { user ->
                manifestsContainingUser(user).flatMap { manifests ->
                    manifests.map { flightById(it.flightNo) }.flip()
                }
            }
}