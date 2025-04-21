package com.tambapps.marcel.gymnase.controller

import com.tambapps.marcel.gymnase.GymnaseApplication
import com.tambapps.marcel.gymnase.node.MarcelCodeArea
import javafx.application.Platform
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.MenuBar
import javafx.scene.control.Tab
import javafx.scene.control.TabPane

class GymnaseController: ListChangeListener<Tab> {

  private companion object {
    const val HIDE_TAB_HEADER_CLASS = "hide-tab-header"
  }

  @FXML
  private lateinit var tabPane: TabPane
  @FXML
  private lateinit var menuBar: MenuBar

  @FXML
  fun initialize() {
    tabPane.tabs.addListener(this)
    newTab()
    updateTabHeaderVisibility(tabPane.tabs)
  }

  private fun createTab(): Tab = Tab().apply {
    text = "New file"
    val loader = FXMLLoader(GymnaseApplication::class.java.getResource("code-area-view.fxml"))
    val codeArea = loader.load<MarcelCodeArea>()
    val controller = loader.getController<MarcelCodeAreaController>()
    controller.tab = this
    content = codeArea
  }

  @FXML
  fun newTab() {
    tabPane.tabs.add(createTab())
  }

  @FXML
  fun close() {
    if (tabPane.tabs.size <= 1) {
      Platform.exit()
    } else {
      val currentTab = tabPane.selectionModel.selectedItem
      tabPane.tabs.remove(currentTab)
    }
  }

  private fun updateTabHeaderVisibility(tabs: ObservableList<out Tab>) {
    val styleClass = tabPane.styleClass
    if (tabs.size <= 1 && !styleClass.contains(HIDE_TAB_HEADER_CLASS)) {
      tabPane.styleClass.add(HIDE_TAB_HEADER_CLASS)
    } else if (tabs.size > 1 && styleClass.contains(HIDE_TAB_HEADER_CLASS)) {
      tabPane.styleClass.remove(HIDE_TAB_HEADER_CLASS)
    }
  }

  override fun onChanged(change: ListChangeListener.Change<out Tab>) = updateTabHeaderVisibility(change.list)
}