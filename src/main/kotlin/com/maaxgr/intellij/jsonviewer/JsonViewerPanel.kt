package com.maaxgr.intellij.jsonviewer

import com.intellij.openapi.project.Project
import com.intellij.ui.JBColor
import com.maaxgr.intellij.jsonviewer.components.MEditor
import com.maaxgr.intellij.jsonviewer.components.MSearch
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.awt.BorderLayout
import java.awt.Color
import java.awt.GridLayout
import java.awt.event.KeyEvent
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel


class JsonViewerPanel(project: Project) : JPanel(BorderLayout()) {

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
        lblError.foreground = JBColor.RED
        val lblPanel = JPanel().apply {
            layout = GridLayout()
            add(lblError)
        }

        //textarea in scroll pane
        editor = MEditor(
            filterShortcutTriggered = ::handleFilterShortcutTriggered,
            project = project
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

    private fun handleFilterShortcutTriggered() {
        search.visible = true
        search.focus()
    }

    private fun handleFormatClick() {
        val formatText = editor.content

        lblError.text = ""

        val newText = formatText.replace("(\\n)".toRegex(), "")

        if (newText.isBlank()) {
            lblError.text = "Empty JSON text"
            return
        }

        try {
            Json.parseToJsonElement(newText)
        } catch (ee: SerializationException) {
            lblError.text = "Invalid JSON text"
        }

        val jsonFormatter = JsonFormatter()
        val formattedText = jsonFormatter.format(newText)
        editor.content = formattedText
        editor.focus()
        editor.setCaretStart()
    }
}
