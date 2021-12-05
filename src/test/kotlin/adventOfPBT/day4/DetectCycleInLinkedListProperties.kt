package adventOfPBT.day4

import adventOfPBT.day4.LinkedListDomain.Cycles
import adventOfPBT.day4.LinkedListDomain.CyclesMode.*
import net.jqwik.api.*
import net.jqwik.api.domains.Domain
import net.jqwik.api.domains.DomainContextBase
import net.jqwik.api.providers.TypeUsage
import net.jqwik.kotlin.api.any
import net.jqwik.kotlin.api.ofSize

@Domain(LinkedListDomain::class)
class DetectCycleInLinkedListProperties {

    @Property(tries = 10)
    fun noCycleDetectedInListsWithoutCycles(@ForAll @Cycles(NO) list: LinkedList) =
        detectCycleInLinkedList(list) == false

    @Property(tries = 10)
    fun cycleDetectedInListsWithCycles(@ForAll @Cycles(YES) list: LinkedList) =
        detectCycleInLinkedList(list) == true
}

class LinkedListDomain : DomainContextBase() {

    enum class CyclesMode { YES, NO }

    @Retention(AnnotationRetention.RUNTIME)
    annotation class Cycles(val value: CyclesMode) {
    }

    @Provide
    fun linkedLists(usage: TypeUsage): Arbitrary<LinkedList> {
        val cycles = usage.findAnnotation(Cycles::class.java).map { it.value }.orElse(null)
        if (cycles == null) {
            val message = "LinkedList generation requires annotation @Cycles"
            throw IllegalArgumentException(message)
        }
        return when(cycles) {
            NO -> linkedListsWithoutCycles()
            YES -> linkedListsWithCycles()
        }
    }

    private fun linkedListsWithCycles(): Arbitrary<LinkedList> {
        return linkedListsWithoutCycles().map {
            it.last().next = it
            it
        }
    }

    private fun linkedListsWithoutCycles(): Arbitrary<LinkedList> {
        val lists = Int.any().list().ofSize(1..100)
        return lists.map { list ->
            var current : LinkedList? = null
            for (value in list) {
                current = LinkedList(value, current)
            }
            current!!
        }
        // Recursion is involved:
        // val sizes = Int.any(1..1000)
        // return sizes.flatMap { size ->
        //     Arbitraries.recursive(
        //         { -> Int.any().map { LinkedList(it) } },
        //         { a ->
        //             a.flatMap { next ->
        //                 Int.any().map { LinkedList(it, next) }
        //             }
        //         },
        //         size
        //     )
        // }
    }
}