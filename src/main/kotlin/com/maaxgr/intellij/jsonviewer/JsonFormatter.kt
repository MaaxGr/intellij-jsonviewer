package com.maaxgr.intellij.jsonviewer

import com.maaxgr.intellij.jsonviewer.util.StringReader

class JsonFormatter {

    companion object {
        private const val INDENT_SPACE = 2
        private val ADD_INDENT_TRIGGERS = arrayOf('[', '{')
        private val REMOVE_INDENT_TRIGGERS = arrayOf(']', '}')
    }

    fun format(json: String) = linuxNewlines(formatResettedString(resetString(json)))

    fun linuxNewlines(json: String): String {
        return json.replace(Regex("\\r\\n|\\r|\\n"), "\n")
    }

    fun resetString(json: String): String {
        if (json.isEmpty()) {
            return json
        }

        //remove newline
        val jsonWithoutNewline = json.replace(Regex("\\r\\n|\\r|\\n"), "")

        //remove not needed spaces
        val jsonWithoutSpaces = StringBuilder()
        var inValue = false

        fun StringReader.handleChar() {
            if (current == '"' && previous != '\\') {
                inValue = !inValue
            }

            if (current == ' ') {
                if (inValue) {
                    jsonWithoutSpaces.append(current)
                }
                return
            }

            jsonWithoutSpaces.append(current)
        }

        StringReader(jsonWithoutNewline).apply {
            while (hasNext()) {
                toNext()
                handleChar()
            }
        }

        return jsonWithoutSpaces.toString()
    }

    fun formatResettedString(text: String): String {
        if (text.isEmpty()) {
            return ""
        }

        val formattedText = StringBuilder()
        var indentCount = 0
        var inValue = false

        fun StringReader.handleChar() {
            if (current == '"' && previous != '\\') {
                inValue = !inValue
            }

            if (!inValue) {
                when {
                    ADD_INDENT_TRIGGERS.contains(current) -> {
                        formattedText.appendLine(current)
                        indentCount += 1
                        appendIndent(formattedText, indentCount)
                        return
                    }
                    current == ',' -> {
                        formattedText.appendLine(current)
                        appendIndent(formattedText, indentCount)
                        return
                    }
                    current == ':' -> {
                        formattedText.append(": ")
                        return
                    }
                    REMOVE_INDENT_TRIGGERS.contains(current) -> {
                        formattedText.appendLine()
                        indentCount -= 1
                        appendIndent(formattedText, indentCount)
                        formattedText.append(current)
                        return
                    }
                }
            }

            formattedText.append(current)
        }

        StringReader(text).apply {
            while (hasNext()) {
                toNext()
                handleChar()
            }
        }

        return formattedText.toString()
    }

    private fun appendIndent(text: StringBuilder, indentCount: Int) {
        text.append(" ".repeat(indentCount * INDENT_SPACE))
    }

}
