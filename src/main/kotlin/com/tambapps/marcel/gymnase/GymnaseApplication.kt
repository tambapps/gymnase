package com.tambapps.marcel.gymnase

import javafx.application.Application
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class GymnaseApplication

fun main(args: Array<String>) {
	Application.launch(GymnaseFXApplication::class.java, *args)
}
