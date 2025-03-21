# Textualizer

Custom localization solution for all Minecraft versions.

---

[![Discord Badge](https://raw.githubusercontent.com/intergrav/devins-badges/v2/assets/cozy/social/discord-singular_64h.png)](https://s.deftu.dev/discord)
[![Ko-Fi Badge](https://raw.githubusercontent.com/intergrav/devins-badges/v2/assets/cozy/donate/kofi-singular_64h.png)](https://s.deftu.dev/kofi)

---

# For users

## Why do I need this?
Minecraft's (and Forge's) language management system is very buggy and inconsistent across several Minecraft versions. This library aims to provide a consistent and reliable way to manage translations in your mods.

## Is it going to affect my FPS / performance?
No. The library doesn't add anything which would affect performance outside of the initial loading of the translations and language switching.

---

# For developers

## Setup

You need to add it as a dependency in your `build.gradle(.kts)` file.
```kt
repositories {
    maven("https://maven.deftu.dev/releases")
}

dependencies {
    modImplementation("dev.deftu:textualizer-<MINECRAFT VERSION>-<MOD LOADER>:<VERSION>")
}
```
Of course, replace `<MINECRAFT VERSION>` with the version of Minecraft you are developing for, `<MOD LOADER>` with the mod loader you are developing for, and `<VERSION>` with the version of the library you want to use.

## Usage

To create a translated string, you can use
```java
import dev.deftu.textualizer.minecraft.MCLocalization;

String text = MCLocalization.get("com.example");
```

To create a translated string with arguments, you can use
```java
import dev.deftu.textualizer.MCLocalization;

int number = 10;
String text = MCLocalization.get("com.example", number);
```

---

[![BisectHosting](https://www.bisecthosting.com/partners/custom-banners/8fb6621b-811a-473b-9087-c8c42b50e74c.png)](https://bisecthosting.com/deftu)

---

**This project is licensed under [LGPL-3.0][lgpl3].**  
**&copy; 2024 Deftu**

[lgpl3]: https://www.gnu.org/licenses/lgpl-3.0.en.html
