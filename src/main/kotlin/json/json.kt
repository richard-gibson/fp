package json

sealed class Json
data class JsString(val value: String): Json()
data class JsBoolean(val value: Boolean): Json()
data class JsNumber(val value: Double): Json()
data class JsArray(val value: List<Json>): Json()
data class JsObject(val value: Map<String, Json>): Json()
object JsNull: Json()


fun Json.stringify(): String = when(this) {
    is JsString  -> "\"$value\""
    is JsNumber  -> value.toString()
    is JsBoolean -> value.toString()
    is JsNull    -> "null"
    is JsArray   -> "[${value.map{it.stringify()}.joinToString()}]"
    is JsObject  -> "{${value.map { (k,v) -> "\"$k\" : ${v.stringify()}" }.joinToString()}}"
}

fun main(args: Array<String>) {
    println(
            JsObject(mapOf(
            "foo" to JsNull,
            "bar" to JsString("barstr"),
            "baz" to JsBoolean(true),
            "bop" to JsArray(
                    listOf(JsString("bop1"),
                            JsString("bop2"),
                            JsString("bop3"),
                            JsNumber(2.0)))
            )).stringify())
}