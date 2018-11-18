package async

sealed class EggSize {
    object small: EggSize()
    object medium: EggSize()
    object large: EggSize()
}


data class Egg(val size: EggSize)
data class Flour(val weight: Int)
data class Butter(val weight: Int)
data class Mixture(val flour: Flour, val butter: Butter, val eggs: List<Egg>)
data class Cake(val flour: Flour, val butter: Butter, val eggs: List<Egg>)


data class BadButterException(val msg: String): Exception(msg)