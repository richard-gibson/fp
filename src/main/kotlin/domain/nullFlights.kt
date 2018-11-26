package domain

import domain.repository.flightManafests
import domain.repository.flights
import domain.repository.users
import domain.nullFlights.userFlights

object nullFlights {

    fun userByName(name: String): User? =
            users.filter { it.name == name }.firstOrNull()

    fun manifestsContainingUser(user: User): List<FlightManafest>? =
            flightManafests.filter { fm -> fm.passengers.contains(user.id) }

    fun flightById(flightNo: Int): Flight? =
            flights.filter { it.flightNo == flightNo }.firstOrNull()


    fun userFlights(name: String): List<Flight>? =
        try {
            userByName(name)?.let { user ->
                manifestsContainingUser(user)?.let { manifests ->
                    manifests.map { flightById(it.flightNo) }.filterNotNull()
                }
            }
        } catch (e: Exception) {
            println("Something went wrong ${e.message}")
            throw e
        }

}

fun main(s: Array<String>) {

    println(userFlights("bob"))

    println(userFlights("brian"))
}