package com.tambapps.marcel.gymnase.fx.highlight

import com.tambapps.marcel.compiler.CompilerConfiguration
import com.tambapps.marcel.gymnase.data.ProgrammingLanguage
import com.tambapps.marcel.repl.MarcelReplCompiler
import com.tambapps.marcel.repl.ReplMarcelSymbolResolver
import marcel.lang.URLMarcelClassLoader
import org.springframework.stereotype.Service

@Service
class CodeHighlighterFactory(
  private val compilerConfiguration: CompilerConfiguration
) {

  fun create(programmingLanguage: ProgrammingLanguage) = when (programmingLanguage) {
    ProgrammingLanguage.MARCEL -> {
      val classLoader = URLMarcelClassLoader()
      val symbolResolver = ReplMarcelSymbolResolver(classLoader)
      val replCompiler = MarcelReplCompiler(compilerConfiguration, classLoader, symbolResolver)
      MarcelCodeHighlighter(replCompiler)
    }
    ProgrammingLanguage.JAVA -> JavaCodeHighlighter()
  }
}