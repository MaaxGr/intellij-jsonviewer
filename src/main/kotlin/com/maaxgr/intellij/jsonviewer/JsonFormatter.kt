package com.maaxgr.intellij.jsonviewer

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class JsonFormatter {

    @OptIn(ExperimentalSerializationApi::class)
    fun format(jsonString: String): String {
        val json = Json {
            prettyPrint = true
            prettyPrintIndent = "  "
        }
        val jsonElement = Json.parseToJsonElement(jsonString)

        return json.encodeToString(jsonElement)
    }


}
