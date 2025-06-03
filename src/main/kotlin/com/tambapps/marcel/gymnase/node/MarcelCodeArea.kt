package com.tambapps.marcel.gymnase.node

import com.tambapps.marcel.gymnase.service.ExecutorServiceFactory
import com.tambapps.marcel.gymnase.service.MarcelCodeHighlighterFactory
import com.tambapps.marcel.gymnase.service.PreferencesManager
import javafx.beans.value.ChangeListener
import javafx.concurrent.Task
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
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
      // Remove highlight from the previous line
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
    setupCurrentLineHighlight()
    setupCodeHighlight()
    addEventFilter(KeyEvent.KEY_PRESSED) { event ->
      if (event.code == KeyCode.ENTER && !event.isShiftDown) {
        autoIndentOnEnter()
        event.consume()
      }
    }
  }

  private fun autoIndentOnEnter() {
    // paragraph = line
    val currentLineText = getParagraph(currentParagraph).text

    // Find leading spaces or tabs on current line
    var leadingSpacesCount = 0
    while (leadingSpacesCount < currentLineText.length && (currentLineText[leadingSpacesCount].let { it == ' ' || it == '\t' })) {
      leadingSpacesCount++
    }

    val currentLinePosition = caretColumn
    if (currentLinePosition < currentLineText.length - 1) {
      // If the cursor is not at the end of the line, we remove spaces AFTER the cursor (until the next text) so that there are no more spaces on the new line
      val trailingSpacesCount = currentLineText.substring(currentLinePosition).takeWhile { it == ' ' || it == '\t' }.count()
      if (trailingSpacesCount > 0) {
        deleteText(caretPosition, caretPosition + trailingSpacesCount)
      }
    }
    // Insert newline + same indent
    insertText(caretPosition, System.lineSeparator() + " ".repeat(leadingSpacesCount))
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
          Optional.ofNullable(t.get())
        else {
          t.failure.printStackTrace()
          Optional.empty()
        }
      }
      .subscribe(this::applyHighlighting)
  }

  private fun computeHighlightingAsync(): Task<StyleSpans<List<String>>?> {
    val text: String = text ?: ""
    val task: Task<StyleSpans<List<String>>?> =
      object : Task<StyleSpans<List<String>>?>() {
        protected override fun call(): StyleSpans<List<String>>? {
          return if (text.isNotEmpty()) highlighter.highlight(text) else null
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