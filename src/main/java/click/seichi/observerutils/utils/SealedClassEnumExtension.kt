// https://zenn.dev/chimerast/articles/b0e50701125ad6
interface SealedClassEnumExtension<T>
inline fun <reified T> SealedClassEnumExtension<T>.values(): List<T> {
    return T::class.sealedSubclasses.mapNotNull { it.objectInstance }
}