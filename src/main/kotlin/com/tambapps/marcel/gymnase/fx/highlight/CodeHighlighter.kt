package com.tambapps.marcel.gymnase.fx.highlight

import org.fxmisc.richtext.model.StyleSpans

interface CodeHighlighter {

  /**
   * Highlights the code by assigning HTML classe(s) to the diffrent text parts.
   *
   * @param text the input text to highlight
   * @return the style spans
   */
  fun highlight(text: CharSequence): StyleSpans<List<String>>

}