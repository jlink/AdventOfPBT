package adventOfPBT.day3

import net.jqwik.api.Assume
import net.jqwik.api.ForAll
import net.jqwik.api.Property
import net.jqwik.kotlin.api.JqwikIntRange
import org.assertj.core.api.Assertions.assertThat

/**
 * The properties show a few things:
 * - Assumptions can work as simple domain constraints when they don't filter out too much
 * - jqwik will generate _all possible values_ if their number is less than the default number of tries
 * - Using extension methods for conditions in Kotlin feels nice
 * - IntelliJ claims that Windows might have problems with double quotes in function names
 */
class FizzbuzzProperties {

    @Property
    fun `all numbers not divisible by 3 or 5 return themselves`(
        @ForAll @JqwikIntRange(min = 1, max = 200) n: Int
    ) {
        Assume.that(n.notDivisibleBy(3, 5))
        assertThat(fizzbuzz(n)).isEqualTo(n.toString())
    }

    @Property
    fun `all numbers divisible by 3 and not 5 return "Fizz"`(
        @ForAll @JqwikIntRange(min = 1, max = 200) n: Int
    ) {
        Assume.that(n.divisibleBy(3) && n.notDivisibleBy(5))
        assertThat(fizzbuzz(n)).isEqualTo("Fizz")
    }

    @Property(maxDiscardRatio = 10)
    fun `all numbers divisible by 5 and not 3 return "Buzz"`(
        @ForAll @JqwikIntRange(min = 1, max = 200) n: Int
    ) {
        Assume.that(n.divisibleBy(5) && n.notDivisibleBy(3))
        assertThat(fizzbuzz(n)).isEqualTo("Buzz")
    }

    @Property(maxDiscardRatio = 20)
    fun `all numbers divisible by 3 and 5 return "Fizz Buzz"`(
        @ForAll @JqwikIntRange(min = 1, max = 200) n: Int
    ) {
        Assume.that(n.divisibleBy(3, 5))
        assertThat(fizzbuzz(n)).isEqualTo("Fizz Buzz")
    }

    private fun Int.notDivisibleBy(vararg divisors: Int) = divisors.asSequence().none { this % it == 0 }
    private fun Int.divisibleBy(vararg divisors: Int) = divisors.asSequence().all { this % it == 0 }
}

fun fizzbuzz(n: Int): String {
    if (n % 15 == 0) {
        return "Fizz Buzz"
    }
    if (n % 3 == 0) {
        return "Fizz"
    }
    if (n % 5 == 0) {
        return "Buzz"
    }
    return n.toString()
}