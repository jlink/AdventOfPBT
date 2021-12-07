package adventOfPBT.day7

import net.jqwik.api.Example
import org.assertj.core.api.Assertions.assertThat
import java.math.BigInteger

class FibonacciProperties {

    // Since a fibonacci sequence could start with any two numbers, the start must be fixed somehow.
    @Example
    fun `fibonacci sequence starts with 0 and 1`() {
        assertThat(fibonacci(0)).isEqualTo(BigInteger.ZERO)
        assertThat(fibonacci(1)).isEqualTo(BigInteger.ONE)
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
        else -> BigInteger.TWO
    }
}