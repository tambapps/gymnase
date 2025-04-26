package com.tambapps.marcel.gymnase.property

import java.util.prefs.Preferences

class IntPreferencesProperty(
  preferences: Preferences,
  val key: String,
  val defaultValue: Int,
) : AbstractPreferencesProperty<Int>(preferences) {

  override fun getValue() = preferences.getInt(key, defaultValue)

  override fun getName() = key

  override fun set(value: Int) = preferences.putInt(key, value)

}