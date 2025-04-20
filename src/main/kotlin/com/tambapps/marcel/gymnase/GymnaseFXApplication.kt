package com.tambapps.marcel.gymnase

import com.tambapps.marcel.gymnase.configuration.SceneProperties
import com.tambapps.marcel.gymnase.fx.node.MarcelCodeArea
import javafx.application.Application
import javafx.application.Platform
import javafx.scene.Scene
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.ApplicationEvent
import org.springframework.context.ApplicationListener
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component

class GymnaseFXApplication: Application() {
	private lateinit var springContext: ConfigurableApplicationContext

	override fun init() {
		springContext = SpringApplicationBuilder(GymnaseApplication::class.java).run(*parameters.raw.toTypedArray())
	}

	override fun stop() {
		springContext.close()
		Platform.exit()
	}

	override fun start(primaryStage: Stage) {
		// needed because spring context is not ready yet here
		springContext.publishEvent(StageReadyEvent(primaryStage))
	}
}

@Component
class PrimaryStageInitializer(
	private val sceneProperties: SceneProperties,
	private val codeArea: MarcelCodeArea,
	@Value("classpath:styles/java-keywords.css")
	private val javaStyleSheet: Resource
): ApplicationListener<StageReadyEvent> {

	override fun onApplicationEvent(event: StageReadyEvent) {
		val stage = event.stage
		val root = StackPane(codeArea)
		val scene = Scene(root, sceneProperties.width, sceneProperties.height)
		stage.scene = scene
		stage.title = sceneProperties.name

		scene.stylesheets.add(javaStyleSheet.url.toExternalForm());

		stage.show()
	}
}


class StageReadyEvent(stage: Stage) : ApplicationEvent(stage) {
	val stage: Stage
		get() = getSource() as Stage
}