package collections

import arrow.test.UnitSpec
import arrow.test.laws.Law
import generators.genNonEmptyPList
import generators.genPList
import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll


//Implement the following laws
// Think of another law that could be used for PList

class PListLaws : UnitSpec() {

    val reverseLaw = Law("a list reversed twice should equal the original list") {
        forAll(genPList(Gen.string())) { plist ->
            plist.reverse().reverse() == plist
        }
    }

    val setHeadLaw = Law("setting the head of a list successful returns a new list with a different head") {
        forAll(genNonEmptyPList(Gen.string())) { plist ->
            val newHead = Gen.string().generate()
            val newList = plist.setHead(newHead)
            newList.head() == newHead &&
                    newList.tail() == plist.tail()
        }
    }

    val splitAndJoinLaw = Law("A list split at any location and joined should equal the original list") {
        forAll(genNonEmptyPList(Gen.string())) { plist ->
            val splitLocation = when(plist) {
                is Nil -> 0
                else -> Gen.choose(0, plist.length()).generate()
            }

            val (left, right) = plist.splitAt(splitLocation)
            left join right == plist
        }

    }

    init {
        testLaws(listOf(reverseLaw, setHeadLaw, splitAndJoinLaw))
    }
}