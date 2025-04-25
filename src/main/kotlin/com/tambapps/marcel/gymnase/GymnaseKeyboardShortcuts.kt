package com.tambapps.marcel.gymnase

import com.tambapps.marcel.gymnase.controller.GymnaseController
import javafx.scene.Scene
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import javafx.scene.input.KeyEvent

object GymnaseKeyboardShortcuts {

  fun configure(scene: Scene, controller: GymnaseController) {
    val saveCombo: KeyCombination = KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN)

    scene.addEventHandler(KeyEvent.KEY_PRESSED, { event ->
      if (saveCombo.match(event)) {
        event.consume()
        controller.save()
      }
    })
  }
}