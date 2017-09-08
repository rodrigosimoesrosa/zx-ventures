package br.com.mirabilis.util.io


import com.google.gson.ExclusionStrategy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.InputStream

object GsonUtil {

    fun getGson(): Gson = getGson(null)
    fun getGson(exclusionStrategy: ExclusionStrategy?): Gson {
        if (exclusionStrategy == null) {
            return Gson()
        } else {
            return GsonBuilder().setExclusionStrategies(exclusionStrategy).create()
        }
    }
    fun <T> toSerialize(input: InputStream, className: Class<T>): T {
        return getGson().fromJson(InputStreamUtil.toString(input), className)
    }
    fun <T> toSerialize(input: String, className: Class<T>): T {
        return getGson().fromJson(input, className)
    }

    fun <T> toJson(input:T):String {
        return getGson().toJson(input)
    }
}


