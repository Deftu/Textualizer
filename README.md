# Textualizer
An extension upon Textile which has it’s own custom I18n implementation.

---

[![Discord Badge](https://raw.githubusercontent.com/intergrav/devins-badges/v2/assets/cozy/social/discord-singular_64h.png)](https://s.deftu.dev/discord)
[![Ko-Fi Badge](https://raw.githubusercontent.com/intergrav/devins-badges/v2/assets/cozy/donate/kofi-singular_64h.png)](https://s.deftu.dev/kofi)

---

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
import dev.deftu.textualizer.LanguageManager;

String text = LanguageManager.translate("com.example");
```

To create a translated string with arguments, you can use
```java
import dev.deftu.textualizer.LanguageManager;

int number = 10;
String text = LanguageManager.translate("com.example", number);
```

## Interopability with Textile

Textualizer provides implementations of `TextHolder` and `MutableTextHolder` (or, more specifically, `SimpleTextHolder` and `SimpleMutableTextHolder`)
```java
import dev.deftu.textile.TextHolder;
import dev.deftu.textile.MutableTextHolder;
import dev.deftu.textualizer.text.TextualizerTextHolder;
import dev.deftu.textualizer.text.TextualizerMutableTextHolder;

TextHolder text = new TextualizerTextHolder("com.example");
MutableTextHolder mutableText = new TextualizerMutableTextHolder("com.example");
```

---

[![BisectHosting](https://www.bisecthosting.com/partners/custom-banners/8fb6621b-811a-473b-9087-c8c42b50e74c.png)](https://bisecthosting.com/deftu)

---

**This project is licensed under [LGPL-3.0][lgpl3].**\
**&copy; 2024 Deftu**

[lgpl3]: https://www.gnu.org/licenses/lgpl-3.0.en.html
