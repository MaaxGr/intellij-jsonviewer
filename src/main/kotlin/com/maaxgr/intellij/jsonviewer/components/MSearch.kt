package com.maaxgr.intellij.jsonviewer.components

import com.intellij.CommonBundle
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.IdeActions
import com.intellij.openapi.actionSystem.ToggleAction
import com.intellij.ui.DocumentAdapter
import com.intellij.ui.SearchTextField
import com.maaxgr.intellij.jsonviewer.MComponent
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.event.DocumentEvent

class MSearch(
    private val textChange: (String) -> Unit = {},
    private val escapeAction: () -> Unit = {}
) : MComponent {

    private val filterPanel: JPanel = JPanel(BorderLayout())
    private val searchTextField: SearchTextField = SearchTextField()

    var text: String
        get() = searchTextField.text
        set(value) {
            searchTextField.text = value
        }

    var visible: Boolean
        get() = filterPanel.isVisible
        set(value) {
            filterPanel.isVisible = value
        }

    override val jComponent: JComponent
        get() = filterPanel

    init {
        //init search text field
        searchTextField.addDocumentListener(object : DocumentAdapter() {
            override fun textChanged(e: DocumentEvent) {
                textChange(searchTextField.text)
            }
        })

        //init filter panel
        filterPanel.add(JLabel(CommonBundle.message("label.filter") + ":"), BorderLayout.WEST);
        filterPanel.add(searchTextField);
        filterPanel.isVisible = false
        filterPanel.maximumSize = Dimension(filterPanel.maximumSize.width, 80)

        val filterAction = EscapeHandler()
        filterAction.registerCustomShortcutSet(
            ActionManager.getInstance().getAction(IdeActions.ACTION_EDITOR_ESCAPE).shortcutSet,
            searchTextField
        )
    }

    fun focus() {
        searchTextField.requestFocus()
    }

    //probably not the right action i guess?
    private inner class EscapeHandler : ToggleAction() {

        override fun isSelected(e: AnActionEvent): Boolean {
            return false
        }

        override fun setSelected(e: AnActionEvent, state: Boolean) {
            escapeAction()
        }
    }

}
