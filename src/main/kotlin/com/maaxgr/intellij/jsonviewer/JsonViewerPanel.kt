package com.maaxgr.intellij.jsonviewer

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.stream.MalformedJsonException
import com.maaxgr.intellij.jsonviewer.components.MEditor
import com.maaxgr.intellij.jsonviewer.components.MSearch
import java.awt.BorderLayout
import java.awt.GridLayout
import java.awt.event.KeyEvent
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel


class JsonViewerPanel : JPanel(BorderLayout()) {

    private val btnFormat: JButton
    private val lblError: JLabel

    private val editor: MEditor
    private val search: MSearch

    init {
        //format button
        btnFormat = JButton("Format").apply {
            addActionListener { handleFormatClick() }
        }
        btnFormat.mnemonic = KeyEvent.VK_F
        val btnPanel = JPanel().apply {
            layout = GridLayout()
            add(btnFormat)
        }

        //error label
        lblError = JLabel()
        val lblPanel = JPanel().apply {
            layout = GridLayout()
            add(lblError)
        }

        //textarea in scroll pane
        editor = MEditor(
            filterShortcutTriggered = ::handleFilterShortcutTriggered
        )

        search = MSearch(
            textChange = ::handleSearchInputChange,
            escapeAction = ::handleEscapeAction
        )

        //compose all together
        val mainContentPanel = JPanel()
            .apply {
                layout = BoxLayout(this, BoxLayout.PAGE_AXIS)
                add(btnPanel)
                add(lblPanel)
                add(search.jComponent)
                add(editor.jComponent)
            }

        add(mainContentPanel, BorderLayout.CENTER)
    }

    private fun handleEscapeAction() {
        search.apply {
            visible = false
            text = ""
        }
        editor.focus()
    }

    private fun handleSearchInputChange(text: String) {
        editor.highlightText = text
    }

    private fun handleFilterShortcutTriggered(state: Boolean) {
        search.visible = state

        if (state) {
            search.focus()
        }
    }

    private fun handleFormatClick() {
        val formatText = editor.content

        lblError.text = ""

        val newText = formatText.replace("(\\n)".toRegex(), "")

        try {
            Gson().getAdapter(JsonElement::class.java).fromJson(newText)
        } catch (ee: MalformedJsonException) {
            lblError.text = "Invalid JSON text"
        }

        val jsonFormatter = JsonFormatter()
        val formattedText = jsonFormatter.format(newText)
        editor.content = formattedText
        editor.focus()
        editor.setCaretStart()
    }
}
