import arrow.core.*

/*
fun <A, B, C> Tuple2<Tuple2<A, B>, C>.flatten(): Tuple3<A, B, C> = Tuple3(a.a, a.b, b)
fun <A, B, C, D> Tuple2<Tuple2<Tuple2<A, B>, C>, D>.flatten(): Tuple4<A, B, C, D> = Tuple4(a.a.a, a.a.b, a.b, b)
fun <A, B, C, D, E> Tuple2<Tuple2<Tuple2<Tuple2<A, B>, C>, D>, E>.flatten(): Tuple5<A, B, C, D, E> = Tuple5(a.a.a.a, a.a.a.b, a.a.b, a.b, b)
fun <A, B, C, D, E, F> Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<A, B>, C>, D>, E>, F>.flatten(): Tuple6<A, B, C, D, E, F> = Tuple6(a.a.a.a.a, a.a.a.a.b, a.a.a.b, a.a.b, a.b, b)
fun <A, B, C, D, E, F, G> Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<A, B>, C>, D>, E>, F>, G>.flatten(): Tuple7<A, B, C, D, E, F, G> = Tuple7(a.a.a.a.a.a, a.a.a.a.a.b, a.a.a.a.b, a.a.a.b, a.a.b, a.b, b)
fun <A, B, C, D, E, F, G, H> Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<A, B>, C>, D>, E>, F>, G>, H>.flatten(): Tuple8<A, B, C, D, E, F, G, H> = Tuple8(a.a.a.a.a.a.a, a.a.a.a.a.a.b, a.a.a.a.a.b, a.a.a.a.b, a.a.a.b, a.a.b, a.b, b)
fun <A, B, C, D, E, F, G, H, I> Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<A, B>, C>, D>, E>, F>, G>, H>, I>.flatten(): Tuple9<A, B, C, D, E, F, G, H, I> = Tuple9(a.a.a.a.a.a.a.a, a.a.a.a.a.a.a.b, a.a.a.a.a.a.b, a.a.a.a.a.b, a.a.a.a.b, a.a.a.b, a.a.b, a.b, b)
fun <A, B, C, D, E, F, G, H, I, J> Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<A, B>, C>, D>, E>, F>, G>, H>, I>, J>.flatten(): Tuple10<A, B, C, D, E, F, G, H, I, J> = Tuple10(a.a.a.a.a.a.a.a.a, a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.b, a.a.a.a.a.a.b, a.a.a.a.a.b, a.a.a.a.b, a.a.a.b, a.a.b, a.b, b)
fun <A, B, C, D, E, F, G, H, I, J, K> Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<A, B>, C>, D>, E>, F>, G>, H>, I>, J>, K>.flatten(): Tuple11<A, B, C, D, E, F, G, H, I, J, K> = Tuple11(a.a.a.a.a.a.a.a.a.a, a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.b, a.a.a.a.a.a.b, a.a.a.a.a.b, a.a.a.a.b, a.a.a.b, a.a.b, a.b, b)
fun <A, B, C, D, E, F, G, H, I, J, K, L> Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<A, B>, C>, D>, E>, F>, G>, H>, I>, J>, K>, L>.flatten(): Tuple12<A, B, C, D, E, F, G, H, I, J, K, L> = Tuple12(a.a.a.a.a.a.a.a.a.a.a, a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.b, a.a.a.a.a.a.b, a.a.a.a.a.b, a.a.a.a.b, a.a.a.b, a.a.b, a.b, b)
fun <A, B, C, D, E, F, G, H, I, J, K, L, M> Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<A, B>, C>, D>, E>, F>, G>, H>, I>, J>, K>, L>, M>.flatten(): Tuple13<A, B, C, D, E, F, G, H, I, J, K, L, M> = Tuple13(a.a.a.a.a.a.a.a.a.a.a.a, a.a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.b, a.a.a.a.a.a.b, a.a.a.a.a.b, a.a.a.a.b, a.a.a.b, a.a.b, a.b, b)
fun <A, B, C, D, E, F, G, H, I, J, K, L, M, N> Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<A, B>, C>, D>, E>, F>, G>, H>, I>, J>, K>, L>, M>, N>.flatten(): Tuple14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> = Tuple14(a.a.a.a.a.a.a.a.a.a.a.a.a, a.a.a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.b, a.a.a.a.a.a.b, a.a.a.a.a.b, a.a.a.a.b, a.a.a.b, a.a.b, a.b, b)
fun <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<A, B>, C>, D>, E>, F>, G>, H>, I>, J>, K>, L>, M>, N>, O>.flatten(): Tuple15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> = Tuple15(a.a.a.a.a.a.a.a.a.a.a.a.a.a, a.a.a.a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.b, a.a.a.a.a.a.b, a.a.a.a.a.b, a.a.a.a.b, a.a.a.b, a.a.b, a.b, b)
fun <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<A, B>, C>, D>, E>, F>, G>, H>, I>, J>, K>, L>, M>, N>, O>, P>.flatten(): Tuple16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> = Tuple16(a.a.a.a.a.a.a.a.a.a.a.a.a.a.a, a.a.a.a.a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.b, a.a.a.a.a.a.b, a.a.a.a.a.b, a.a.a.a.b, a.a.a.b, a.a.b, a.b, b)
fun <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<A, B>, C>, D>, E>, F>, G>, H>, I>, J>, K>, L>, M>, N>, O>, P>, Q>.flatten(): Tuple17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> = Tuple17(a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a, a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.b, a.a.a.a.a.a.b, a.a.a.a.a.b, a.a.a.a.b, a.a.a.b, a.a.b, a.b, b)
fun <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<A, B>, C>, D>, E>, F>, G>, H>, I>, J>, K>, L>, M>, N>, O>, P>, Q>, R>.flatten(): Tuple18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> = Tuple18(a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a, a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.b, a.a.a.a.a.a.b, a.a.a.a.a.b, a.a.a.a.b, a.a.a.b, a.a.b, a.b, b)
fun <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<A, B>, C>, D>, E>, F>, G>, H>, I>, J>, K>, L>, M>, N>, O>, P>, Q>, R>, S>.flatten(): Tuple19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> = Tuple19(a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a, a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.b, a.a.a.a.a.a.b, a.a.a.a.a.b, a.a.a.a.b, a.a.a.b, a.a.b, a.b, b)
fun <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<A, B>, C>, D>, E>, F>, G>, H>, I>, J>, K>, L>, M>, N>, O>, P>, Q>, R>, S>, T>.flatten(): Tuple20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> = Tuple20(a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a, a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.b, a.a.a.a.a.a.b, a.a.a.a.a.b, a.a.a.a.b, a.a.a.b, a.a.b, a.b, b)
fun <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<Tuple2<A, B>, C>, D>, E>, F>, G>, H>, I>, J>, K>, L>, M>, N>, O>, P>, Q>, R>, S>, T>, U>.flatten(): Tuple21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> = Tuple21(a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a, a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.a.b, a.a.a.a.a.a.a.b, a.a.a.a.a.a.b, a.a.a.a.a.b, a.a.a.a.b, a.a.a.b, a.a.b, a.b, b)
*/

