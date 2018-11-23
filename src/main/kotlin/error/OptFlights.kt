package error

import error.repository.flightManafests
import error.repository.flights
import error.repository.users
import arrow.core.*
import arrow.data.*
import arrow.instances.option.applicative.applicative
import arrow.instances.option.monad.binding

object OptFlights {

    fun userByName(name: String): Option<User> =
            users.firstOrNull { it.name == name }.toOption()

    fun manifestsContainingUser(user: User): Option<Nel<FlightManafest>> =
            Nel.fromList(flightManafests.filter { fm -> fm.passengers.contains(user.id) })

    fun flightById(flightNo: Int): Option<Flight> =
            flights.firstOrNull { it.flightNo == flightNo }.toOption()


    fun userFlights(name: String): Option<Nel<Flight>> =
            try {
                binding {
                    val user = userByName(name).bind()
                    val manifests = manifestsContainingUser(user).bind()
                    val optFlights = manifests.map { flightById(it.flightNo) }
                    optFlights.sequence(Option.applicative()).bind()
                }

            } catch (e: Exception) {
                println("Something went wrong ${e.message}")
                throw e
            }
}