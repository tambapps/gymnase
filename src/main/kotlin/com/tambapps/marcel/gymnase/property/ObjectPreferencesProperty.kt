package com.tambapps.marcel.gymnase.property

import java.util.prefs.Preferences

class ObjectPreferencesProperty<T>(
  preferences: Preferences,
  private val key: String,
  private val defaultValue: T,
  private val parser: (String) -> T,
  private val composer: (T) -> String,
) : AbstractPreferencesProperty<T>(preferences) {

  override fun getValue() = preferences.get(key, null)?.let { parser.invoke(it) } ?: defaultValue

  override fun set(value: T) = preferences.put(key, composer.invoke(value))

  override fun getName() = key
}