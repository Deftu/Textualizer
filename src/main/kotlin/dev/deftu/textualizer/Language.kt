package dev.deftu.textualizer

public interface Language<T : Language<T>> {

    public val fallback: T?

    public val code: String

    public val region: String

    public val isRightToLeft: Boolean

    public val nativeName: String

}
