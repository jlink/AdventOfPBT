package adventOfPBT.day7

import net.jqwik.api.Example
import net.jqwik.api.ForAll
import net.jqwik.api.Property
import net.jqwik.kotlin.api.JqwikIntRange
import org.assertj.core.api.Assertions.assertThat
import java.math.BigInteger

class FibonacciProperties {

    // Since a fibonacci sequence could start with any two numbers, the start must be fixed somehow.
    @Example
    fun `fibonacci sequence starts with 0 and 1`() {
        assertThat(fibonacci(0)).isEqualTo(BigInteger.ZERO)
        assertThat(fibonacci(1)).isEqualTo(BigInteger.ONE)
    }

    // The main logic of the fibonacci sequence can fully be covered in a single property.
    // Jqwik will try all values and not sample randomly, if the upper bound is <= 1001.
    // Mind that a naive implementation will lead to VERY long execution time for indices above 50
    @Property
    fun `fibonacci of any index is the sum of fibonacci of two preceding values`(
        @ForAll @JqwikIntRange(min = 2, max = 1000) index: Int
    ) {
        val value = fibonacci(index)
        assertThat(value).isEqualTo(fibonacci(index - 1) + fibonacci(index - 2))
    }

}

/**
 * Compute fibonacci of n
 *
 * @param n - Index within fibonacci sequence
 *
 * @return
 * The value of F(n) where F is the fibonacci sequence.
 * F(n) = F(n-1) + F(n-2), with F(0) = 0n and F(1) = 1n.
 */
fun fibonacci(index: Int): BigInteger {
    return when (index) {
        0 -> BigInteger.ZERO
        1 -> BigInteger.ONE
        else -> fibonacciCache.computeIfAbsent(index) { i -> fibonacci(i - 2) + fibonacci(i - 1) }
    }
}

// Of course, this should rather be a Memoize class to abstract memoization away
val fibonacciCache = mutableMapOf<Int, BigInteger>()