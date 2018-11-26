package generators

import collections.PList
import io.kotlintest.properties.Gen

//create a generator for a non empty PList
fun <A> genNonEmptyPList(ga: Gen<A>): Gen<PList<A>> = Gen.create {
    PList.fromList(Gen.list(ga).generate())
}


//create a generator for an empty PList
fun <A> genEmptyPList(): Gen<PList<A>> = Gen.create {
    PList.empty<A>()
}

//create a generator for an empty or non empty PList
fun <A> genPList(ga: Gen<A>): Gen<PList<A>> = Gen.oneOf(
   listOf(genNonEmptyPList(ga).generate(), genEmptyPList<A>().generate())
)

