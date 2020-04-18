package com.maaxgr.intellij.jsonviewer.components

import com.intellij.codeInsight.highlighting.HighlightManager
import com.intellij.codeInsight.highlighting.HighlightManagerImpl
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.IdeActions
import com.intellij.openapi.actionSystem.ToggleAction
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.LogicalPosition
import com.intellij.openapi.editor.ScrollType
import com.intellij.openapi.editor.colors.EditorColors
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import com.intellij.openapi.editor.impl.DocumentImpl
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.text.StringUtil
import com.intellij.openapi.wm.IdeFocusManager
import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.inject


class MEditor(
    val filterShortcutTriggered: (Boolean) -> Unit = {}
) : KoinComponent {

    //global
    private val project: Project = get()

    //component
    private val editor = EditorFactory.getInstance().createEditor(DocumentImpl(""))
    val jComponent = editor.component

    init {
        editor.headerComponent = null
    }

    //state
    var content
        set(value) {
            WriteCommandAction.runWriteCommandAction(project) {
                editor.document.setText(value)
            }
        }
        get() = editor.document.text

    var highlightText = ""
        set(value) {
            field = value
            refreshHighlighting()
        }

    fun setCaretStart() {
        val caret = editor.caretModel.primaryCaret
        caret.moveToLogicalPosition(LogicalPosition(0, 0))
    }

    fun focus() {
        IdeFocusManager.getGlobalInstance().doWhenFocusSettlesDown {
            IdeFocusManager.getGlobalInstance().requestFocus(editor.contentComponent, true)
        }
        editor.scrollingModel.scrollToCaret(ScrollType.RELATIVE)
    }

    init {
        //refresh highlighting on document change
        editor.document.addDocumentListener(object : DocumentListener {
            override fun documentChanged(event: DocumentEvent) {
                println("document changed: before: '${event.oldFragment}' to '${event.newFragment}' ")

                if (event.newFragment != "") {
                    ApplicationManager.getApplication().invokeLater {
                        refreshHighlighting()
                    }
                }
            }
        })

        val filterAction = FilterAction()
        filterAction.registerCustomShortcutSet(ActionManager.getInstance().getAction(IdeActions.ACTION_FIND).shortcutSet, editor.component)
    }

    private fun refreshHighlighting() {
        val highlightManager = HighlightManager.getInstance(project) as HighlightManagerImpl
        val colorManager = EditorColorsManager.getInstance()

        //remove all highlights
        highlightManager.getHighlighters(editor).forEach { highlightManager.removeSegmentHighlighter(editor, it) }

        if (highlightText.isEmpty()) {
            return
        }

        val attributes = colorManager.globalScheme.getAttributes(EditorColors.TEXT_SEARCH_RESULT_ATTRIBUTES)
        val documentText = editor.document.text

        var i = -1
        while (true) {
            val nextOccurrence: Int = StringUtil.indexOfIgnoreCase(documentText, highlightText, i + 1)
            if (nextOccurrence < 0) {
                break
            }
            i = nextOccurrence

            highlightManager.addOccurrenceHighlight(editor, i, i + highlightText.length, attributes,
                HighlightManager.HIDE_BY_TEXT_CHANGE, null, null)
        }
    }

    private inner class FilterAction : ToggleAction() {

        override fun isSelected(e: AnActionEvent): Boolean {
            return false
        }

        override fun setSelected(e: AnActionEvent, state: Boolean) {
            filterShortcutTriggered(state)
        }
    }

}
