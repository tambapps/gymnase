package com.tambapps.marcel.gymnase.service

import com.tambapps.marcel.compiler.CompilerConfiguration
import com.tambapps.marcel.gymnase.node.MarcelCodeArea
import com.tambapps.marcel.repl.MarcelReplCompiler
import com.tambapps.marcel.repl.ReplMarcelSymbolResolver
import marcel.lang.URLMarcelClassLoader

object MarcelCodeAreaFactory {

  val COMPILER_CONFIGURATION = CompilerConfiguration(dumbbellEnabled = true)

  fun create(): MarcelCodeArea = MarcelCodeArea(createHighlighter())

  private fun createHighlighter(): MarcelCodeHighlighter {
    val classLoader = URLMarcelClassLoader()
    val symbolResolver = ReplMarcelSymbolResolver(classLoader)
    val replCompiler = MarcelReplCompiler(COMPILER_CONFIGURATION, classLoader, symbolResolver)
    return MarcelCodeHighlighter(replCompiler)
  }
}