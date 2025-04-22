package com.tambapps.marcel.gymnase.controller

import com.tambapps.marcel.gymnase.node.MarcelCodeArea
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.fxml.FXML
import javafx.scene.control.Tab
import java.io.File

class MarcelCodeAreaController {

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
      codeArea.textProperty().apply {
        addListener(TextChangedListener(this))
      }
    } else {
      updateTabText()
    }
  }

  private fun updateTabText(file: File? = fileProperty.value) {
    tab.text = when {
      file != null && dirty -> "${file.name}*"
      file != null && !dirty -> "${file.name}"
      else -> "Untitled"
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