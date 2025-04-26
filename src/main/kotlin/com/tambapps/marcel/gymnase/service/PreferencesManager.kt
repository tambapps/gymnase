package com.tambapps.marcel.gymnase.service

import com.tambapps.marcel.gymnase.GymnaseApplication
import com.tambapps.marcel.gymnase.property.BooleanPreferencesProperty
import com.tambapps.marcel.gymnase.property.IntPreferencesProperty
import com.tambapps.marcel.gymnase.property.LongPreferencesProperty
import java.util.prefs.Preferences
import kotlin.let
import kotlin.reflect.KProperty
import kotlin.text.split

object PreferencesManager {

  private const val CODE_FONT_SIZE = "CODE_FONT_SIZE"
  private const val CODE_STYLE_SHEET = "CODE_STYLE_SHEET"
  private const val HIGHLIGHT_DELAY = "HIGHLIGHT_DELAY"
  private const val SCENE_SIZE = "SCENE_SIZE"
  private const val SHOW_LINES_NUMBER = "SHOW_LINES_NUMBER"

  private val preferences: Preferences = Preferences.userNodeForPackage(GymnaseApplication::class.java)

  val fontSizeProperty = IntPreferencesProperty(preferences, CODE_FONT_SIZE, 15)

  val showLinesNumberProperty = BooleanPreferencesProperty(preferences, SHOW_LINES_NUMBER, true)

  val codeStyleSheet: String
    get() = GymnaseApplication::class.java.getResource(preferences.get(CODE_STYLE_SHEET, "code-styles/one-dark.css"))!!.toExternalForm()

  val highlightDelayMillisProperty = LongPreferencesProperty(preferences, HIGHLIGHT_DELAY, 100L)

  // TODO do others
  var sceneSize by PreferenceProperty(SCENE_SIZE, Pair(800.0, 800.0),
    composer = { pair -> "${pair.first}|${pair.second}" }, parser = { s ->
      s.split("|").let { Pair(it[0].toDouble(), it[1].toDouble()) }
    })

  private class PreferenceBooleanProperty(
    private val key: String,
    private val defaultValue: Boolean,
  ) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>) = preferences.getBoolean(key, defaultValue)
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Boolean) = preferences.putBoolean(key, value)
  }

  private class PreferenceLongProperty(
    private val key: String,
    private val defaultValue: Long,
  ) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>) = preferences.getLong(key, defaultValue)
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Long) = preferences.putLong(key, value)
  }

  private class PreferenceIntProperty(
    private val key: String,
    private val defaultValue: Int,
  ) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): Int {
      return preferences.getInt(key, defaultValue)
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
      preferences.addPreferenceChangeListener { event -> event.newValue }
      preferences.putInt(key, value)
    }
  }

  private class PreferenceProperty<T>(
    private val key: String,
    private val defaultValue: T,
    private val parser: (String) -> T,
    private val composer: (T) -> String,
  ) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>) = preferences.get(key, null)?.let(parser::invoke) ?: defaultValue

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
      preferences.put(key, composer.invoke(value))
    }
  }
}

