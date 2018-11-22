package collections
sealed class PList<out A> {
    abstract fun isEmpty(): Boolean
    abstract fun head(): A
    abstract fun tail(): PList<A>

    companion object {
        operator fun <A> invoke(x: A, xs: PList<A> = Nil): PList<A> = Cons(x, xs)
        operator fun <A> invoke(vararg a: A) = fromArr(a)

        private fun <A> fromList(l: List<A>): PList<A> =
                if (l.isEmpty()) Nil
                else Cons(l[0], fromList(l.drop(1)))

        private fun <A> fromArr(xs: Array<A>): PList<A> =
                if (xs.isEmpty()) Nil
                else Cons(xs[0], fromList(xs.drop(1)))

        fun <A> empty(): PList<A> = Nil

    }
}

object Nil : PList<Nothing>() {
    override fun isEmpty(): Boolean = true
    override fun head(): Nothing = throw UnsupportedOperationException("head of empty list")
    override fun tail(): PList<Nothing> = throw UnsupportedOperationException("tail of empty list")
    override fun toString() = "Nil"
}

data class Cons<A>(val head: A, val tail: PList<A>) : PList<A>() {
    override fun isEmpty(): Boolean = false
    override fun head(): A = head
    override fun tail(): PList<A> = tail
    override fun toString() = "$head, $tail"
}

//ListOps
fun <A,B> PList<A>.fold(empty: (PList<A>) -> B, notEmpty: (PList<A>) -> B): B =
        when (this) {
            is Nil -> empty(this)
            is Cons -> notEmpty(this)
        }

infix fun <A> A.prep(xs: PList<A>): PList<A> = Cons(this, xs)

infix fun <A> PList<A>.join(xs: PList<A>): PList<A> =
        fold({ xs }, { Cons(head(), tail() join xs) })

infix fun <A> PList<A>.app(a: A): PList<A> =
        this join PList(a)

fun <A, B> PList<A>.map(f: (A) -> B): PList<B> =
        fold({ Nil }, { Cons(f(head()), tail().map(f)) })

fun <A, B> PList<A>.flatMap(f: (A) -> PList<B>): PList<B> =
//        map(f).flatten()

//      or more efficiently as 1 pass
      fold({ Nil }, { f(head()) join tail().flatMap(f) })


fun <A, B> PList<A>.foldLeft(acc: B, f: (B, A) -> B): B =
        fold({ acc }, { tail().foldLeft(f(acc, head()), f) })

fun <A> PList<PList<A>>.flatten(): PList<A> =
        foldLeft(PList.empty()) { acc, e -> acc join e }

fun <A, B> PList<A>.foldRight(acc: B, f: (B, A) -> B): B =
        fold({ acc }, { f(tail().foldRight(acc, f), head()) })

fun <A> PList<A>.reverse(): PList<A> =
        foldLeft(PList.empty()) { acc, e -> Cons(e, acc) }

fun <A> PList<A>.filter(pred: (A) -> Boolean): PList<A> =
        foldRight(PList.empty()) { acc, e ->
            if (pred(e)) Cons(e, acc)
            else acc
        }

fun <A> PList<A>.filterNot(pred: (A) -> Boolean): PList<A> =
        foldRight(PList.empty()) { acc, e ->
            if (!pred(e))
                Cons(e, acc)
            else acc
        }

fun <A> PList<A>.partition(pred: (A) -> Boolean): Pair<PList<A>, PList<A>> =
        foldRight(Pair(PList.empty(), PList.empty())) { (pos, neg), e ->
            if (pred(e)) Cons(e, pos) to neg
            else pos to Cons(e, neg)
        }

fun <A> PList<A>.dropWhile(pred: (A) -> Boolean): PList<A> =
        fold({ Nil }, {
            if (pred(head())) tail().dropWhile(pred)
            else this
        })


/**
 *
 def splitAt(n: Int): (IList[A], IList[A]) = {
@tailrec def splitAt0(n: Int, as: IList[A], accum: IList[A]): (IList[A], IList[A]) =
if (n < 1) (accum.reverse, as) else as match {
case INil() => (this, empty)
case ICons(h, t) => splitAt0(n - 1, t, h :: accum)
}
splitAt0(n, this, empty)
}
 */
fun <A> PList<A>.splitAt(i: Int): Pair<PList<A>, PList<A>> {
    fun _splitAt(n: Int, dec: PList<A>, acc: PList<A>) {
        if (n < 1) dec to acc
        else when(dec) {
            is Nil -> (this to Nil)
        }
    }
    return TODO()
}


fun main(a: Array<String>) {

    val l1: PList<String> = PList("bar", "baz")
    val l2: PList<String> = PList("foo", "bar", "baz")
    val l3: PList<String> = PList.empty()


    println(l1 app "box")
    println(l1 join l2)

    println(l1 join l3)
    println(l3 join l2)

    val l4 = PList(1, 2, 3, 4)
    println(l4)
    println(l4.map { x -> x + 1 })
    println(l4.reverse())


    println(l2.foldLeft("") { acc, e ->
        "$acc, $e"
    })

    println(l2.foldRight("") { acc, e ->
        "$acc, $e"
    })

    println(l2.reverse())


    val dropped = l4.dropWhile { it % 2 == 1 }
    val evens = l4.filter { it % 2 == 0 }
    val odds = l4.filterNot { it % 2 == 0 }
    val split = l4.partition { it % 2 == 0 }

    println(dropped)
    println(evens)
    println(odds)
    println(split)
    println(split == (evens to odds))
    println("appends ${evens join odds}")

    println(l4.map { PList(it) }.flatten())
    println(l4.flatMap { PList(it) })
    println(l4.map { PList(it) }.flatten() == l4.flatMap { PList(it) })
    println(l4)

}

