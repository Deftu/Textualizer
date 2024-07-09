package dev.deftu.textualizer.text

import dev.deftu.textile.SimpleMutableTextHolder
import dev.deftu.textualizer.LanguageManager

public class TextualizerMutableTextHolder(
    key: String,
    vararg replacements: Any
) : SimpleMutableTextHolder(LanguageManager.translate(key, *replacements))
