package com.tambapps.marcel.gymnase.fx.highlight

import com.tambapps.marcel.repl.MarcelReplCompiler
import com.tambapps.marcel.repl.console.AbstractHighlighter
import com.tambapps.marcel.repl.console.HighlightTheme
import org.fxmisc.richtext.model.StyleSpans
import org.fxmisc.richtext.model.StyleSpansBuilder

class MarcelCodeHighlighter(replCompiler: MarcelReplCompiler) :
  AbstractHighlighter<StyleSpans<List<String>>, StyleSpansBuilder<List<String>>, List<String>>(
    replCompiler,
    THEME
  ), CodeHighlighter {

    companion object {
      val THEME = HighlightTheme(
        variable = listOf("variable"),
        string = listOf("string"),
        stringTemplate = listOf("string_template"),
        default = listOf(),
        function = listOf("function"),
        keyword = listOf("keyword"),
        comment = listOf("comment"),
        number = listOf("number"),
      )
    }
  override fun newBuilder(): StyleSpansBuilder<List<String>> = StyleSpansBuilder<List<String>>()

  override fun build(builder: StyleSpansBuilder<List<String>>): StyleSpans<List<String>> = builder.create()

  override fun highlight(
    builder: StyleSpansBuilder<List<String>>,
    style: List<String>,
    string: String
  ) {
    builder.add(style, string.length)
  }
}