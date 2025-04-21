package com.tambapps.marcel.gymnase

import com.tambapps.marcel.gymnase.service.MarcelCodeAreaFactory
import javafx.application.Platform
import javafx.collections.ListChangeListener
import javafx.fxml.FXML
import javafx.scene.control.MenuBar
import javafx.scene.control.Tab
import javafx.scene.control.TabPane


class GymnaseController: ListChangeListener<Tab> {
  @FXML
  private lateinit var tabPane: TabPane
  @FXML
  private lateinit var menuBar: MenuBar

  @FXML
  fun initialize() {
    updateTabHeaderVisibility()
    tabPane.tabs.addListener(this)
    val tab = tabPane.tabs.first()
    tab.content = MarcelCodeAreaFactory.create()
  }

  @FXML
  fun close() {
    Platform.exit()
  }

  private fun updateTabHeaderVisibility() {
    if (tabPane.tabs.size <= 1) {
      tabPane.styleClass.add("hide-tab-header")
    } else {
      tabPane.styleClass.remove("hide-tab-header")
    }
  }

  override fun onChanged(change: ListChangeListener.Change<out Tab>) = updateTabHeaderVisibility()
}