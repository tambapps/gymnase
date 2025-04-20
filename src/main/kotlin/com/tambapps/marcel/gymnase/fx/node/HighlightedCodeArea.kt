package com.tambapps.marcel.gymnase.fx.node

import com.tambapps.marcel.gymnase.data.ProgrammingLanguage
import com.tambapps.marcel.gymnase.fx.highlight.CodeHighlighterFactory
import com.tambapps.marcel.gymnase.service.PreferencesManager
import javafx.concurrent.Task
import org.fxmisc.richtext.CodeArea
import org.fxmisc.richtext.LineNumberFactory
import org.fxmisc.richtext.model.StyleSpans
import org.reactfx.Subscription
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import java.io.Closeable
import java.time.Duration
import java.util.*
import java.util.concurrent.ExecutorService

@Component
class HighlightedCodeArea(
  @Qualifier("codeHighlightingExecutor") private val executor: ExecutorService,
  codeHighlighterFactory: CodeHighlighterFactory,
  private val preferencesManager: PreferencesManager
): CodeArea(), Closeable {

  private var cleanupWhenDone: Subscription
  private val highlighter = codeHighlighterFactory.create(ProgrammingLanguage.MARCEL)

  init {
    initStyle()
    paragraphGraphicFactory = LineNumberFactory.get(this)
    replaceText("fun void main() {\n    println(\"Hello, Marcel!\")\n}")
    applyHighlighting(highlighter.highlight(text))

    cleanupWhenDone = multiPlainChanges()
      .successionEnds(Duration.ofMillis(preferencesManager.highlightDelayMillis))
      .retainLatestUntilLater(executor)
      .supplyTask(this::computeHighlightingAsync)
      .awaitLatest(multiPlainChanges())
      .filterMap { t ->
        if (t.isSuccess)
          Optional.of(t.get())
        else {
          t.failure.printStackTrace()
          Optional.empty()
        }
      }
      .subscribe(this::applyHighlighting)
  }

  private fun computeHighlightingAsync(): Task<StyleSpans<List<String>>> {
    val text: String = text ?: ""
    val task: Task<StyleSpans<List<String>>> =
      object : Task<StyleSpans<List<String>>>() {
        protected override fun call(): StyleSpans<List<String>> {
          return highlighter.highlight(text)
        }
      }
    executor.execute(task)
    return task
  }

  private fun applyHighlighting(highlighting: StyleSpans<List<String>>) {
    setStyleSpans(0, highlighting)
  }

  private fun initStyle() {
    style = "-fx-font-size: ${preferencesManager.fontSize}px;"
  }

  override fun close() {
    cleanupWhenDone.unsubscribe()
    executor.shutdown()
  }
}