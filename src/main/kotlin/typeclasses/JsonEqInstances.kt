package json.instances

import arrow.extension
import arrow.typeclasses.Eq
import json.*
import json.instances.jsarray.eq.eq
import json.instances.jsstring.eq.eq
import json.instances.jsnumber.eq.eq
import json.instances.jsboolean.eq.eq
import json.instances.jsnull.eq.eq
import json.instances.jsobject.eq.eq
import json.instances.json.eq.eq

/**
 * create a Eq<Json> instance
 */
@extension
interface JsStringEq: Eq<JsString> {
    override fun JsString.eqv(b: JsString): Boolean =
            this.value == b.value

}

@extension
interface JsNumberEq: Eq<JsNumber> {
    override fun JsNumber.eqv(b: JsNumber): Boolean =
            this.value == b.value

}

@extension
interface JsBooleanEq: Eq<JsBoolean> {
    override fun JsBoolean.eqv(b: JsBoolean): Boolean =
            this.value == b.value

}

@extension
interface JsNullEq: Eq<JsNull> {
    override fun JsNull.eqv(b:JsNull): Boolean = true
}

@extension
interface  JsonEq: Eq<Json> {
    override fun Json.eqv(b: Json): Boolean = when {
        this is JsString && b is JsString -> with(JsString.eq())  { this@eqv.eqv(b) }
        this is JsBoolean && b is JsBoolean -> with(JsBoolean.eq()) { this@eqv.eqv(b) }
        this is JsNumber && b is JsNumber -> with(JsNumber.eq()) { this@eqv.eqv(b) }
        this is JsNull && b is JsNull -> with(JsNull.eq()) { this@eqv.eqv(b) }
        this is JsArray && b is JsArray -> with(JsArray.eq(this@JsonEq)) { this@eqv.eqv(b) }
        this is JsObject && b is JsObject -> with(JsObject.eq( this@JsonEq)) { this@eqv.eqv(b) }
        else -> false
    }
}



//for usablitiy create a show extension function for the json object
fun Json.eqv(b: Json) = with(Json.eq()) { this@eqv.eqv(b) }


@extension
interface JsArrayEQ : Eq<JsArray> {
    fun EQ(): Eq<Json>
    override fun JsArray.eqv(b: JsArray): Boolean =
        value.size == b.value.size &&
        value.zip(b.value).filter { (a, b) ->
            with(EQ()) {
                a.eqv(b)
            }
        }.size == value.size

}

@extension
interface JsObjectEq: Eq<JsObject> {
    fun EQ(): Eq<Json>

    override fun JsObject.eqv(b: JsObject): Boolean =
        value.size == b.value.size &&
        value.toList().zip(b.value.toList()).filter {
            (left, right) ->
                left.first == right.first &&
                with(EQ()){ left.second.eqv(right.second) }
        }.size == value.size

}
