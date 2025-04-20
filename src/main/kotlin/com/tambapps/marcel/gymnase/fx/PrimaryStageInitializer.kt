package com.tambapps.marcel.gymnase.fx

import com.tambapps.marcel.gymnase.configuration.SceneProperties
import com.tambapps.marcel.gymnase.fx.node.HighlightedCodeArea
import com.tambapps.marcel.gymnase.service.PreferencesManager
import javafx.scene.Scene
import javafx.scene.layout.StackPane
import javafx.scene.text.Font
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component

@Component
class PrimaryStageInitializer(
	private val sceneProperties: SceneProperties,
	private val codeArea: HighlightedCodeArea,
	private val preferencesManager: PreferencesManager
): ApplicationListener<StageReadyEvent> {

	override fun onApplicationEvent(event: StageReadyEvent) {
		val stage = event.stage
		val root = StackPane(codeArea)
		val (width, height) = preferencesManager.sceneSize
		val scene = Scene(root, width, height)
		stage.scene = scene
		stage.title = sceneProperties.name
		loadFonts()
		scene.stylesheets.add(javaClass.getResource("/styles/code-area.css")!!.toExternalForm())
		stage.show()
	}

	private fun loadFonts() {
		for (fontName in listOf("JetBrainsMono-Regular.ttf", "JetBrainsMono-Bold.ttf")) {
			javaClass.getResourceAsStream("/fonts/${fontName}").use { stream ->
				Font.loadFont(stream, preferencesManager.fontSize.toDouble())
			}
		}
	}
}
