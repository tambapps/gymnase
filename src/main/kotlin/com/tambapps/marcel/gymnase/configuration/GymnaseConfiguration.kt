package com.tambapps.marcel.gymnase.configuration

import com.tambapps.marcel.gymnase.GymnaseFXApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.prefs.Preferences

@Configuration
class GymnaseConfiguration {

  @Bean
  fun codeHighlightingExecutor(): ExecutorService = Executors.newSingleThreadExecutor()

  @Bean
  fun preferences(): Preferences {
    return Preferences.userNodeForPackage(GymnaseFXApplication::class.java)
  }
}