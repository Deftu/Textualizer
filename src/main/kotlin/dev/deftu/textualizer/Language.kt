package dev.deftu.textualizer

import com.google.gson.Gson
import com.google.gson.JsonObject
import java.io.InputStream
import java.util.function.BiConsumer

public data class Language(
    public val isRightToLeft: Boolean,
    public val translations: Map<String, String>
) {

    public companion object {

        public const val DEFAULT_LANGUAGE: String = "en_us"

        private val basicFormattingRegex = "%(\\d+\\$)?[\\d.]*[df]|\\{}".toRegex()
        private val gson = Gson()

        @JvmStatic
        public fun load(inputStream: InputStream, consumer: BiConsumer<String, String>) {
            val jsonObject = gson.fromJson(inputStream.reader(), JsonObject::class.java)
            for (entry in jsonObject.entrySet()) {
                if (!entry.value.isJsonPrimitive) {
                    continue
                }

                val stringValue = basicFormattingRegex.replace(entry.value.asString) { matchResult ->
                    when {
                        matchResult.value == "\\{}" -> "%s"
                        matchResult.value.matches(Regex("%(\\d+\\$)?[\\d.]*[df]")) -> "%${matchResult.groupValues.getOrNull(1) ?: ""}s"
                        else -> matchResult.value
                    }
                }

                consumer.accept(entry.key, stringValue)
            }
        }

        @JvmStatic
        public fun loadFromPath(path: String, consumer: BiConsumer<String, String>) {
            Language::class.java.getResourceAsStream(path)?.use { inputStream ->
                load(inputStream, consumer)
            } ?: throw IllegalArgumentException("Resource not found")
        }

    }

    public fun get(key: String): String {
        return translations[key] ?: key
    }

    public fun isTranslated(key: String): Boolean {
        return translations.containsKey(key)
    }

}
