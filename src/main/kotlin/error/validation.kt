package error.validation


import arrow.Kind
import arrow.core.*
import arrow.data.*
import arrow.instances.either.applicative.applicative
import arrow.instances.either.monadError.monadError
import arrow.instances.nonemptylist.semigroup.semigroup
import arrow.instances.validated.applicative.applicative
import arrow.instances.validated.applicativeError.applicativeError
import arrow.typeclasses.ApplicativeError
import com.sun.org.apache.xerces.internal.impl.dv.xs.BooleanDV

sealed class Failure
data class EmptyString(val fieldName: String) : Failure()
data class InvalidCity(val fieldName: String) : Failure()
data class ValueOutOfRange(val i: Int) : Failure()


val zip = listOf("10002", "10005", "10022")
val cities = listOf("Dublin", "London", "Madrid")


data class Employee(val name: String, val zipCode: String, val city: String, val salary: Int)

val MEF = Either.monadError<Failure>()

fun <L, R> Either<L, R>.ensure(err: () -> L, pred: (R) -> Boolean): Either<L, R> =
        with(Either.monadError<L>()) { this@ensure.ensure(err, pred)}.fix()

fun <A, B> Either<A, B>.toValidatedNel(): ValidatedNel<A, B> =
        this.fold({ it.invalidNel() }, { it.valid() })

fun nonBlank(fieldName: String, data: String): Either<Failure, String> =
        data.right().ensure({ EmptyString("$fieldName cannot be blank") }, { it.isNotEmpty() })

fun inRange(lower: Int, upper: Int, data: Int): Either<Failure, Int> =
        data.right().ensure({ ValueOutOfRange(data) }, { it in lower..upper })

fun validZip(data: String): Either<Failure, String> =
        data.right().ensure({ InvalidCity(data) }, { it in zip })

fun validCities(data: String): Either<Failure, String> =
        data.right().ensure({ InvalidCity(data) }, { it in cities } )

fun empEitherFromMonad(name: String, zipCode: String, city: String, salary: Int): Either<Failure, Employee> =
        Either.monadError<Failure>().binding {
            val n = nonBlank("name", name).bind()
            val z = validZip(zipCode).bind()
            val c = validCities(city).bind()
            val s = inRange(10, 20, salary).bind()
            Employee(n, z, c, s)
        }.fix()

fun empEitherFromApp(name: String, zipCode: String, city: String, salary: Int): Either<Failure, Employee> =
        Either.applicative<Failure>().map(
                nonBlank("name", name),
                validZip(zipCode),
                validCities(city),
                inRange(10, 20, salary)) { (n, z, c, s) ->
            Employee(n, z, c, s)
        }.fix()

fun empValidatedFromApp(name: String, zipCode: String, city: String, salary: Int): Validated<Nel<Failure>, Employee> =
        Validated.applicativeError<Nel<Failure>>(NonEmptyList.semigroup()).map(
                nonBlank("name", name).toValidatedNel(),
                validZip(zipCode).toValidatedNel(),
                validCities(city).toValidatedNel(),
                inRange(10, 20, salary).toValidatedNel()) { (n, z, c, s) ->
            Employee(n, z, c, s)
        }.fix()

fun main(a: Array<String>) {
    println(empEitherFromApp("foo", "00111", "Dublin", 15))
    println(empEitherFromApp("", "00", "Washington", 17500))
    println(empEitherFromApp("foo", "00", "Washington", 17500))
    println()

    println()
    println(empEitherFromMonad("foo", "00111", "Dublin", 15))
    println(empEitherFromMonad("", "00", "Washington", 17500))
    println(empEitherFromMonad("foo", "00", "Washington", 17500))
    println()

    println()
    println(empValidatedFromApp("foo", "00111", "Dublin", 15))
    println(empValidatedFromApp("", "00", "Washington", 17500))
    println(empValidatedFromApp("foo", "00", "Washington", 17500))
}
