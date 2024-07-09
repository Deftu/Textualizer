package dev.deftu.textualizer

//#if MC <= 1.12.2
//$$ import net.minecraftforge.fml.common.Loader
//$$ import net.minecraftforge.fml.common.ModContainer
//$$ import net.minecraftforge.fml.common.ModMetadata
//#endif

import dev.deftu.omnicore.client.OmniClient
import dev.deftu.omnicore.common.OmniIdentifier
import net.minecraft.client.MinecraftClient
import net.minecraft.resource.ReloadableResourceManagerImpl
import net.minecraft.resource.ResourceManager
import net.minecraft.resource.SynchronousResourceReloader
import java.util.*

public object LanguageManager : SynchronousResourceReloader {

    private const val DEFAULT_LANGUAGE: String = "en_us"

    public var isInitialized: Boolean = false
        private set
    private val metadata = mutableMapOf<String, LanguageMetadata>()
    public val currentLanguageCode: String
        get() {
            return MinecraftClient.getInstance().languageManager.language
                //#if MC <= 1.19.2
                //$$ .code
                //#endif
                .lowercase(Locale.US)
        }
    public lateinit var currentLanguage: Language
        private set

    /**
     * Sets up the language manager to detect language changes.
     *
     * This needs to be manually called in your mod's client-side entrypoint in both Forge and NeoForge.
     * Do NOT call this function on Fabric. It is automatically handled.
     */
    @JvmStatic
    public fun initialize() {
        if (isInitialized) {
            return
        }

        val resourceManager = OmniClient.getInstance().resourceManager
        if (resourceManager is ReloadableResourceManagerImpl) {
            resourceManager.registerReloader(this)
            isInitialized = true
        }
    }

    @JvmStatic
    public fun translate(key: String, vararg replacements: Any): String {
        val translation = currentLanguage.get(key)
        return translation.format(*replacements)
    }

    @JvmStatic
    public fun isTranslated(key: String): Boolean {
        return ::currentLanguage.isInitialized && currentLanguage.isTranslated(key)
    }

    override fun reload(resourceManager: ResourceManager) {
        metadata.clear()
        metadata.putAll(AvailableLanguageLoader.loadFromResourcePacks(AvailableLanguageLoader.getResourcePacks(resourceManager)))

        val loadedLanguages = mutableListOf(DEFAULT_LANGUAGE)
        var isCurrentlyRightToLeft = false
        if (currentLanguageCode != DEFAULT_LANGUAGE) {
            val currentMetadata = metadata[currentLanguageCode]
            if (currentMetadata != null) {
                loadedLanguages.add(currentLanguageCode)
                isCurrentlyRightToLeft = currentMetadata.isRightToLeft
            }
        }

        val language = loadLanguage(resourceManager, loadedLanguages, isCurrentlyRightToLeft)
        currentLanguage = language
        //#if MC <= 1.12.2
        //$$ LocalePropertiesManipulator.replace(language)
        //#endif
    }

    private fun loadLanguage(
        resourceManager: ResourceManager,
        loadedLanguages: List<String>,
        isRightToLeft: Boolean
    ): Language {
        val result = mutableMapOf<String, String>()

        for (language in loadedLanguages) {
            val mutatedLanguage = buildString {
                val splitLanguage = language.split("_")
                append(splitLanguage[0].lowercase(Locale.ENGLISH))
                splitLanguage.drop(1).forEach { part -> append("_${part.uppercase(Locale.ENGLISH)}") }
            }

            val path = "languages/$mutatedLanguage.json"
            for (namespace in resourceManager.allNamespaces) {
                try {
                    val fullPath = "/assets/$namespace/$path"
                    LanguageManager::class.java.getResourceAsStream(fullPath)?.use { inputStream ->
                        Language.load(inputStream, result::put)
                    }
                } catch (t: Throwable) {
                    t.printStackTrace()
                }
            }

            //#if MC <= 1.12.2
            //$$ for (modId in Loader.instance().modList.map(ModContainer::getMetadata).map(ModMetadata::modId)) {
            //$$     try {
            //$$         val fullPath = "/assets/$modId/$path"
            //$$         LanguageManager::class.java.getResourceAsStream(fullPath)?.use { inputStream ->
            //$$             Language.load(inputStream, result::put)
            //$$         }
            //$$     } catch (t: Throwable) {
            //$$         t.printStackTrace()
            //$$     }
            //$$ }
            //#endif
        }

        return Language(isRightToLeft, result)
    }

}
