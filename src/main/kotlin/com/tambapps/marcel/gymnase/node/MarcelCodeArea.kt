package com.tambapps.marcel.gymnase.node

import com.tambapps.marcel.gymnase.service.MarcelCodeHighlighter
import com.tambapps.marcel.gymnase.service.PreferencesManager
import javafx.concurrent.Task
import org.fxmisc.richtext.CodeArea
import org.fxmisc.richtext.LineNumberFactory
import org.fxmisc.richtext.model.StyleSpans
import org.reactfx.Subscription
import java.io.Closeable
import java.time.Duration
import java.util.*
import java.util.concurrent.Executors

class MarcelCodeArea(
  private val highlighter: MarcelCodeHighlighter
): CodeArea(), Closeable {

  private val executor = Executors.newSingleThreadExecutor()
  private var cleanupWhenDone: Subscription
  private var lastLine = -1

  init {
    initStyle()
    paragraphGraphicFactory = LineNumberFactory.get(this)
    replaceText("fun void main() {\n    println(\"Hello, Marcel!\")\n}")
    applyHighlighting(highlighter.highlight(text))
    setupCurrentLineHighlight()
    cleanupWhenDone = setupCodeHighlight()
  }

  private fun setupCodeHighlight(): Subscription {
    return multiPlainChanges()
      .successionEnds(Duration.ofMillis(PreferencesManager.highlightDelayMillis))
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
    style = "-fx-font-size: ${PreferencesManager.fontSize}px;"
  }

  private fun setupCurrentLineHighlight() {
    caretPositionProperty().addListener { _, _, _ ->
      val currentParagraph = currentParagraph

      if (currentParagraph != lastLine) {
        // Remove highlight from previous line
        if (lastLine >= 0 && lastLine < paragraphs.size) {
          setParagraphStyle(lastLine, emptyList())
        }

        // Add style to current line
        setParagraphStyle(currentParagraph, listOf("current-line"))
        lastLine = currentParagraph
      }
    }
  }

  override fun close() {
    cleanupWhenDone.unsubscribe()
    executor.shutdown()
  }
}