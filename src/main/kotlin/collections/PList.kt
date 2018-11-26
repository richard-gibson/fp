package collections

sealed class PList<out A> {
    abstract fun isEmpty(): Boolean
    abstract fun head(): A
    abstract fun tail(): PList<A>

    companion object {
        operator fun <A> invoke(x: A, xs: PList<A> = Nil): PList<A> = Cons(x, xs)
        operator fun <A> invoke(vararg a: A) = fromArr(a)

        fun <A> fromList(l: List<A>): PList<A> =
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
/**
 * create a function that creates a new PList with a new head but
 * same tail as before
 */
fun <A> PList<A>.setHead(x: A): PList<A> = Cons(x, tail())

/**
 * create a function that creates a new PList with a new tail but
 * same head as before
 */
fun <A> PList<A>.setTail(xs: PList<A>): PList<A> = Cons(head(), xs)


/**
 * create a function that creates a new PList with a new tail but
 * same head as before
 */
fun <A, B> PList<A>.fold(empty: (PList<A>) -> B, notEmpty: (PList<A>) -> B): B =
        when (this) {
            is Nil -> empty(this)
            is Cons -> notEmpty(this)
        }

// Create a function to calculate the length of a PList
fun <A> PList<A>.length(): Int = fold({ 0 }, { 1 + tail().length() })

// Create hashcode for PList
fun <A> PList<A>.hashcode(): Int = fold({ 0 }, { head().hashCode() + tail().hashCode() })

// Create equals for PList
fun <A> PList<A>.equals(other: PList<A>) =
        this.length() == other.length() && this.hashCode() == other.hashCode()

// Create a function that adds a element to the front of a PList
infix fun <A> A.prepend(xs: PList<A>): PList<A> = Cons(this, xs)

// Create a function that joins 2 PLists
infix fun <A> PList<A>.join(xs: PList<A>): PList<A> =
        fold({ xs }, { Cons(head(), tail() join xs) })

// Create a function that adds an element to the end of a Plist
infix fun <A> PList<A>.append(x: A): PList<A> =
        this join PList(x)

/**
 * Create a function that takes a function f: (A) -> B returns a new Plist with the function
 * applied to each element
 */

fun <A, B> PList<A>.map(f: (A) -> B): PList<B> =
        fold({ Nil }, { Cons(f(head()), tail().map(f)) })

/**
 * Create a function that takes a function f: (A) -> PList<B> returns a new Plist<B> with the function
 * applied to each element
 */
fun <A, B> PList<A>.flatMap(f: (A) -> PList<B>): PList<B> =
        fold({ Nil }, { f(head()) join tail().flatMap(f) })


/**
 * Create a function that reduces a PList from left to right into an new structure
 * e.g. PList("A", "B", "C").foldLeft(""){acc, elem -> acc + "") gives "CBA"
 */
fun <A, B> PList<A>.foldLeft(acc: B, f: (B, A) -> B): B =
        fold({ acc }, { tail().foldLeft(f(acc, head()), f) })

//Create a function that flattens PList<PList<A>> to PList<A>
fun <A> PList<PList<A>>.flatten(): PList<A> =
        fold({ Nil }, { head() join tail().flatten() })

/**
 * Create a function that reduces a PList from left to right into an new structure
 * e.g. PList("A", "B", "C").foldRight(""){acc, elem -> acc + "") gives "ABC"
 */
fun <A, B> PList<A>.foldRight(acc: B, f: (B, A) -> B): B =
        fold({ acc }, { f(tail().foldRight(acc, f), head()) })



//Create a function that produces a new PList with elements reversed
fun <A> PList<A>.reverse(): PList<A> =
        foldLeft(PList.empty()) { acc, e -> Cons(e, acc) }


//Create a function that produces a new PList where elements match predicate
fun <A> PList<A>.filter(pred: (A) -> Boolean): PList<A> =
        foldRight(PList.empty()) { acc, e ->
            if (pred(e)) Cons(e, acc)
            else acc
        }
//Create a function that produces a new PList where elements do not match predicate
fun <A> PList<A>.filterNot(pred: (A) -> Boolean): PList<A> =
        foldRight(PList.empty()) { acc, e ->
            if (!pred(e))
                Cons(e, acc)
            else acc
        }
//Create a function that zips one PList to another
fun <A> PList<A>.zip(other: PList<A>): PList<Pair<A, A>> {
    fun _zip(left: PList<A>, right: PList<A>, acc: PList<Pair<A, A>>): PList<Pair<A, A>> =
            if (left == Nil || right == Nil)
                acc
            else
                _zip(left.tail(), right.tail(),
                        acc append (left.head() to right.head())
                )
    return _zip(this, other, Nil)
}

/**
 * Create a function that produces a pair of PList where the first PList matches a predicate
 * and the second fails the predicate
 */
fun <A> PList<A>.partition(pred: (A) -> Boolean): Pair<PList<A>, PList<A>> =
        foldRight(Pair(PList.empty(), PList.empty())) { (pos, neg), e ->
            if (pred(e)) Cons(e, pos) to neg
            else pos to Cons(e, neg)
        }

//Create a function that produces a new PList with the first N elements of the original
fun <A> PList<A>.takeN(n: Int): PList<A> =
        fold({ Nil }, {
            if (n > 0) head() prepend tail().takeN(n - 1)
            else Nil
        })


//Create a function that produces a new PList with all elements until predicate fails
fun <A> PList<A>.takeWhile(pred: (A) -> Boolean): PList<A> =
        fold({ Nil }, {
            if (pred(head())) head() prepend tail().takeWhile(pred)
            else Nil
        })

//Create a function that produces a new PList that drops all elements until predicate fails
fun <A> PList<A>.dropWhile(pred: (A) -> Boolean): PList<A> =
        fold({ Nil }, {
            if (pred(head())) tail().dropWhile(pred)
            else this
        })

//Create a function that produces a new PList without the first N elements of the original
fun <A> PList<A>.dropN(n: Int): PList<A> =
        fold({ Nil }, {
            if (n > 0) tail().dropN(n - 1)
            else this
        })
/**
 * Create a function that produces a pair of PList where the first PList contains
 * the first N elements of the original and the second contains the rest of the original
 */
fun <A> PList<A>.splitAt(n: Int): Pair<PList<A>, PList<A>> =
        (takeN(n) to dropN(n))



