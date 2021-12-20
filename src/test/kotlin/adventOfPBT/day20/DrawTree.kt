package adventOfPBT.day20

/**
 * Draw a tree with:
 * - a trunc made of '^',
 * - leaves on the left made of '('
 * - and the ones on the right made of ')'
 *
 * @param size - Size of the tree >=1
 */
fun drawTree(size: Int) = drawTreeLines(size).joinToString("\n")

private const val TRUNK = "^"

private fun drawTreeLines(size: Int): List<String> {
    return innerTree(size) + trunk(size)
}

private fun trunk(size: Int): List<String> = listOf(
    pad(size, 1, TRUNK), pad(size, 1, TRUNK)
)

private fun innerTree(size: Int): List<String> {
    val inner = mutableListOf<String>()
    for (index in 1..size) {
        var line = TRUNK
        if (index >= 2) {
            line = "(".repeat(index - 1) + line + ")".repeat(index - 1)
        }
        line = pad(size, index, line)
        inner.add(line)
    }
    return inner
}

private fun pad(size: Int, index: Int, line: String): String {
    var line1 = line
    val space = " ".repeat(size - index)
    line1 = space + line1 + space
    return line1
}
