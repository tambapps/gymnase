package com.tambapps.marcel.gymnase.service

import org.springframework.stereotype.Service
import java.util.prefs.Preferences

@Service
class PreferencesManager(
  private val preferences: Preferences
) {
  private companion object {
    const val CODE_FONT_SIZE = "CODE_FONT_SIZE"
    const val HIGHLIGHT_DELAY = "HIGHLIGHT_DELAY"
    const val SCENE_SIZE = "SCENE_SIZE"
  }

  val fontSize: Int
    get() = preferences.getInt(CODE_FONT_SIZE, 15)

  val highlightDelayMillis: Long
    get() = preferences.getLong(HIGHLIGHT_DELAY, 100L)

  var sceneSize: Pair<Double, Double>
    get() = getDoublePair(SCENE_SIZE) ?: Pair(800.0, 800.0)
    set(value) {
      if (value != null) putDoublePair(SCENE_SIZE, value.first, value.second)
    }

  private fun getDoublePair(key: String): Pair<Double, Double>? = preferences.get(key, null)?.let {
    val fields = it.split("|")
    Pair(fields[0].toDouble(), fields[1].toDouble())
  }

  private fun putDoublePair(key: String, d1: Double, d2: Double) {
    preferences.put(key, "$d1|$d2")
  }
}