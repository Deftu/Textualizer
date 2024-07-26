package dev.deftu.textualizer

//#if FORGE-LIKE && MC >= 1.16.5
//$$ import cpw.mods.modlauncher.Launcher
//#endif

import dev.deftu.omnicore.client.OmniClient
import dev.deftu.omnicore.common.OmniLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.resource.ReloadableResourceManagerImpl
import net.minecraft.resource.ResourceManager
import net.minecraft.resource.SynchronousResourceReloader
import java.io.InputStream
import java.util.*

public object LanguageManager : SynchronousResourceReloader {

    private const val DEFAULT_LANGUAGE: String = "en_us"

    //#if FORGE-LIKE && MC >= 1.16.5
    //$$ private val modLauncherClassLoader: ClassLoader
    //$$     get() {
    //$$         val launcher = Launcher.INSTANCE
    //$$         val field = launcher.javaClass.getDeclaredField("classLoader")
    //$$         field.isAccessible = true
    //$$         return field.get(launcher) as ClassLoader
    //$$     }
    //#endif

    private val isDebug: Boolean
        get() = System.getProperty("textualizer.debug")?.toBoolean() ?: false

    private val isLoadDebug: Boolean
        get() = isDebug || System.getProperty("textualizer.debug.load")?.toBoolean() ?: false

    private val isTranslationDebug: Boolean
        get() = isDebug || System.getProperty("textualizer.debug.translation")?.toBoolean() ?: false

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

        if (isLoadDebug) {
            println("Current language: $currentLanguage")
            println("Loaded languages: ${loadedLanguages.joinToString(", ")}")
        }
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

            if (isTranslationDebug) {
                println("Loading language: $language")
            }

            val loadedPaths = mutableSetOf<String>()
            val path = "languages/$mutatedLanguage.json"

            // Attempt to load through class loader using namespaces
            for (namespace in resourceManager.allNamespaces) {
                val fullPath = "/assets/$namespace/$path"
                if (fullPath in loadedPaths) {
                    if (isTranslationDebug) {
                        println("Skipping language from namespace: $language from $namespace")
                    }

                    continue
                }

                try {
                    val inputStream = getResource(fullPath)
                    println("Loaded language from namespace: $language from $namespace - $inputStream")
                    if (inputStream == null) {
                        if (isTranslationDebug) {
                            println("Did not load translations: $language from $namespace")
                        }

                        loadedPaths.add(fullPath)
                        continue
                    }

                    Language.load(inputStream, result::put)
                    loadedPaths.add(fullPath)
                    inputStream.close()
                    if (isTranslationDebug) {
                        println("Loaded language from namespace: $language from $namespace")
                    }
                } catch (t: Throwable) {
                    t.printStackTrace()
                }
            }

            // Attempt to load through class loader using mod IDs
            for (modId in OmniLoader.getLoadedMods().map(OmniLoader.ModInfo::id)) {
                val fullPath = "/assets/$modId/$path"
                if (fullPath in loadedPaths) {
                    if (isTranslationDebug) {
                        println("Skipping language from mod ID: $language from $modId")
                    }

                    continue
                }

                try {
                    val inputStream = getResource(fullPath)
                    if (inputStream == null) {
                        if (isTranslationDebug) {
                            println("Did not load translations: $language from $modId")
                        }

                        loadedPaths.add(fullPath)
                        continue
                    }

                    Language.load(inputStream, result::put)
                    loadedPaths.add(fullPath)
                    inputStream.close()
                    if (isTranslationDebug) {
                        println("Loaded language from mod ID: $language from $modId")
                    }
                } catch (t: Throwable) {
                    t.printStackTrace()
                }
            }
        }

        return Language(isRightToLeft, result)
    }

    private fun getResource(path: String): InputStream? {
        //#if FABRIC || MC <= 1.12.2
        return this::class.java.getResourceAsStream(path)
        //#else
        //$$ println(modLauncherClassLoader)
        //$$ return modLauncherClassLoader.getResourceAsStream(path)
        //#endif
    }

}
