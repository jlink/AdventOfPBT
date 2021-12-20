package adventOfPBT.day20

import net.jqwik.api.Example
import net.jqwik.api.ForAll
import net.jqwik.api.Property
import net.jqwik.kotlin.api.JqwikIntRange
import org.assertj.core.api.Assertions.assertThat
import java.util.function.Consumer

class DrawTreeProperties {

    @Example
    fun `draw a tree for visual inspection`() {
        println(drawTree(7))
    }

    @Property
    fun `top and bottom of tree`(@ForAll @JqwikIntRange(min = 1, max = 20) treeSize: Int) {
        val tree = drawTree(treeSize).lines()
        assertThat(tree.first().trim()).isEqualTo("^")
        assertThat(tree.last().trim()).isEqualTo("^")
    }

    @Property
    fun `height of tree`(@ForAll @JqwikIntRange(min = 1, max = 20) treeSize: Int) {
        assertThat(drawTree(treeSize).lines()).hasSize(treeSize + 2)
    }

    @Property
    fun `trunk is in the middle`(@ForAll @JqwikIntRange(min = 1, max = 20) treeSize: Int) {
        val tree = drawTree(treeSize).lines()
        assertThat(tree).allSatisfy(Consumer { line ->
            assertThat(line.length).isOdd
            assertThat(line.count { it == '^' }).isEqualTo(1)
            assertThat(line[treeSize - 1]).isEqualTo('^')
        })
    }

    @Property
    fun `tree is symmetric`(@ForAll @JqwikIntRange(min = 1, max = 20) treeSize: Int) {
        val tree = drawTree(treeSize).lines()
        assertThat(tree).allSatisfy(Consumer { line ->
            assertThat(line.isSymmetric()).isTrue
        })
    }

    @Property
    fun `tree has correct width`(@ForAll @JqwikIntRange(min = 1, max = 20) treeSize: Int) {
        val tree = drawTree(treeSize).lines()
        assertThat(tree).allSatisfy(Consumer { line ->
            assertThat(line.length).isEqualTo(1 + 2 * (treeSize - 1))
        })
    }

    @Property
    fun `width of leaves plus trunk`(@ForAll @JqwikIntRange(min = 1, max = 20) treeSize: Int) {
        val innerTree = treeWithoutTopAndBottom(treeSize)
        innerTree.forEachIndexed { index, s ->
            assertThat(s.trim().length).isEqualTo(index * 2 + 1)
        }
    }

    private fun treeWithoutTopAndBottom(treeSize: Int) = drawTree(treeSize).lines().subList(1, treeSize + 1)

    private fun String.isSymmetric(): Boolean {
        if (length <= 1) {
            return true
        }
        for (index in 0..length / 2) {
            val left = this[index]
            val right = this[length - index - 1]
            if (left == '(' && right == ')') {
                continue;
            } else if (left == '^' && right == '^') {
                continue;
            } else if (left == ' ' && right == ' ') {
                continue;
            } else {
                return false
            };
        }
        return true
    }
}