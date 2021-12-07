package adventOfPBT.day6

import net.jqwik.api.*
import net.jqwik.api.footnotes.EnableFootnotes
import net.jqwik.api.footnotes.Footnotes
import net.jqwik.kotlin.api.ofSize
import org.assertj.core.api.Assertions.assertThat
import java.math.BigInteger

// This is a long-running test suite.
// The reason is the slow and un-optimized simplification algorithm.
// That's why I reduced the number of tries.
@EnableFootnotes
@PropertyDefaults(tries = 100)
class SimplifyFractionProperties {

    // Shows how to use footnotes to get additional information only if property fails
    @Property
    fun Footnotes.`simplify does not change value`(@ForAll numerator: Int, @ForAll denominator: Int): Boolean {
        Assume.that(denominator != 0)
        val f = Fraction(numerator, denominator)
        val simplified = simplifyFraction(f)

        addFootnote("Fraction:   $f")
        addFootnote("Simplified: $simplified")

        return f.compareTo(simplified) == 0
    }

    // This is a step towards the more generic implementation when driving implementation through properties.
    @Property
    fun `same numerator and denominator are simplified to 1`(@ForAll numerator: Int) {
        Assume.that(numerator != 0 && numerator != Int.MIN_VALUE)
        val f = Fraction(numerator, numerator)
        val simplified = simplifyFraction(f)

        assertThat(simplified.numerator).isEqualTo(1)
        assertThat(simplified.denominator).isEqualTo(1)
    }

    @Property
    fun `with no common factors simplification does not change fraction`(
        @ForAll("primes") nFactors: List<Int>,
        @ForAll("primes") dFactorsCandidates: List<Int>,
    ) {
        val dFactors = dFactorsCandidates.removeAllIn(nFactors)
        val numerator = nFactors.product()
        val denominator = dFactors.product()

        // Filter out cases with Int overflow on multiplication of factors
        Assume.that(
            numerator < BigInteger.valueOf(Int.MAX_VALUE.toLong())
                    && denominator < BigInteger.valueOf(Int.MAX_VALUE.toLong())
        )

        val f = Fraction(numerator.intValueExact(), denominator.intValueExact())
        val simplified = simplifyFraction(f)
        assertThat(simplified).isEqualTo(f)
    }

    @Property
    fun `simplification divides by common factors`(
        @ForAll("primes") nFactors: List<Int>,
        @ForAll("primes") dFactors: List<Int>,
    ) {
        val numerator = nFactors.product()
        val denominator = dFactors.product()

        // Filter out cases with Int overflow on multiplication of factors
        Assume.that(
            numerator < BigInteger.valueOf(Int.MAX_VALUE.toLong())
                    && denominator < BigInteger.valueOf(Int.MAX_VALUE.toLong())
        )

        val commonFactor = dFactors.keepAllIn(nFactors).product().intValueExact()

        val f = Fraction(numerator.intValueExact(), denominator.intValueExact())
        val simplified = simplifyFraction(f)
        assertThat(simplified).isEqualTo(f.divideBy(commonFactor))
    }

    @Provide
    fun primes(): Arbitrary<List<Int>> = Arbitraries.of(2, 3, 5, 7, 11, 13, 17, 19, 23, 29).list().ofSize(1..10)
}

private fun List<Int>.product(): BigInteger {
    var product = BigInteger.ONE
    for (factor in this) {
        product = product.times(BigInteger.valueOf(factor.toLong()))
    }
    return product
}

private fun <E> List<E>.removeAllIn(toRemove: List<E>) = this.filter { e -> e !in toRemove }.toList()

private fun <E> List<E>.keepAllIn(toKeep: List<E>): List<E> {
    val restToKeep =toKeep.toMutableList()
    val commonElements = mutableListOf<E>()
    for (e in this) {
        if (e in restToKeep) {
            commonElements.add(e)
            restToKeep.remove(e)
        }
    }
    return commonElements
}
