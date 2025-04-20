package com.tambapps.marcel.gymnase

import com.tambapps.marcel.gymnase.fx.StageReadyEvent
import javafx.application.Application
import javafx.application.Platform
import javafx.stage.Stage
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.ConfigurableApplicationContext

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
