package domain

import domain.repository.flightManafests
import domain.repository.flights
import domain.repository.users
import arrow.core.*
import arrow.core.Failure
import arrow.data.*
import arrow.instances.`try`.applicative.applicative
import arrow.instances.`try`.monad.binding

/**
 * Take null flights implementation and refactor to use Try
 */
object TryFlights {

    fun <T> Option<T>.toTry(ifEmpty: () -> Throwable): Try<T> =
            this.fold({ Failure(ifEmpty()) }) { arrow.core.Success(it) }

    fun <T> Nel<Try<T>>.flip(): Try<Nel<T>> =
            this.sequence(Try.applicative()).fix()

    fun userByName(name: String): Try<User> =
            Option.fromNullable(users.firstOrNull { it.name == name })
                    .toTry { EmptyReponseException("no user id for $name") }

    fun manifestsContainingUser(user: User): Try<Nel<FlightManafest>> =
            Nel.fromList(flightManafests.filter { fm -> fm.passengers.contains(user.id) })
                    .fold({ Failure(EmptyReponseException("no flight manafests found for user ${user.name}")) },
                            { manifests -> Success(manifests) })

    fun flightById(flightNo: Int): Try<Flight> =
            Option.fromNullable(flights.firstOrNull { it.flightNo == flightNo })
                    .toTry { EmptyReponseException("no flights for $flightNo") }

    fun flightsFromManifests(manifests: Nel<FlightManafest>): Try<Nel<Flight>> =
            manifests.map { flightById(it.flightNo) }.flip()

    fun userFlights(name: String): Try<Nel<Flight>> =
            binding {
                val user = userByName(name).bind()
                val manifests = manifestsContainingUser(user).bind()
                val tryFlights = manifests.map { flightById(it.flightNo) }
                tryFlights.flip().bind()
            }

    fun userFlightsFlatMap(name: String): Try<Nel<Flight>> =
            userByName(name).flatMap { user ->
                manifestsContainingUser(user).flatMap { manifests ->
                    manifests.map { flightById(it.flightNo) }.flip()
                }
            }
}