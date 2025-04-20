package com.tambapps.marcel.gymnase.fx

import javafx.stage.Stage
import org.springframework.context.ApplicationEvent

class StageReadyEvent(stage: Stage) : ApplicationEvent(stage) {
	val stage: Stage
		get() = getSource() as Stage
}