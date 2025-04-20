package com.tambapps.marcel.gymnase

import com.tambapps.marcel.gymnase.fx.StageReadyEvent
import com.tambapps.marcel.gymnase.service.PreferencesManager
import javafx.application.Application
import javafx.application.Platform
import javafx.stage.Stage
import org.springframework.beans.factory.getBean
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.ConfigurableApplicationContext

class GymnaseFXApplication: Application() {
	private lateinit var springContext: ConfigurableApplicationContext
	private lateinit var stage: Stage

	override fun init() {
		springContext = SpringApplicationBuilder(GymnaseApplication::class.java).run(*parameters.raw.toTypedArray())
	}

	override fun stop() {
		val preferencesManager = springContext.getBean<PreferencesManager>()
		preferencesManager.sceneSize = stage.width to stage.height
		springContext.close()
		Platform.exit()
	}

	override fun start(primaryStage: Stage) {
		this.stage = primaryStage
		// needed because spring context is not ready yet here
		springContext.publishEvent(StageReadyEvent(primaryStage))
	}
}
