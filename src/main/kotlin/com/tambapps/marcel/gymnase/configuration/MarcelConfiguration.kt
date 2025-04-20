package com.tambapps.marcel.gymnase.configuration

import com.tambapps.marcel.compiler.CompilerConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MarcelConfiguration {

  @Bean
  fun compilerConfiguration() = CompilerConfiguration(dumbbellEnabled = true)
}