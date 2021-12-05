package adventOfPBT.day4

data class LinkedList(val value: Int, var next: LinkedList? = null) {
    fun last(): LinkedList {
        val visited = mutableListOf(this)
        var current = this
        while(current.next != null) {
            if (current.next in visited) {
                return current
            }
            current = current.next!!
        }
        return current
    }

    override fun toString(): String {
        return "LinkedList(value=`$value`,hasNext=`${next != null}`)"
    }
}

fun detectCycleInLinkedList(list: LinkedList) : Boolean {
    val visited = mutableListOf(list)
    var current = list
    while(current.next != null) {
        if (current.next in visited) {
            return true
        }
        current = current.next!!
    }
    return false
}