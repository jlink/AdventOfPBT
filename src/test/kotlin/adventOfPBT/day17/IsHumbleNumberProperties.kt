package adventOfPBT.day17

import net.jqwik.api.*
import net.jqwik.api.constraints.Size
import java.math.BigInteger

class IsHumbleNumberProperties {

    /**
     * Generate humble numbers by multiplying a list of humble prime factors.
     * Assume that product is <= Long.MAX_VALUE.
     */
    @Property
    fun `humble numbers are humble`(
        @ForAll("humbleFactors") humbleFactors: List<Long>
    ): Boolean {
        val number: BigInteger = humbleFactors.product()
        Assume.that(number <= BigInteger.valueOf(Int.MAX_VALUE.toLong()))

        return isHumbleNumber(number.longValueExact())
    }

    /**
     * Generate humble numbers by multiplying a potentially empty list of humble prime factors
     * with a non-empty list of non-humble prime factors.
     * Assume that product is <= Long.MAX_VALUE.
     */
    @Property
    fun `non-humble numbers are not humble`(
        @ForAll("humbleFactors") humbleFactors: List<Long>,
        @ForAll("nonHumbleFactors") @Size(min = 1) nonHumbleFactors: List<Long>
    ): Boolean {
        val number: BigInteger = (humbleFactors + nonHumbleFactors).product()
        Assume.that(number <= BigInteger.valueOf(Long.MAX_VALUE))

        return !isHumbleNumber(number.longValueExact())
    }

    // Return product of all numbers or 1 if list is empty
    private fun List<Long>.product(): BigInteger = (this + 1L)
        .map { BigInteger.valueOf(it) }
        .reduce(BigInteger::times)

    @Provide
    private fun humbleFactors() = primes().filter { it <= 7 }.list().ofMaxSize(100)

    @Provide
    private fun nonHumbleFactors() = primes().filter { it > 7 }.list().ofMaxSize(100)

    // There could be as many primes in the list as you want.
    @Provide
    private fun primes() = Arbitraries.of(2L, 3L, 5L, 7L, 11L, 13L, 17L, 19L, 101L)

    @Property
    fun validatePrimesGenerator(@ForAll("primes") prime: Long) {
        for(divisor in 2L until prime) {
            assert(prime % divisor != 0L) { "$prime has divisor $divisor" }
        }
    }

}