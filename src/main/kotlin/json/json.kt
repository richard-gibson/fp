package json

sealed class Json {
    companion object
}
data class JsString(val value: String): Json(){
    companion object
}
data class JsBoolean(val value: Boolean): Json(){
    companion object
}
data class JsNumber(val value: Double): Json() {
    companion object
}
data class JsArray(val value: List<Json>): Json(){
    companion object
}
data class JsObject(val value: Map<String, Json>): Json(){
    companion object
}
class JsNull: Json() {
    companion object
}


fun Json.stringify(): String = when(this) {
    is JsString  -> "\"$value\""
    is JsNumber  -> value.toString()
    is JsBoolean -> value.toString()
    is JsNull    -> "null"
    is JsArray   -> "[${value.map{it.stringify()}.joinToString()}]"
    is JsObject  -> "{${value.map { (k,v) -> "\"$k\" : ${v.stringify()}" }.joinToString()}}"
}

