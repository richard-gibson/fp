package ktcheck

import arrow.core.Either
import arrow.data.k
import arrow.instances.eq
import arrow.test.UnitSpec
import io.kotlintest.matchers.shouldBe
import io.kotlintest.matchers.shouldThrow
import io.kotlintest.properties.*
import io.kotlintest.specs.StringSpec
import arrow.test.laws.Law
import arrow.test.laws.forFew

class HelloKTCheck : StringSpec({

    "check integer commutativity" {
            forAll(Gen.positiveIntegers(), Gen.int()) { no1: Int, no2: Int ->
                println("Checking with $no1 and $no2")
                (no1 + no2) == (no2 + no1)
            }
        }
})

class TablePropsCheck : StringSpec({
        "check table data" {
            forAll(
                    table(
                            headers("no1", "no2", "total"),
                            row(7, 9, 16),
                            row(3, 23, 26),
                            row(10, 16, 26),
                            row(5, 25, 30)
                    )
            ) { no1: Int, no2: Int, sum: Int ->
                println("Checking with $no1 and $no2")
                (no1 + no2) shouldBe sum
            }
        }
    })

class TablePropsErrCheck : StringSpec({
    "check table error data" {
        shouldThrow<AssertionError> {
            forAll(
                    table(
                            headers("no1", "no2", "total"),
                            row(7, 9, 16),
                            row(3, 23, 26),
                            row(10, 16, 56),
                            row(5, 25, 30)
                    )
            ) { no1: Int, no2: Int, sum: Int ->
                println("Checking with $no1 and $no2")
                (no1 + no2) shouldBe sum
            }
        }.message shouldBe "Test failed for (no1, 10), (no2, 16), (total, 56) with error expected: 56 but was: 26"
    }
})


class StringBuilderLaws: UnitSpec() {
    init {
        val appending = Law("appending") {
            forAll(Gen.string(), Gen.string()) { str1, str2 ->
                val sb = StringBuilder().append(str1).append(str2)
                sb.toString() == (str1 + str2)
            }
        }

        val inserting = Law("inserting") {
            forFew(20, Gen.string(), Gen.string()) { str1, str2 ->
                val sb = StringBuilder().insert(0, str1).insert(0, str2)
                sb.toString() == (str2 + str1)
            }

        }

        val reversal = Law("reversal") {
            forAll(Gen.string()) { str1 ->
                val sb = StringBuilder().append(str1)
                sb.reverse().toString() == str1.reversed()
            }
        }

        testLaws(listOf(appending, inserting, reversal))
    }
}


data class Card(val rank: String, val suit: String)
data class Hand(val c1: Card, val c2: Card, val c3: Card, val c4: Card, val c5: Card)

val genRank: Gen<String> = Gen.oneOf(listOf("A", "Q", "K", "J", "10", "9", "8", "7", "6", "5", "4", "3", "2"))
val genSuit: Gen<String> = Gen.oneOf(listOf("C", "H", "S", "D"))
val genCard: Gen<Card> =
        Gen.create { Card(genRank.generate(), genSuit.generate()) }

fun rndHand(handSize : Int = 5, generatedCards: Set<Card> = emptySet()): Hand =
    if(generatedCards.size <= handSize)
        rndHand(handSize, generatedCards + genCard.generate())
    else {
        val genCardList = generatedCards.toList()
            Hand(c1 = genCardList[0],
                    c2 = genCardList[1],
                    c3 = genCardList[2],
                    c4 = genCardList[3],
                    c5 = genCardList[4])

    }

val genHand = Gen.create { rndHand() }

class PokerHandLaws : UnitSpec() {
    init {
        testLaws(listOf(Law("unique poker hand") {
            forAll(genHand) { hand ->
                val (c1, c2, c3, c4, c5) = hand
                println(hand)
                setOf(c1, c2, c3, c4, c5).size == 5
            }
        }))
    }
}



/*

class ClassificationKTCheck : StringSpec({

    "check integer commutativity" {
        forAll(1000, Gen.int(), Gen.int()) { no1: Int, no2: Int ->
            classify((no2 + no1) < 0, "positive sum", "negative sum")
            classify((no2 + no1) % 2 == 0, "even sum", "odd sum")
            println("Checking with $no1 and $no2")
            (no1 + no2) == (no2 + no1)
        }
    }
})

//Checking with -206910213 and -1298167582
//Checking with 261784079 and 1461464149
//Checking with -584841675 and 1027719521
//Checking with -1967788949 and -1100590330
//Checking with -1407608341 and -1781282358
//51.00% even sum
//50.10% negative sum
//49.90% positive sum
//49.00% odd sum







class TablePropsErrorCheck : StringSpec({
    "check table data" {
        forallRows(
                row(7, 9, 16),
                row(3, 23, 26),
                row(10, 16, 26),
                row(5, 25, 30)
        ) { no1: Int, no2: Int, sum: Int ->
            println("Checking with $no1 and $no2")
            (no1 + no2) shouldBe sum
        }
    }
})

class TablePropsCheck : StringSpec({
    "check table error data" {
        shouldThrow<AssertionError> {
            forallRows(
                    row(7, 9, 16),
                    row(3, 23, 26),
                    row(10, 16, 56),
                    row(5, 25, 30)
            ) { no1: Int, no2: Int, sum: Int ->
                println("Checking with $no1 and $no2")
                (no1 + no2) shouldBe sum
            }
        }.message shouldBe "Test failed for (no1, 10), (no2, 16), (sum, 56) with error expected: 56 but was: 26"
    }
})*/
