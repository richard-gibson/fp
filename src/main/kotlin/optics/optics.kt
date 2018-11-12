package optics

import arrow.core.Either
import arrow.core.Option
import arrow.core.compose
import optics.Address.Companion.street
import optics.Employee.Companion.address
import java.math.BigDecimal

data class Lens<S, A>(
val get: (S) -> A,
val set: (A) -> (S) -> S
) {
    infix fun <B> compose(that: Lens<A, B>): Lens<S, B> =
            Lens(get = that.get compose this.get,
                    set =
                    {b: B -> {s: S -> this.set(that.set(b)(this.get(s)))(s)} })//??!?!?!


    fun updated(f: (A) -> A): (S) -> S =
            { s: S -> this.set(f(this.get(s)))(s) }

}



data class Prism<S, A>(
        val get: (S) -> Option<A>,
        val set: (A) -> S) { 
    infix fun <B> compose(that: Prism<A, B>): Prism<S, B> =
    Prism(get = {s: S -> (this.get(s).flatMap(that.get))},
          set = this.set compose that.set)

    infix fun <B> compose(that: Lens<A, B>): Optional<S, B> = TODO()

}



data class Optional<S, A>(
        val getOrModify: (S) -> Either<S, A>,
        val set: (A) -> (S) -> S) {
    fun <B> compose(that: Optional<A, B>): Optional<S, B> = TODO()

    fun <B> compose(that: Lens<A, B>): Optional<S, B> = TODO()

    fun <B> compose(that: Prism<A, B>): Optional<S, B> = TODO()

//    fun <B> compose(that: Traversal<A, B>): Traversal<S, B> = TODO()

}

val i : (Int) -> (Int) -> Int =
        {i1 -> {i2 -> i1 +i2}}
val j = i(1)(2)

data class Employee(
        val name: String,
        val dob: java.time.Instant,
        val salary: BigDecimal,
        val address: Address) {
   companion object {
        val salary: Lens<Employee, BigDecimal> =
        Lens({it.salary}, {s -> { it.copy(salary = s)}})
       val address: Lens<Employee, Address> =
               Lens({it.address}, {a -> {it.copy(address = a)}})
    }
}
data class Address(
        val number: String,
        val street: String,
        val postalCode: String,
        val country: String){
    companion object {
        val street: Lens<Address, String> =
                Lens({it.street}, {st -> {it.copy(street = st)}})
    }
}

sealed class Country{
    object USA    : Country()
    object UK     : Country()
    object Poland : Country()

    companion object  {
        val usa: Prism<Country, Unit> = TODO()
    }
}


val employeeStreet =  address compose street

fun main(a: Array<String>){
    val emp =
            Employee(name = "bill", dob = java.time.Instant.now(),
            salary = BigDecimal(100.00),
            address = Address(number = "12A", street = "foo st",
            postalCode = "TR34 5JR",  country="UK"))

    println(employeeStreet.updated{it.toUpperCase()}(emp))
}