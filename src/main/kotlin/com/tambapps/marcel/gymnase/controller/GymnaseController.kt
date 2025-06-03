package com.tambapps.marcel.gymnase.controller

import com.tambapps.marcel.gymnase.GymnaseApplication
import com.tambapps.marcel.gymnase.node.MarcelCodeArea
import com.tambapps.marcel.gymnase.service.PreferencesManager
import javafx.application.Platform
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.CheckMenuItem
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
  private lateinit var showLinesNumberMenuItem: CheckMenuItem
  @FXML
  private lateinit var highlightSelectedLineMenuItem: CheckMenuItem
  private val isStageReady get() = tabPane.scene != null
  private val stage: Stage
    get() = tabPane.scene.window as Stage
  private val currentTab: Tab
    get() = tabPane.selectionModel.selectedItem
  private val tabControllerMap: MutableMap<Tab, MarcelCodeAreaController> = mutableMapOf()

  @FXML
  fun initialize() {
    tabPane.tabs.addListener(this)
    showLinesNumberMenuItem.isSelected = PreferencesManager.showLinesNumberProperty.value
    highlightSelectedLineMenuItem.isSelected = PreferencesManager.highlightSelectedLineProperty.value
    newTab()
    updateTabHeaderVisibility(tabPane.tabs)
  }

  @FXML
  private fun onShowLinesNumber() {
    PreferencesManager.showLinesNumberProperty.value = showLinesNumberMenuItem.isSelected
  }

  @FXML
  private fun onHighlightSelectedLine() {
    PreferencesManager.highlightSelectedLineProperty.value = highlightSelectedLineMenuItem.isSelected
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
    addTab(createTab())
  }

  @FXML
  fun openFile() {
    val fileChooser = newFileChooser()
    val file = fileChooser.showOpenDialog(stage) ?: return
    val newTab = createTab(file)
    addTab(newTab)
    tabPane.selectionModel.select(newTab)
  }

  private fun addTab(tab: Tab) {
    if (tabPane.tabs.size == 1 && tabPane.tabs.first() == currentTab
      && tabControllerMap.getValue(currentTab).text.isBlank()) {
      removeTab(currentTab)
    }
    tabPane.tabs.add(tab)
    if (!isStageReady) {
      return
    }
    if (tabPane.tabs.size == 1) {
      val fileName = tabControllerMap[tab]?.fileName
      stage.title = fileName ?: "Gymnase"
    } else {
      stage.title = "Gymnase"
    }
  }

  @FXML
  fun close() {
    if (tabPane.tabs.size <= 1) {
      Platform.exit()
    } else {
      removeTab(currentTab)
    }
  }

  private fun removeTab(tab: Tab) {
    tabControllerMap.remove(tab)
    tabPane.tabs.remove(tab)
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