package com.tambapps.marcel.gymnase.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Configuration
class EditorViewConfiguration {

  @Bean
  fun codeHighlightingExecutor(): ExecutorService = Executors.newSingleThreadExecutor()
}