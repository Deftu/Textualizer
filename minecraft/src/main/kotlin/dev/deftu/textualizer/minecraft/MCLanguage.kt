package dev.deftu.textualizer.minecraft

import com.google.gson.Gson
import com.google.gson.JsonObject
import dev.deftu.textualizer.Language
import java.io.InputStream
import java.util.function.BiConsumer

public data class MCLanguage(
    override val fallback: MCLanguage?,
    override val code: String,
    override val region: String,
    override val isRightToLeft: Boolean,
    override val nativeName: String,
    public val translations: Map<String, String>
) : Language<MCLanguage> {

    public companion object {

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

}
