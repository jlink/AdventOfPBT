package adventOfPBT.day16

import net.jqwik.api.Assume
import net.jqwik.api.ForAll
import net.jqwik.api.Property
import org.assertj.core.api.Assertions.assertThat

/**
 * Unlike in day 9's [SortedProperties] I'm not using type variables here
 * because the order of values in an array does not depend on the type of elements in any way.
 *
 * I'm also using [List]s instead of arrays since arrays do not bring anything new to the table and are much less common in Kotlin.
 */
class ReversedProperties {

    @Property
    fun `the size remains the same`(@ForAll original: List<Int>): Boolean {
        val reversed: List<Int> = reversed(original)
        return original.size == reversed.size
    }

    @Property
    fun `all elements stay`(@ForAll original: List<Int>) {
        val reversed: List<Int> = reversed(original)
        assertThat(original).allMatch { it in reversed }
    }

    /**
     * This property is only valid if there is a first and last element;
     * therefore the assumption.
     * Alternatively, we could work with a [Size] annotation.
     */
    @Property
    fun `first and last element are swapped`(@ForAll original: List<Int>): Boolean {
        Assume.that(original.size > 2)
        val reversed = reversed(original)
        return original.first() == reversed.last()
                && reversed.first() == original.last()
    }

    @Property
    fun `reverse twice is original`(@ForAll original: List<Int>): Boolean {
        return reversed(reversed(original)) == original
    }

}

fun <T> reversed(data: List<T>): List<T> {
    return data.asReversed()
}