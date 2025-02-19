import com.modrinth.minotaur.dependencies.DependencyType
import com.modrinth.minotaur.dependencies.ModDependency
import dev.deftu.gradle.utils.MinecraftVersion
import dev.deftu.gradle.utils.ModLoader

plugins {
    java
    kotlin("jvm")
    id("dev.deftu.gradle.multiversion")
    id("dev.deftu.gradle.tools")
    id("dev.deftu.gradle.tools.resources")
    id("dev.deftu.gradle.tools.bloom")
    id("dev.deftu.gradle.tools.minecraft.api")
    id("dev.deftu.gradle.tools.minecraft.loom")
    id("dev.deftu.gradle.tools.publishing.maven")
    id("dev.deftu.gradle.tools.minecraft.releases")
}

kotlin.explicitApi()
toolkitMavenPublishing.forceLowercase.set(true)
toolkitLoomApi.setupTestClient()
toolkitMultiversion.moveBuildsToRootProject.set(true)
if (mcData.isForgeLike && mcData.version >= MinecraftVersion.VERSION_1_16_5) {
    toolkitLoomHelper.useKotlinForForge()

    if (mcData.isForge) {
        toolkitLoomHelper.useForgeMixin(modData.id)
    }
}

toolkitReleases {
    detectVersionType.set(true)

    modrinth {
        projectId.set("UhitUcEo")
        if (mcData.loader == ModLoader.FABRIC) {
            dependencies.addAll(listOf(
                ModDependency("P7dR8mSH", DependencyType.REQUIRED),                     // Fabric API
                ModDependency("Ha28R6CL", DependencyType.REQUIRED),                     // Fabric Language Kotlin
                ModDependency("mOgUt4GM", DependencyType.OPTIONAL)                      // Mod Menu
            ))
        } else if (mcData.version >= MinecraftVersion.VERSION_1_16_5) {
            dependencies.addAll(listOf(
                ModDependency("ordsPcFz", DependencyType.REQUIRED)                      // Kotlin for Forge
            ))
        }

        if (mcData.version >= MinecraftVersion.VERSION_1_16_5) {
            dependencies.add(ModDependency("T0Zb6DLv", DependencyType.REQUIRED))        // Textile
            dependencies.add(ModDependency("MaDESStl", DependencyType.REQUIRED))        // Omnicore
        }
    }
}

repositories {
    maven("https://maven.neoforged.net/releases")
}

dependencies {
    val textileVersion = "0.8.0"
    api("dev.deftu:textile:$textileVersion")
    modImplementation("dev.deftu:textile-$mcData:$textileVersion")
    modImplementation("dev.deftu:omnicore-$mcData:0.13.0")

    if (mcData.isFabric) {
        modImplementation("net.fabricmc:fabric-language-kotlin:${mcData.dependencies.fabric.fabricLanguageKotlinVersion}")

        if (mcData.isLegacyFabric) {
            // 1.8.9 - 1.13
            modImplementation("net.legacyfabric.legacy-fabric-api:legacy-fabric-api:${mcData.dependencies.legacyFabric.legacyFabricApiVersion}")
        } else {
            // 1.16.5+
            modImplementation("net.fabricmc.fabric-api:fabric-api:${mcData.dependencies.fabric.fabricApiVersion}")
        }
    } else if (mcData.isForgeLike) {
        implementation("cpw.mods:modlauncher:8.1.3")
    }
}
