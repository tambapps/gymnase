package com.tambapps.marcel.gymnase.service

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object ExecutorServiceFactory {

  private val executors = mutableListOf<ExecutorService>()

  fun newSingleThreadExecutor(): ExecutorService {
    val executor = Executors.newSingleThreadExecutor()
    executors.add(executor)
    return executor
  }

  fun dispose() {
    executors.forEach { it.shutdown() }
    executors.clear()
  }
}