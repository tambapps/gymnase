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
import javafx.stage.FileChooser
import javafx.stage.Stage
import java.io.File

class GymnaseController: ListChangeListener<Tab> {

  companion object {
    private const val HIDE_TAB_HEADER_CLASS = "hide-tab-header"

    fun newFileChooser() = FileChooser().apply {
      title = "Open Marcel File"
      extensionFilters.addAll(
        FileChooser.ExtensionFilter("Marcel Files", "*.mcl", "*.marcel"),
        //FileChooser.ExtensionFilter("All Files", "*.*")
      )
    }
  }

  @FXML
  private lateinit var tabPane: TabPane
  @FXML
  private lateinit var menuBar: MenuBar
  private val stage: Stage
    get() = tabPane.scene.window as Stage
  private val currentTab: Tab
    get() = tabPane.selectionModel.selectedItem
  private val tabControllerMap: MutableMap<Tab, MarcelCodeAreaController> = mutableMapOf()

  @FXML
  fun initialize() {
    tabPane.tabs.addListener(this)
    newTab()
    updateTabHeaderVisibility(tabPane.tabs)
  }

  private fun createTab(file: File? = null): Tab = Tab().apply {
    text = "New file"
    val loader = FXMLLoader(GymnaseApplication::class.java.getResource("code-area-view.fxml"))
    val codeArea = loader.load<MarcelCodeArea>()
    val controller = loader.getController<MarcelCodeAreaController>()
    tabControllerMap[this] = controller
    controller.initialize(this, file)
    content = codeArea
  }

  @FXML
  fun newTab() {
    tabPane.tabs.add(createTab())
  }

  @FXML
  fun openFile() {
    val fileChooser = newFileChooser()
    val file = fileChooser.showOpenDialog(stage) ?: return
    val newTab = createTab(file)
    tabPane.tabs.add(newTab)
    tabPane.selectionModel.select(newTab)
  }

  @FXML
  fun close() {
    if (tabPane.tabs.size <= 1) {
      Platform.exit()
    } else {
      val currentTab = currentTab
      tabControllerMap.remove(currentTab)
      tabPane.tabs.remove(currentTab)
    }
  }

  @FXML
  fun save() {
    tabControllerMap[currentTab]?.save()
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