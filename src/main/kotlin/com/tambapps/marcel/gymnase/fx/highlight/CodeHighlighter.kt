package com.tambapps.marcel.gymnase.fx.highlight

import org.fxmisc.richtext.model.StyleSpans

interface CodeHighlighter {

  fun highlight(text: String): StyleSpans<List<String>>

}