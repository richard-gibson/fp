package error

import arrow.core.Option

data class Street(val number: Int, val name: String)
data class Address(val city: String, val street: Street)
data class User(val id: Int, val name: String, val email: String, val address: Address, val phone: Option<Int> = Option.empty())
data class Flight(val flightNo: Int, val destination: String, val departure: String)
data class FlightManafest(val flightNo: Int, val passengers: List<Int>)