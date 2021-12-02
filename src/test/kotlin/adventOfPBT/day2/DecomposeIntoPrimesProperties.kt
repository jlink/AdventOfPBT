package adventOfPBT.day2

import net.jqwik.api.*
import net.jqwik.kotlin.api.JqwikIntRange
import net.jqwik.kotlin.api.ofSize
import org.assertj.core.api.Assertions.assertThat
import kotlin.math.pow

class DecomposeIntoPrimesProperties {

    @Property
    fun `prime is decomposed into list of itself`(@ForAll("primes") prime: Int) {
        assertThat(decomposeIntoPrimes(prime)).containsExactly(prime)
    }

    @Property
    fun `prime squared is decomposed into list of twice itself`(@ForAll("primes") prime: Int) {
        assertThat(decomposeIntoPrimes(prime * prime)).containsExactly(prime, prime)
    }

    @Property
    fun `prime powered to n is decomposed into list of n times itself`(
        @ForAll("primes") prime: Int,
        @ForAll power: @JqwikIntRange(min = 1, max = 5) Int
    ) {
        val number = prime.toDouble().pow(power.toDouble()).toInt()
        assertThat(decomposeIntoPrimes(number)).containsOnly(prime)
        assertThat(decomposeIntoPrimes(number)).hasSize(power)
    }

    @Property
    fun `product of two primes is decomposed into list of both`(
        @ForAll("primes") prime1: Int,
        @ForAll("primes") prime2: Int
    ) {
        Assume.that(prime1 < prime2)
        assertThat(decomposeIntoPrimes(prime1 * prime2)).containsExactly(prime1, prime2)
    }

    @Property
    fun `product of list of primes is decomposed into original list`(
        @ForAll("listOfPrimes") primes: List<Int>
    ) {
        val product = product(primes)
        Assume.that(product > 1) // To catch Int overflow with multiplication
        assertThat(decomposeIntoPrimes(product))
            .containsExactlyInAnyOrderElementsOf(primes)
    }

    // This property will detect performance problems with naive implementations
    @Property
    fun `any number above 1 can be decomposed`(
        @ForAll number: @JqwikIntRange(min = 2) Int
    ): Boolean {
        val factors = decomposeIntoPrimes(number)
        val product = product(factors)
        return product == number
    }

    @Provide
    fun listOfPrimes() = primes().list().ofSize(1..7)

    @Provide
    private fun primes() = Arbitraries.of(2, 3, 5, 7, 11, 13, 17, 19, 23, 29)

    private fun product(primes: List<Int>) = primes.reduce(Int::times)

}

fun decomposeIntoPrimes(number: Int): List<Int> {
    var rest = number
    val factors = mutableListOf<Int>()
    var candidate = 2
    while (rest >= candidate) {
        while (rest % candidate != 0) {
            if (Math.sqrt(rest.toDouble()) < candidate) {
                candidate = rest
            } else {
                candidate++
            }
        }
        factors.add(candidate)
        rest /= candidate
    }
    return factors
}
