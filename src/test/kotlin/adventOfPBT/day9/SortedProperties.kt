package adventOfPBT.day9

import net.jqwik.api.ForAll
import net.jqwik.api.Property
import org.assertj.core.api.Assertions.assertThat

// Using lists instead of arrays since type resolution for arrays with type constraints has a bug in jqwik 1.6.1
class SortedProperties {

    // Using a generic type constraint instead of a concrete sortable / comparable type like Int.
    // This makes the property arguably more generic.
    // In practice jqwik will generate lists of numbers, strings, dates etc.
    @Property
    fun <T : Comparable<T>> anyArrayCanBeSorted(@ForAll array: List<T>) {
        val sorted = array.sorted()
        assertThat(isSorted(sorted)).isTrue
    }

    // Checking sortedness is easier than actually sorting
    private fun <T : Comparable<T>> isSorted(array: List<T>): Boolean {
        if (array.size < 2) {
            return true
        }
        return array[0] <= array[1] && isSorted(array.drop(1))
    }
}
