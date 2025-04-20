package com.tambapps.marcel.gymnase.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("gymnase.fx")
data class SceneProperties(
  val name: String,
  val width: Double = -1.0,
  val height: Double = -1.0,
)
