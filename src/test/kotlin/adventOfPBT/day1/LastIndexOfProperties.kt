package adventOfPBT.day1

import net.jqwik.api.*
import net.jqwik.api.constraints.StringLength
import net.jqwik.kotlin.api.any

class LastIndexOfProperties {

    @Property
    fun `when search string not in container return -1`(
        @ForAll @StringLength(max = 1000) container: String,
        @ForAll @StringLength(min = 1, max = 100) searchString: String
    ) : Boolean {
        Assume.that(searchString !in container)
        return container.lastIndexOf(searchString) == -1
    }

    @Property
    fun `single appearance of search string is correctly indexed`(
        @ForAll("containerIndexPairs") containerAndIndex: Pair<String, Int>,
        @ForAll @StringLength(min = 1) searchString: String
    ) : Boolean {
        val(container, index) = containerAndIndex
        Assume.that(searchString !in container)
        val modifiedContainer = container.substring(0,index) + searchString + container.substring(index)
        return modifiedContainer.lastIndexOf(searchString) == index
    }

    @Provide
    fun containerIndexPairs(): Arbitrary<Pair<String, Int>> {
        val strings = String.any()
        return strings.flatMap { s ->
            Int.any(0..s.length).map { Pair(s, it)}
        }
    }

    @Property
    fun `double appearance of search string returns index of last appearance`(
        @ForAll("containerIndexPairs") containerAndIndex: Pair<String, Int>,
        @ForAll @StringLength(min = 1) searchString: String
    ) : Boolean {
        val(container, index) = containerAndIndex
        Assume.that(searchString !in container)
        val modifiedContainer = searchString + container.substring(0,index) + searchString + container.substring(index)
        val indexOfLastAppearance = index + searchString.length
        return modifiedContainer.lastIndexOf(searchString) == indexOfLastAppearance
    }

}