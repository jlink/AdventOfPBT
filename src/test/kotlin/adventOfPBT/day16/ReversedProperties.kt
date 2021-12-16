package adventOfPBT.day16

import net.jqwik.api.*
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

    /**
     * Metamorphic properties do not make assumptions about single execution of function under test,
     * but about the change of the result, when the input is also changed.
     */
    @Group
    inner class `Metamorphic Properties` {

        @Property
        fun `adding element to list`(
            @ForAll original: List<Int>,
            @ForAll element: Int
        ) {
            val originalReversed = reversed(original)
            val appendedList = original + element
            val appendedListReversed = reversed(appendedList)

            assertThat(appendedListReversed).isEqualTo(listOf(element) + originalReversed)
        }

        @Property
        fun `reversing two lists`(
            @ForAll l1: List<Int>,
            @ForAll l2: List<Int>
        ) {
            val l1Reversed = reversed(l1)
            val l2Reversed = reversed(l2)

            val l1PlusL2 = l1 + l2
            val l1PlusL2Reversed = reversed(l1PlusL2)

            assertThat(l1PlusL2Reversed).isEqualTo(l2Reversed + l1Reversed)
        }


    }
}

fun <T> reversed(data: List<T>): List<T> {
    // Enable the following line to see which properties will detect the broken implementation:
    // if (data.size > 4) return data
    return data.asReversed()
}