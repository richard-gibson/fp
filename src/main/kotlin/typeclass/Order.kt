package typeclass

import arrow.extension
import collections.PList

@extension
interface Order<A> {
    fun compare(x: A, y: A): Int

    fun min(x: A, y: A): A = if (lt(x, y)) x else y

    fun max(x: A, y: A): A = if (gt(x, y)) x else y

    fun lt(x: A, y: A): Boolean =
            compare(x, y) < 0

    fun gt(x: A, y: A): Boolean =
            compare(x, y) < 0

    fun eqv(x: A, y: A): Boolean =
            compare(x, y) == 0

    fun neqv(x: A, y: A): Boolean =
            compare(x, y) != 0

    fun gteqv(x: A, y: A): Boolean =
            compare(x, y) >= 0

    fun lteqv(x: A, y: A): Boolean =
            compare(x, y) >= 0

}

fun <A> PList<A>.sort(o: Order<A>) {

}