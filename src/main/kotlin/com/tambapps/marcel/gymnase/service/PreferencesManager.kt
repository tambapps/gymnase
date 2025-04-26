package com.tambapps.marcel.gymnase.service

import com.tambapps.marcel.gymnase.GymnaseApplication
import com.tambapps.marcel.gymnase.property.BooleanPreferencesProperty
import com.tambapps.marcel.gymnase.property.IntPreferencesProperty
import com.tambapps.marcel.gymnase.property.LongPreferencesProperty
import com.tambapps.marcel.gymnase.property.ObjectPreferencesProperty
import java.util.prefs.Preferences
import kotlin.let
import kotlin.text.split

object PreferencesManager {

  private const val CODE_FONT_SIZE = "CODE_FONT_SIZE"
  private const val CODE_STYLE_SHEET = "CODE_STYLE_SHEET"
  private const val HIGHLIGHT_DELAY = "HIGHLIGHT_DELAY"
  private const val SCENE_SIZE = "SCENE_SIZE"
  private const val SHOW_LINES_NUMBER = "SHOW_LINES_NUMBER"
  private const val HIGHLIGHT_SELECTED_LINE = "HIGHLIGHT_SELECTED_LINE"

  private val preferences: Preferences = Preferences.userNodeForPackage(GymnaseApplication::class.java)

  val fontSizeProperty = IntPreferencesProperty(preferences, CODE_FONT_SIZE, 15)

  val showLinesNumberProperty = BooleanPreferencesProperty(preferences, SHOW_LINES_NUMBER, true)

  val highlightSelectedLineProperty = BooleanPreferencesProperty(preferences, HIGHLIGHT_SELECTED_LINE, true)

  val codeStyleSheet: String
    get() = GymnaseApplication::class.java.getResource(preferences.get(CODE_STYLE_SHEET, "code-styles/one-dark.css"))!!.toExternalForm()

  val highlightDelayMillisProperty = LongPreferencesProperty(preferences, HIGHLIGHT_DELAY, 100L)

  val sceneSizeProperty = ObjectPreferencesProperty(
    preferences,
    SCENE_SIZE,
    Pair(800.0, 800.0),
    composer = { pair -> "${pair.first}|${pair.second}" },
    parser = { s ->
      s.split("|").let { Pair(it[0].toDouble(), it[1].toDouble()) }
    }
  )
}

