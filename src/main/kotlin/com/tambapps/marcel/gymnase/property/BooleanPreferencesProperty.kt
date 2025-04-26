package com.tambapps.marcel.gymnase.property

import java.util.prefs.Preferences

class BooleanPreferencesProperty(
  preferences: Preferences,
  val key: String,
  val defaultValue: Boolean,
) : AbstractPreferencesProperty<Boolean>(preferences) {

  override fun getValue() = preferences.getBoolean(key, defaultValue)

  override fun getName() = key

  override fun set(value: Boolean) = preferences.putBoolean(key, value)

}