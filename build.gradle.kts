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

dependencies {
    val textileVersion = "0.5.1"
    api("dev.deftu:textile:$textileVersion")
    modImplementation("dev.deftu:textile-$mcData:$textileVersion")
    modImplementation("dev.deftu:omnicore-$mcData:0.9.0")

    if (mcData.isFabric) {
        modImplementation("net.fabricmc.fabric-api:fabric-api:${mcData.dependencies.fabric.fabricApiVersion}")
        modImplementation("net.fabricmc:fabric-language-kotlin:${mcData.dependencies.fabric.fabricLanguageKotlinVersion}")
    }
}
