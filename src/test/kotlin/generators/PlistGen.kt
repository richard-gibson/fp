package generators

import collections.PList
import io.kotlintest.properties.Gen

fun <A> genNonEmptyPList(ga: Gen<A>): Gen<PList<A>> = Gen.create {
    PList.fromList(Gen.list(ga).generate())
}


fun <A> genEmptyPList(): Gen<PList<A>> = Gen.create {
    PList.empty<A>()
}

fun <A> genPList(ga: Gen<A>): Gen<PList<A>> = Gen.oneOf(
   listOf(genNonEmptyPList(ga).generate(), genEmptyPList<A>().generate())
)

