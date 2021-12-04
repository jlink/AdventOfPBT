package adventOfPBT.day4

import adventOfPBT.day4.LinkedListDomain.Cycles
import adventOfPBT.day4.LinkedListDomain.CyclesMode.NO
import net.jqwik.api.*
import net.jqwik.api.constraints.Size
import net.jqwik.api.domains.Domain
import net.jqwik.api.domains.DomainContextBase
import net.jqwik.api.providers.TypeUsage
import java.util.*

@Domain(LinkedListDomain::class)
class DetectCycleInLinkedListProperties {

    // check generation of linked lists

    // no cycles in linked lists
    @Property
    fun noCycleDetectedInListsWithoutCycles(@ForAll @Cycles(NO) list: LinkedList) =
        detectCycleInLinkedList(list) == false

    // cycles in linked lists
}

class LinkedListDomain : DomainContextBase() {

    enum class CyclesMode { YES, NO, MAYBE }

    @Retention(AnnotationRetention.RUNTIME)
    annotation class Cycles(val value: CyclesMode) {
    }

    @Provide
    fun linkedLists(usage: TypeUsage): Arbitrary<LinkedList> {
        val size: Optional<Size> = usage.findAnnotation(Size::class.java)
        val cycles: Optional<Cycles> = usage.findAnnotation(Cycles::class.java)
        if (cycles.isEmpty) {
            val message = "LinkedList generation requires annotation @Cycles"
            throw IllegalArgumentException(message)
        }
        return Arbitraries.just(LinkedList(42))
    }
}