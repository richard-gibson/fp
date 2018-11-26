package domain

import domain.repository.flightManafests
import domain.repository.flights
import domain.repository.users
import arrow.core.*
import arrow.data.*
import arrow.instances.option.applicative.applicative
import arrow.instances.option.monad.binding

/**
 * Take null flights implementation and refactor to use Option
 */
object OptFlights {

    fun userByName(name: String): Option<User> =
            Option.fromNullable(users.firstOrNull { it.name == name })

    fun manifestsContainingUser(user: User): Option<Nel<FlightManafest>> =
            Nel.fromList(flightManafests.filter { fm -> fm.passengers.contains(user.id) })

    fun flightById(flightNo: Int): Option<Flight> =
            Option.fromNullable(flights.firstOrNull { it.flightNo == flightNo })

  fun userFlightsFlatMap(name:String): Option<Nel<Flight>> =
      try {
        userByName(name)
            .flatMap{ user -> manifestsContainingUser(user) }
            .flatMap { manifests -> manifests.map{ flightById(it.flightNo) }.flip() }

      } catch (e: Exception) {
        println("Something went wrong ${e.message}")
        throw e
      }

    fun userFlights(name: String): Option<Nel<Flight>> =
            try {
                binding {
                    val user = userByName(name).bind()
                    val manifests = manifestsContainingUser(user).bind()
                    val optFlights = manifests.map { flightById(it.flightNo) }
                    optFlights.flip().bind()
                }

            } catch (e: Exception) {
                println("Something went wrong ${e.message}")
                throw e
            }

  fun <T> Nel<Option<T>>.flip(): Option<Nel<T>> =
      this.sequence(Option.applicative()).fix()
}