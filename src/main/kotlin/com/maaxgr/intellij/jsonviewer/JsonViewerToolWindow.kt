package com.maaxgr.intellij.jsonviewer

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin
import org.koin.dsl.module


class JsonViewerToolWindow : ToolWindowFactory, KoinComponent {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        println("Create tool window")
        val jsonViewerModule = module {
            single { project }
            single { toolWindow }
        }

        startKoin {
            printLogger()
            modules(jsonViewerModule)
        }

        val contentFactory = ContentFactory.SERVICE.getInstance()
        val content = contentFactory.createContent(JsonViewerPanel(), "", false)

        toolWindow.contentManager.addContent(content)
    }

}
