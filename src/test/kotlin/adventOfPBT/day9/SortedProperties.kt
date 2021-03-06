package adventOfPBT.day9

import net.jqwik.api.ForAll
import net.jqwik.api.Property
import org.assertj.core.api.Assertions.assertThat

class SortedProperties {

    // Using a generic type constraint instead of a concrete sortable / comparable type like Int.
    // This makes the property arguably more generic.
    // In practice jqwik will generate arrays of numbers, strings, dates etc.
    @Property
    fun <T : Comparable<T>> `sorted array has exact same elements`(@ForAll array: Array<T>) {
        val sorted = sorted(array)
        assertThat(sorted).hasSameSizeAs(array)
        assertThat(sorted).hasSameElementsAs(array.asList())
    }

    @Property
    fun <T : Comparable<T>> `any array can be sorted`(@ForAll array: Array<T>) {
        val sorted = sorted(array)
        assertThat(isSorted(sorted)).isTrue
    }

    // Checking sortedness is easier than actually sorting
    private fun <T : Comparable<T>> isSorted(array: Array<T>) = isSorted(array.asList())

    private fun <T : Comparable<T>> isSorted(array: List<T>): Boolean {
        if (array.size < 2) {
            return true
        }
        return array[0] <= array[1] && isSorted(array.drop(1))
    }
}

fun <T : Comparable<T>> sorted(array: Array<T>): Array<T> {
    val copy: Array<T> = array.copyOf()
    copy.sort()
    return copy
}