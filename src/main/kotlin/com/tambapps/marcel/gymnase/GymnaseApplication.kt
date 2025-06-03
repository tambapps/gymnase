package com.tambapps.marcel.gymnase

import com.tambapps.marcel.gymnase.controller.GymnaseController
import com.tambapps.marcel.gymnase.service.ExecutorServiceFactory
import com.tambapps.marcel.gymnase.service.PreferencesManager
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage

class GymnaseApplication : Application() {

  private lateinit var stage: Stage

  override fun start(stage: Stage) {
    this.stage = stage
    val icon = Image(javaClass.getResourceAsStream("icons/app-icon.png"))
    stage.icons += icon
    val loader = FXMLLoader(GymnaseApplication::class.java.getResource("gymnase-view.fxml"))
    val root = loader.load<Parent>()
    val controller = loader.getController<GymnaseController>()
    val (width, height) = PreferencesManager.sceneSizeProperty.value
    val scene = Scene(root, width, height)
    GymnaseKeyboardShortcuts.configure(scene, controller)
    stage.title = "Gymnase"
    stage.scene = scene
    stage.show()
  }

  override fun stop() {
    if (stage.width >= 100 && stage.height >= 100) {
      PreferencesManager.sceneSizeProperty.value = stage.width to stage.height
    }
    ExecutorServiceFactory.dispose()
  }
}

fun main() {
  Application.launch(GymnaseApplication::class.java)
}