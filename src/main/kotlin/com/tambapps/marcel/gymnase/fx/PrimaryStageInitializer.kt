package com.tambapps.marcel.gymnase.fx

import com.tambapps.marcel.gymnase.configuration.SceneProperties
import com.tambapps.marcel.gymnase.fx.node.HighlightedCodeArea
import javafx.scene.Scene
import javafx.scene.layout.StackPane
import javafx.scene.text.Font
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationListener
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component

@Component
class PrimaryStageInitializer(
	private val sceneProperties: SceneProperties,
	private val codeArea: HighlightedCodeArea,
	@Value("classpath:styles/code-area.css")
	private val codeAreaStyleResource: Resource,
): ApplicationListener<StageReadyEvent> {

	override fun onApplicationEvent(event: StageReadyEvent) {
		val stage = event.stage
		val root = StackPane(codeArea)
		val scene = Scene(root, sceneProperties.width, sceneProperties.height)
		stage.scene = scene
		stage.title = sceneProperties.name
		initializeFonts()
		scene.stylesheets.add(codeAreaStyleResource.url.toExternalForm())

		stage.show()
	}

	private fun initializeFonts() {
		val regularFont = Font.loadFont(javaClass.getResourceAsStream("/fonts/JetBrainsMono-Regular.ttf"), 25.0)
		val boldFont = Font.loadFont(javaClass.getResourceAsStream("/fonts/JetBrainsMono-Bold.ttf"), 25.0)

	}
}
