package domain

import domain.repository.flightManafests
import domain.repository.flights
import domain.repository.users
import arrow.core.*
import arrow.data.*
import arrow.instances.either.applicative.applicative
import arrow.instances.either.monad.monad


object EithFlights {


    fun <T> Nel<Either<Failure, T>>.flip(): Either<Failure, Nel<T>> =
            this.sequence(Either.applicative()).fix()

    fun userByName(name: String): Either<Failure, User> =
            Option.fromNullable(users.firstOrNull { it.name == name })
                    .toEither { NotFound("no user id for $name") }

    fun manifestsContainingUser(user: User): Either<Failure, Nel<FlightManafest>> =
            attemptFetchManafests(user)
                    .flatMap {
                        Nel.fromList(it)
                                .toEither { NotFound("no flight manafests found for user ${user.name}") }
                    }

    fun attemptFetchManafests(user: User): Either<Failure, List<FlightManafest>> =
            Try { flightManafests.filter { fm -> fm.passengers.contains(user.id) } }.toEither { FlightSystemException(it) }

    fun flightById(flightNo: Int): Either<Failure, Flight> =
            Option.fromNullable(flights.firstOrNull { it.flightNo == flightNo })
                    .toEither { NotFound("no flights for $flightNo") }

    fun flightsFromManifests(manifests: Nel<FlightManafest>): Either<Failure, Nel<Flight>> =
            manifests.map { flightById(it.flightNo) }.flip()

    fun userFlights(name: String): Either<Failure, Nel<Flight>> =
            Either.monad<Failure>().binding {
                val user = userByName(name).bind()
                val manifests = manifestsContainingUser(user).bind()
                val eithFlights = manifests.map { flightById(it.flightNo) }
                eithFlights.flip().bind()
            }.fix()

    fun userFlightsFlatMap(name: String): Either<Failure, Nel<Flight>> =
            userByName(name).flatMap { user ->
                manifestsContainingUser(user).flatMap { manifests ->
                    manifests.map { flightById(it.flightNo) }.flip()
                }
            }
}