val availableLetters = ('a'..'z').toList().map { it.toString() }
val maxTuple = 21

sealed abstract class Node {
    abstract val name: String
}

data class Root(override val name: String) : Node()
data class Child(override val name: String, val dependsOn: Set<String>) : Node()

val f = Root("f")
val c = Root("c")
val e = Child("e", setOf("f"))
val d = Child("d", setOf("c"))
val b = Child("b", setOf("c", "d"))
val a = Child("a", setOf("b", "c", "d"))
val g = Child("g", setOf("h"))
val h = Child("h", setOf("a", "g"))

val depTree = listOf(a, b, d, e, f, c, g, h)

val depsResolved: (List<String>) -> (Node) -> Boolean = { l ->
    { n ->
        when (n) {
            is Root -> true
            is Child -> (l intersect n.dependsOn) == n.dependsOn
        }
    }
}

fun buildDeps(depTree: List<Node>, resolvedDeps: List<Node> = emptyList(), lastResolved: List<Node> = emptyList()): Pair<List<Node>, List<Node>> {
    val (resolved, unresolved) = depTree.partition { depsResolved(resolvedDeps.map(Node::name))(it) }
    return if(resolved == lastResolved) resolvedDeps to unresolved
            else buildDeps(unresolved, resolvedDeps + resolved, resolved)
}



fun main(s: Array<String>) {
    println(buildDeps(depTree))
    println(listOf(1,2,3,4,5) + listOf(6,7,8,9))
}
/*
([Root(name=f),
Root(name=c),
Child(name=d, dependsOn=[c]),
Child(name=e, dependsOn=[f]),
Child(name=b, dependsOn=[c, d]),
Child(name=a, dependsOn=[b, c, d])], [Child(name=g, dependsOn=[h]), Child(name=h, dependsOn=[a, h])])
*/
/*    for (i in 3..maxTuple) {
        val letters = availableLetters.take(i).map { it.toUpperCase() }
        val tuple2Cnt = i - 2
        val tupSignature =
                "fun <" + letters.joinToString() + "> " +
                        (3..i).fold("Tuple2<") { acc, _ -> acc + "Tuple2<" } + "A, B>, " +
                        letters.drop(2).map { it + ">" }.joinToString() +
                        ".flatten(): Tuple$i<" + letters.joinToString() + "> = "


        val params = (1..i-1).fold(listOf("b")){acc, _ -> acc + ("a."+acc.last())}.reversed()


        val upParams: List<String> = listOf(params.first().substring(0, params.first().length-2)) + params.drop(1)
        val construct = "Tuple$i(" + upParams.joinToString() + ")"


        println(tupSignature+construct)

    }*/

//    val t = (1 toT "foo" toT true toT 54 toT "Adsff " toT 0).flatten()
