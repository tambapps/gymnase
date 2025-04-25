package com.tambapps.marcel.gymnase.service

import com.tambapps.marcel.gymnase.GymnaseApplication
import java.util.prefs.Preferences
import kotlin.let
import kotlin.text.split

object PreferencesManager {

  private const val CODE_FONT_SIZE = "CODE_FONT_SIZE"
  private const val CODE_STYLE_SHEET = "CODE_STYLE_SHEET"
  private const val HIGHLIGHT_DELAY = "HIGHLIGHT_DELAY"
  private const val SCENE_SIZE = "SCENE_SIZE"

  private val preferences: Preferences = Preferences.userNodeForPackage(GymnaseApplication::class.java)

  val fontSize: Int
    get() = preferences.getInt(CODE_FONT_SIZE, 15)

  val codeStyleSheet: String
    get() = GymnaseApplication::class.java.getResource(preferences.get(CODE_STYLE_SHEET, "code-styles/one-dark.css"))!!.toExternalForm()

  val highlightDelayMillis: Long
    get() = preferences.getLong(HIGHLIGHT_DELAY, 100L)

  var sceneSize: Pair<Double, Double>
    get() = getDoublePair(SCENE_SIZE) ?: Pair(800.0, 800.0)
    set(value) = putDoublePair(SCENE_SIZE, value.first, value.second)

  private fun getDoublePair(key: String): Pair<Double, Double>? = preferences.get(key, null)?.let {
    val fields = it.split("|")
    Pair(fields[0].toDouble(), fields[1].toDouble())
  }

  private fun putDoublePair(key: String, d1: Double, d2: Double) {
    preferences.put(key, "$d1|$d2")
  }
}