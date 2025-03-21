import com.modrinth.minotaur.dependencies.DependencyType
import com.modrinth.minotaur.dependencies.ModDependency
import dev.deftu.gradle.tools.minecraft.CurseRelation
import dev.deftu.gradle.tools.minecraft.CurseRelationType
import dev.deftu.gradle.utils.version.MinecraftVersions
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
toolkitMultiversion.moveBuildsToRootProject.set(true)
if (mcData.isForgeLike && mcData.version >= MinecraftVersions.VERSION_1_16_5) {
    toolkitLoomHelper.useKotlinForForge()

    if (mcData.isForge) {
        toolkitLoomHelper.useForgeMixin(modData.id)
    }
}

toolkitReleases {
    detectVersionType.set(true)

    modrinth {
        projectId.set("UhitUcEo")
        dependencies.add(ModDependency("textile-lib", DependencyType.REQUIRED))
        dependencies.add(ModDependency("omnicore", DependencyType.REQUIRED))

        if (mcData.version >= MinecraftVersions.VERSION_1_16_5) {
            val kotlinWrapperId = if (mcData.isForgeLike) "kotlin-for-forge" else "fabric-language-kotlin"
            dependencies.add(ModDependency(kotlinWrapperId, DependencyType.REQUIRED))
        }

        if (mcData.isFabric) {
            val fapiId = if (mcData.isLegacyFabric) "legacy-fabric-api" else "fabric-api"
            dependencies.add(ModDependency(fapiId, DependencyType.REQUIRED))
        }
    }

    curseforge {
        projectId.set("1224882")
        relations.add(CurseRelation("textile", CurseRelationType.REQUIRED))
        relations.add(CurseRelation("omnicore", CurseRelationType.REQUIRED))

        if (mcData.version >= MinecraftVersions.VERSION_1_16_5) {
            val kotlinWrapperId = if (mcData.isForgeLike) "kotlin-for-forge" else "fabric-language-kotlin"
            relations.add(CurseRelation(kotlinWrapperId, CurseRelationType.REQUIRED))
        }

        if (mcData.isFabric) {
            val fapiId = if (mcData.isLegacyFabric) "legacy-fabric-api" else "fabric-api"
            relations.add(CurseRelation(fapiId, CurseRelationType.REQUIRED))
        }
    }
}

repositories {
    maven("https://maven.neoforged.net/releases")
}

dependencies {
    api(project(":"))

    val textileVersion = "0.11.1"
    api("dev.deftu:textile:$textileVersion")
    modImplementation("dev.deftu:textile-$mcData:$textileVersion")
    modImplementation("dev.deftu:omnicore-$mcData:0.24.2")

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
