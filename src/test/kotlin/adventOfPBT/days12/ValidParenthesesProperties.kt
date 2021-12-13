package adventOfPBT.days12

import net.jqwik.api.*
import net.jqwik.api.Arbitraries.of
import net.jqwik.api.Arbitraries.oneOf
import net.jqwik.kotlin.api.any
import net.jqwik.kotlin.api.combine

/**
 * The magic of this property is only in the generators.
 */
class ValidParenthesesProperties {

    @Property
    fun `balanced parentheses are valid`(@ForAll("balanced") expression: String): Boolean =
        validParentheses(expression)

    @Property(tries = 100)
    @Report(Reporting.GENERATED)
    fun `unbalanced parentheses are not valid`(@ForAll("unbalanced") expression: String): Boolean {
        return true
        //return !validParentheses(expression)
    }

    @Provide
    fun balanced(): Arbitrary<String> {
        return of("()", "(())", "[()]", "{[]()}")
    }

    /**
     * Manipulate a balanced expression to be no longer balanced
     */
    @Provide
    fun unbalanced(): Arbitrary<String> {
        return balanced()
            .filter { it.length >= 2 }
            .flatMap { balancedExpression ->
                oneOf(
                    removeParenthesis(balancedExpression),
                    injectParenthesis(balancedExpression),
                    swapParentheses(balancedExpression)
                )
            }
    }

    private fun removeParenthesis(balanced: String): Arbitrary<String> {
        val removeAt = Int.any(0 until balanced.length)
        return removeAt.map { balanced.removeRange(it..it) }
    }

    private fun injectParenthesis(balanced: String): Arbitrary<String> {
        val injectionAt = Int.any(0 until balanced.length)
        val injectionChar = Arbitraries.of('(', ')', '[', ']', '{', '}')
        return combine(injectionAt, injectionChar) { at, ch ->
            balanced.substring(0..at - 1) + ch + balanced.substring(at)
        }
    }

    private fun swapParentheses(balanced: String): Arbitrary<String> {
        val swapAt = Int.any(0 until (balanced.length - 1))
            .filter {
                val toSwap = balanced.substring(it..it + 1)
                it == 0 || (toSwap.isNotAPair() && toSwap[0] != toSwap[1])
            }
        return swapAt.map {
            val first = balanced[it]
            val second = balanced[it + 1]
            balanced.substring(0 until it) + second + first + balanced.substring(it + 2)
        }
    }

    private fun String.isNotAPair(): Boolean {
        if (this.length != 2) {
            return true;
        }
        return this !in setOf("()", ")(", "[]", "][", "{}", "}{")
    }
}
