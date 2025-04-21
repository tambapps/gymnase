package com.tambapps.marcel.gymnase

import com.tambapps.marcel.gymnase.service.PreferencesManager
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

class GymnaseApplication : Application() {

  private lateinit var stage: Stage

  override fun start(stage: Stage) {
    val root = FXMLLoader.load<Parent>(GymnaseApplication::class.java.getResource("gymnase-view.fxml"))
    val (width, height) = PreferencesManager.sceneSize
    val scene = Scene(root, width, height)
    stage.title = "Gymnase"
    stage.scene = scene
    stage.show()
    this.stage = stage
  }

  override fun stop() {
    PreferencesManager.sceneSize = stage.width to stage.height
  }
}

fun main() {
  Application.launch(GymnaseApplication::class.java)
}