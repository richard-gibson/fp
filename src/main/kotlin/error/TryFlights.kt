package error

import error.repository.flightManafests
import error.repository.flights
import error.repository.users
import arrow.core.*
import arrow.data.*
import arrow.instances.`try`.applicative.applicative
import arrow.instances.`try`.monad.binding

object TryFlights {
    data class EmptyReponseException(val msg: String) : Exception(msg)

    fun <T> T?.toTry(ifEmpty: () -> Throwable): Try<T> =
            this?.let { arrow.core.Success(it) } ?: arrow.core.Failure(ifEmpty())

    fun <T> Nel<Try<T>>.flip(): Try<Nel<T>> =
            this.sequence(Try.applicative()).fix()

    fun userByName(name: String): Try<User> =
            users.firstOrNull { it.name == name }.toTry { EmptyReponseException("no user id for $name") }

    fun manifestsContainingUser(user: User): Try<Nel<FlightManafest>> =
            Nel.fromList(flightManafests.filter { fm -> fm.passengers.contains(user.id) })
                    .fold({ Failure(EmptyReponseException("no flight manafests found for user ${user.name}")) },
                            { manifests -> Success(manifests) })

    fun flightById(flightNo: Int): Try<Flight> =
            flights.firstOrNull { it.flightNo == flightNo }
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

    fun userFlightsFM(name: String): Try<Nel<Flight>> =
            userByName(name).flatMap { user ->
                manifestsContainingUser(user).flatMap { manifests ->
                    manifests.map { flightById(it.flightNo) }.flip()
                }
            }
}