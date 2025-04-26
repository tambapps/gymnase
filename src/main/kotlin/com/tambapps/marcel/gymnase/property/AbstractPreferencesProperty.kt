package com.tambapps.marcel.gymnase.property

import javafx.beans.InvalidationListener
import javafx.beans.property.Property
import javafx.beans.property.ReadOnlyProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.beans.value.WritableValue
import java.util.prefs.Preferences

abstract class AbstractPreferencesProperty<T>(
  protected val preferences: Preferences,
): ReadOnlyProperty<T>, Property<T>, WritableValue<T> {

  private val changeListeners: MutableList<ChangeListener<in T>> = mutableListOf()

  abstract fun set(value: T)

  override fun addListener(listener: InvalidationListener) = throw UnsupportedOperationException()

  override fun removeListener(listener: InvalidationListener) = throw UnsupportedOperationException()

  // add listener and fire change event now
  fun addListenerNow(listener: ChangeListener<in T>) {
    addListener(listener)
    listener.changed(this, value, value)
  }

  override fun addListener(listener: ChangeListener<in T>) {
    changeListeners.add(listener)
  }

  override fun removeListener(listener: ChangeListener<in T>) {
    changeListeners.remove(listener)
  }

  override fun getBean() = preferences

  override fun bind(observable: ObservableValue<out T>?) = throw UnsupportedOperationException("Cannot bind preferences.")

  override fun bindBidirectional(other: Property<T>?) = throw UnsupportedOperationException("Cannot bind preferences.")

  override fun unbindBidirectional(other: Property<T?>?) = throw UnsupportedOperationException("Cannot bind preferences.")

  override fun unbind() = throw UnsupportedOperationException("Cannot bind preferences.")

  override fun isBound() = false

  final override fun setValue(value: T) {
    val oldValue = this.value
    set(value)
    notifyListeners(oldValue)
  }

  private fun notifyListeners(oldValue: T) = changeListeners.forEach {
    it.changed(this, oldValue, value)
  }

}