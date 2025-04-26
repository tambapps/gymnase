package com.tambapps.marcel.gymnase.node

import com.tambapps.marcel.gymnase.service.ExecutorServiceFactory
import com.tambapps.marcel.gymnase.service.MarcelCodeHighlighterFactory
import com.tambapps.marcel.gymnase.service.PreferencesManager
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.concurrent.Task
import org.fxmisc.richtext.CodeArea
import org.fxmisc.richtext.LineNumberFactory
import org.fxmisc.richtext.model.StyleSpans
import org.reactfx.Subscription
import java.time.Duration
import java.util.*

class MarcelCodeArea: CodeArea() {

  private val executor = ExecutorServiceFactory.newSingleThreadExecutor()
  private var lastLine = -1
  private val highlighter = MarcelCodeHighlighterFactory.create()
  private val currentLineHighlightListener = ChangeListener<Int> { _, _, _ ->
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


  init {
    initStyle()
    setupLinesNumberDisplay()
    replaceText("fun void main() {\n    println(\"Hello, Marcel!\")\n}")
    applyHighlighting(highlighter.highlight(text))
    setupCurrentLineHighlight()
    setupCodeHighlight()
  }

  private fun setupCodeHighlight(): Subscription {
    return multiPlainChanges()
      // flemme to listen for changes
      .successionEnds(Duration.ofMillis(PreferencesManager.highlightDelayMillisProperty.value))
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
    PreferencesManager.fontSizeProperty.addListenerNow { _, _, fontSize ->
      style = "-fx-font-size: ${fontSize}px;"
    }
    stylesheets.add(PreferencesManager.codeStyleSheet)
  }

  private fun setupCurrentLineHighlight() {

    PreferencesManager.highlightSelectedLineProperty.addListenerNow { _, _, highlightSelectedLine ->
      if (highlightSelectedLine) {
        caretPositionProperty().addListener(currentLineHighlightListener)
      } else {
        if (lastLine >= 0) {
          setParagraphStyle(lastLine, emptyList())
          lastLine = -1
        }
        caretPositionProperty().removeListener(currentLineHighlightListener)
      }
    }
  }

  fun setupLinesNumberDisplay() {
    PreferencesManager.showLinesNumberProperty.addListenerNow { _, _, showLinesNumber ->
      paragraphGraphicFactory = if (showLinesNumber) LineNumberFactory.get(this) else null
    }
  }
}