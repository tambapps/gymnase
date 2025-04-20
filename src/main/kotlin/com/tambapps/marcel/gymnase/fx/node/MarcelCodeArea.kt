package com.tambapps.marcel.gymnase.fx.node

import org.fxmisc.richtext.CodeArea
import org.fxmisc.richtext.LineNumberFactory
import org.springframework.stereotype.Component

@Component
class MarcelCodeArea: CodeArea() {

  init {
    paragraphGraphicFactory = LineNumberFactory.get(this)
    replaceText("fun main() {\n    println(\"Hello, Marcel!\")\n}")
  }
}