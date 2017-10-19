package com.zxventures.zxapp.extensions

import kotlin.reflect.KProperty

/**
 * Created by rodrigosimoesrosa
 */
object Delegate {

    fun <Type> notNullValue() = NotNullValue<Type>()

    class NotNullValue<Type> {
        var value: Type? = null

        operator fun getValue(ref: Any?, prop: KProperty<*>): Type =
                value ?: throw IllegalStateException("${prop.name} wasn't initialized")

        operator fun setValue(ref: Any?, prop: KProperty<*>, value: Type) {
            this.value = if (this.value == null) value
            else throw IllegalStateException("${prop.name} was already initialized")
        }
    }
}