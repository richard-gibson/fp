package domain.validation


import arrow.core.*
import arrow.data.*
import arrow.instances.either.applicative.applicative
import arrow.instances.either.monadError.monadError
import arrow.instances.nonemptylist.semigroup.semigroup
import arrow.instances.validated.applicativeError.applicativeError
import domain.repository.cities
import domain.repository.zips

sealed class Failure
data class EmptyString(val fieldName: String) : Failure()
data class InvalidCity(val fieldName: String) : Failure()
data class ValueOutOfRange(val i: Int) : Failure()


data class Employee(val name: String, val zipCode: String, val city: String, val salary: Int)

fun <L, R> Either<L, R>.ensure(err: () -> L, pred: (R) -> Boolean): Either<L, R> =
        with(Either.monadError<L>()) { this@ensure.ensure(err, pred) }.fix()

fun <A, B> Either<A, B>.toValidatedNel(): ValidatedNel<A, B> =
        this.fold({ it.invalidNel() }, { it.valid() })

// Create validation functions for nonBlank, inRange, validZip, validCities

fun nonBlank(fieldName: String, data: String): Either<Failure, String> =
        data.right().ensure({ EmptyString("$fieldName cannot be blank") }, { it.isNotEmpty() })

fun inRange(lower: Int, upper: Int, data: Int): Either<Failure, Int> =
        data.right().ensure({ ValueOutOfRange(data) }, { it in lower..upper })

fun validZip(data: String): Either<Failure, String> =
        data.right().ensure({ InvalidCity(data) }, { it in zips })

fun validCities(data: String): Either<Failure, String> =
        data.right().ensure({ InvalidCity(data) }, { it in cities })


//Validate and build an employee using bind
fun empEitherFromMonad(name: String, zipCode: String, city: String, salary: Int): Either<Failure, Employee> =
        Either.monadError<Failure>().binding {
            val n = nonBlank("name", name).bind()
            val z = validZip(zipCode).bind()
            val c = validCities(city).bind()
            val s = inRange(10, 20, salary).bind()
            Employee(n, z, c, s)
        }.fix()

//Validate and build an employee using applicative map
fun empEitherFromApp(name: String, zipCode: String, city: String, salary: Int): Either<Failure, Employee> =
        Either.applicative<Failure>().map(
                nonBlank("name", name),
                validZip(zipCode),
                validCities(city),
                inRange(10, 20, salary)) { (n, z, c, s) ->
            Employee(n, z, c, s)
        }.fix()

//Validate and build an employee using Validated
fun empValidatedFromApp(name: String, zipCode: String, city: String, salary: Int): Validated<Nel<Failure>, Employee> =
        Validated.applicativeError<Nel<Failure>>(NonEmptyList.semigroup()).map(
                nonBlank("name", name).toValidatedNel(),
                validZip(zipCode).toValidatedNel(),
                validCities(city).toValidatedNel(),
                inRange(10, 20, salary).toValidatedNel()) { (n, z, c, s) ->
            Employee(n, z, c, s)
        }.fix()
