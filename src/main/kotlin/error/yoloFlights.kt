package error

import error.repository.flightManafests
import error.repository.flights
import error.repository.users
import error.yoloFlights.userFlights

object yoloFlights {
    fun userByName(name: String): User =
            users.filter { it.name == name }.first()

    fun manifestsContainingUser(user: User): List<FlightManafest> =
            flightManafests.filter { fm -> fm.passengers.contains(user.id) }

    fun flightById(flightNo: Int): Flight =
            flights.filter { it.flightNo == flightNo }.first()


    fun userFlights(name: String): List<Flight> {
        try {
            val user = userByName(name)
            val manifests = manifestsContainingUser(user)
            return manifests.map { flightById(it.flightNo) }
        } catch (e: Exception) {
            println("Something went wrong ${e.message}")
            throw e
        }
    }
}

fun main(s: Array<String>) {

    println(userFlights("bob"))

    println(userFlights("brian"))
}