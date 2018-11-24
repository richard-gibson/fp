package generators

import io.kotlintest.properties.Gen
import java.lang.IllegalStateException
import java.lang.RuntimeException
/*

fun genThrowable(): Gen<Throwable> = object : Gen<Throwable> {
    override fun generate(): Throwable =
        Gen.oneOf(listOf(RuntimeException(), IllegalStateException(), IndexOutOfBoundsException())).generate()
}

fun IntRange.pickMember(): Int =
        Gen.choose(this.first, this.last).generate()

fun main(args: Array<String>) {
    println((1..10).pickMember())
}*/
