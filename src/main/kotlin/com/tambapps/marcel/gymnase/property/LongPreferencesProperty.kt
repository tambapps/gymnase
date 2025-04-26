package com.tambapps.marcel.gymnase.property

import java.util.prefs.Preferences

class LongPreferencesProperty(
  preferences: Preferences,
  val key: String,
  val defaultValue: Long,
) : AbstractPreferencesProperty<Long>(preferences) {

  override fun getValue() = preferences.getLong(key, defaultValue)

  override fun getName() = key

  override fun set(value: Long) = preferences.putLong(key, value)

}