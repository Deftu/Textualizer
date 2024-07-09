package dev.deftu.textualizer.text

import dev.deftu.textile.SimpleTextHolder
import dev.deftu.textualizer.LanguageManager

public class TextualizerTextHolder(
    key: String,
    vararg replacements: Any
) : SimpleTextHolder(LanguageManager.translate(key, *replacements))
