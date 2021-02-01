package com.brunocasado.randomemojianduserrepos.datasource.network

import android.util.Log
import com.brunocasado.randomemojianduserrepos.datasource.entity.Emoji
import com.brunocasado.randomemojianduserrepos.datasource.entity.EmojiResponse
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import org.json.JSONObject
import java.lang.reflect.Type

class EmojiConverterFactory : JsonDeserializer<EmojiResponse> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): EmojiResponse {
        val list = ArrayList<Emoji>()
        try {
            val jsonObject = JSONObject(json.toString())
            val namesIterator = jsonObject.keys()
            do {
                val name = namesIterator.next()
                val emoji = Emoji(name, jsonObject.getString(name))
                list.add(emoji)
            } while (namesIterator.hasNext())
        } catch (ex: Exception) {
            Log.d("EmojiConverterFactory", ex.message ?: "")
        }
        val map = mapOf<String, String>()

        return EmojiResponse(map, list)
    }
}