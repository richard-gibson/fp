package json


import json.instances.eqv
import json.instances.jsstring.eq.eq
import json.instances.show

fun main(args: Array<String>) {

    val arr1 = JsArray(
            listOf(JsString("bop1"),
                    JsString("bop2"),
                    JsString("bop3"),
                    JsNumber(2.0)))
    val sampleJson = JsObject(mapOf(
            "foo" to JsNull(),
            "bar" to JsString("barstr"),
            "baz" to JsBoolean(true),
            "bop" to JsArray(
                    listOf(JsString("bop1"),
                            JsString("bop2"),
                            JsString("bop3"),
                            JsNumber(2.0)))))

}