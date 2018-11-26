package json.instances

import arrow.extension
import arrow.typeclasses.Show
import json.*
import json.instances.jsarray.show.show
import json.instances.jsboolean.show.show
import json.instances.jsnull.show.show
import json.instances.jsnumber.show.show
import json.instances.jsobject.show.show
import json.instances.json.show.show
import json.instances.jsstring.show.show




@extension
interface JsStringShow: Show<JsString> {
    override fun JsString.show(): String =
            "\"$value\""
}


@extension
interface JsNumberShow: Show<JsNumber> {
    override fun JsNumber.show(): String =
            this.value.toString()
}

@extension
interface JsBooleanShow: Show<JsBoolean> {
    override fun JsBoolean.show(): String =
            this.value.toString()
}

@extension
interface JsNullShow: Show<JsNull> {
    override fun JsNull.show(): String = "null"
}

@extension
interface  JsonShow: Show<Json> {
    override fun Json.show(): String = when(this) {
        is JsString -> with(JsString.show()) { this@show.show() }
        is JsNumber -> with(JsNumber.show()) { this@show.show() }
        is JsBoolean -> with(JsBoolean.show()) { this@show.show() }
        is JsNull -> with(JsNull.show()) { this@show.show() }
        is JsArray -> with(JsArray.show(this@JsonShow)) { this@show.show() }
        is JsObject -> with(JsObject.show(this@JsonShow)) { this@show.show() }
    }
}

//for usablitiy create a show extension function for the json object
fun Json.show() = with(Json.show()) { this@show.show() }

@extension
interface JsArrayShow : Show<JsArray> {
    fun JSSHOW(): Show<Json>
    override fun JsArray.show(): String =
            "[${value.map {
                with(JSSHOW()) { it.show() }
            }.joinToString()}]"
}

@extension
interface JsObjectShow : Show<JsObject> {
    fun JSSHOW(): Show<Json>
    override fun JsObject.show(): String =
            "{${value.map {
                with(JSSHOW()) {
                    "\"${it.key}\" : ${it.value.show()}"
                }
            }.joinToString()}}"
}
