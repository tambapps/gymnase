package com.tambapps.marcel.gymnase.fx.highlight

import org.fxmisc.richtext.model.StyleSpans
import org.fxmisc.richtext.model.StyleSpansBuilder
import java.util.Collections
import java.util.regex.Matcher
import java.util.regex.Pattern

class JavaCodeHighlighter: CodeHighlighter {

  private companion object {
    private val KEYWORDS: Array<String> = arrayOf<String>(
      "abstract", "assert", "boolean", "break", "byte",
      "case", "catch", "char", "class", "const",
      "continue", "default", "do", "double", "else",
      "enum", "extends", "final", "finally", "float",
      "for", "goto", "if", "implements", "import",
      "instanceof", "int", "interface", "long", "native",
      "new", "package", "private", "protected", "public",
      "return", "short", "static", "strictfp", "super",
      "switch", "synchronized", "this", "throw", "throws",
      "transient", "try", "void", "volatile", "while"
    )

    private val KEYWORD_PATTERN: String = "\\b(" + java.lang.String.join("|", *KEYWORDS) + ")\\b"
    private const val PAREN_PATTERN: String = "\\(|\\)"
    private const val BRACE_PATTERN: String = "\\{|\\}"
    private const val BRACKET_PATTERN: String = "\\[|\\]"
    private const val SEMICOLON_PATTERN: String = "\\;"
    private const val STRING_PATTERN: String = "\"([^\"\\\\]|\\\\.)*\""
    private const val COMMENT_PATTERN: String = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/"

    private val PATTERN: Pattern = Pattern.compile(
      ("(?<KEYWORD>" + KEYWORD_PATTERN + ")"
          + "|(?<PAREN>" + PAREN_PATTERN + ")"
          + "|(?<BRACE>" + BRACE_PATTERN + ")"
          + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
          + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
          + "|(?<STRING>" + STRING_PATTERN + ")"
          + "|(?<COMMENT>" + COMMENT_PATTERN + ")")
    )
  }

  override fun highlight(text: String): StyleSpans<List<String>> {
    val matcher: Matcher = PATTERN.matcher(text)
    var lastKwEnd = 0
    val spansBuilder = StyleSpansBuilder<List<String>>()
    while (matcher.find()) {
      val styleClass: String = checkNotNull(
        if (matcher.group("KEYWORD") != null) "keyword" else if (matcher.group("PAREN") != null) "paren" else if (matcher.group(
            "BRACE"
          ) != null
        ) "brace" else if (matcher.group("BRACKET") != null) "bracket" else if (matcher.group("SEMICOLON") != null) "semicolon" else if (matcher.group(
            "STRING"
          ) != null
        ) "string" else if (matcher.group("COMMENT") != null) "comment" else null
      ) /* never happens */
      spansBuilder.add(emptyList(), matcher.start() - lastKwEnd)
      spansBuilder.add(listOf(styleClass), matcher.end() - matcher.start())
      lastKwEnd = matcher.end()
    }
    spansBuilder.add(Collections.emptyList(), text.length - lastKwEnd)
    return spansBuilder.create()
  }
}