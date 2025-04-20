package com.tambapps.marcel.gymnase

import com.tambapps.marcel.gymnase.configuration.SceneProperties
import javafx.application.Application
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties

@EnableConfigurationProperties(SceneProperties::class)
@SpringBootApplication
class GymnaseApplication

fun main(args: Array<String>) {
	Application.launch(GymnaseFXApplication::class.java, *args)
}
