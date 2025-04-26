package com.tambapps.marcel.gymnase.controller

import com.tambapps.marcel.gymnase.node.MarcelCodeArea
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.fxml.FXML
import javafx.scene.control.Tab
import javafx.stage.Stage
import java.io.File

class MarcelCodeAreaController {

  private val stage: Stage
    get() = codeArea.scene.window as Stage
  private lateinit var tab: Tab
  @FXML
  private lateinit var codeArea: MarcelCodeArea
  private val fileProperty = SimpleObjectProperty<File?>()
  private var dirty = false

  fun initialize(tab: Tab, file: File? = null) {
    this.tab = tab
    fileProperty.addListener { _, _, newFile -> updateTabText(file = newFile) }

    if (file != null) {
      fileProperty.value = file
      codeArea.replaceText(file.readText())
      listenTextChanges()
    } else {
      updateTabText()
    }
  }

  fun showLinesNumber(enabled: Boolean) {
    codeArea.showLinesNumber(enabled)
  }

  private fun listenTextChanges() {
    codeArea.textProperty().apply {
      addListener(TextChangedListener(this))
    }
  }

  private fun updateTabText(file: File? = fileProperty.value) {
    tab.text = when {
      file != null && dirty -> "${file.name}*"
      file != null && !dirty -> "${file.name}"
      else -> "Untitled"
    }
  }

  fun save() {
    val file = fileProperty.value
    if (file != null) {
      file.writeText(codeArea.text)
      dirty = false
      updateTabText(file)
      listenTextChanges()
    } else {
      val file = GymnaseController.newFileChooser().showSaveDialog(stage) ?: return
      fileProperty.value = file
      file.writeText(codeArea.text)
      listenTextChanges()
      dirty = false
    }
  }

  private inner class TextChangedListener<T>(private val property: ObservableValue<T>): ChangeListener<T> {
    override fun changed(
      observable: ObservableValue<out T>,
      oldValue: T,
      newValue: T
    ) {
      val wasEdited = oldValue != newValue
      if (wasEdited) {
        dirty = true
        updateTabText()
        // removing to avoid keep listening for nothing
        property.removeListener(this)
      }
    }
  }
}