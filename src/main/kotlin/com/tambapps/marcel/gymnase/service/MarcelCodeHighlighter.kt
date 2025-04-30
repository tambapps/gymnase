package com.tambapps.marcel.gymnase.service

import com.tambapps.marcel.repl.MarcelReplCompiler
import com.tambapps.marcel.repl.console.AbstractHighlighter
import com.tambapps.marcel.repl.console.HighlightTheme
import org.fxmisc.richtext.model.StyleSpans
import org.fxmisc.richtext.model.StyleSpansBuilder

class MarcelCodeHighlighter(replCompiler: MarcelReplCompiler) :
  AbstractHighlighter<StyleSpans<List<String>>, StyleSpansBuilder<List<String>>, List<String>>(
    replCompiler,
    THEME
  ) {

  companion object {
    const val DEFAULT_CLASS = "default"
    val THEME = HighlightTheme(
      field = listOf(DEFAULT_CLASS, "variable"),
      string = listOf(DEFAULT_CLASS, "string"),
      stringTemplate = listOf(DEFAULT_CLASS, "string_template"),
      default = listOf(DEFAULT_CLASS),
      function = listOf(DEFAULT_CLASS, "function"),
      keyword = listOf(DEFAULT_CLASS, "keyword"),
      comment = listOf(DEFAULT_CLASS, "comment"),
      number = listOf(DEFAULT_CLASS, "number"),
      variable = listOf(DEFAULT_CLASS),
      annotation = listOf(DEFAULT_CLASS, "annotation"),
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