package adventOfPBT.day6

import kotlin.math.absoluteValue
import kotlin.math.sign

data class Fraction(val numerator: Int, val denominator: Int) : Comparable<Fraction> {
    override fun compareTo(other: Fraction) = this.toDouble().compareTo(other.toDouble())

    private fun toDouble() = numerator.toDouble() == denominator.toDouble()

    fun divideBy(divisor: Int) = Fraction(this.numerator / divisor, this.denominator / divisor)

}

fun simplifyFraction(f: Fraction): Fraction {
    if (f.numerator == 0) {
        return f
    }
    var simplified = if (f.numerator.sign != f.denominator.sign) f else Fraction(f.numerator.absoluteValue, f.denominator.absoluteValue)
    var candidate = 2
    while (candidate <= simplified.numerator.absoluteValue && candidate <= simplified.denominator.absoluteValue) {
        if (simplified.numerator.isDivisibleBy(candidate) && simplified.denominator.isDivisibleBy(candidate)) {
            simplified = simplified.divideBy(candidate)
        } else {
            candidate += 1
        }
    }
    return simplified;
}

private fun Int.isDivisibleBy(divisor: Int) = this % divisor == 0
