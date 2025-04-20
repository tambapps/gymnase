package com.tambapps.marcel.gymnase.configuration

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@EnableConfigurationProperties(SceneProperties::class)
@Configuration
class EditorViewConfiguration