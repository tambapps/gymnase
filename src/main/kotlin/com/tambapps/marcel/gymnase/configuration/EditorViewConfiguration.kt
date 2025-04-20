package com.tambapps.marcel.gymnase.configuration

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@EnableConfigurationProperties(SceneProperties::class)
@Configuration
class EditorViewConfiguration {

  @Bean
  fun codeHighlightingExecutor(): ExecutorService = Executors.newSingleThreadExecutor()
}