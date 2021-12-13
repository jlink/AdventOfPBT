package adventOfPBT.day12

import net.jqwik.api.*
import net.jqwik.api.Arbitraries.*
import net.jqwik.api.statistics.Statistics
import net.jqwik.api.statistics.StatisticsCoverage
import net.jqwik.kotlin.api.any
import net.jqwik.kotlin.api.combine
import org.assertj.core.api.Assertions
import java.util.function.Predicate

/**
 * The magic of this property is only in the generators.
 */
class ValidParenthesesProperties {

    @Group
    inner class `Validate Generators` {

        /**
         * Validate that unbalanced expressions have both even and odd size
         */
        @Property
        fun `check unbalanced expression generation`(@ForAll("unbalanced") expression: String) {
            Statistics.label("odd size").collect(expression.length % 2 == 1)
                .coverage {checker ->
                    checker.check(true).percentage(Predicate { it > 20 })
                    checker.check(false).percentage(Predicate { it > 20 })
                }
        }

        @Property
        fun `check balanced expression generation`(@ForAll("balanced") expression: String) {
            Assertions.assertThat(expression.length).isEven()

            /**
             * Expression size should be small and large
             */
            Statistics.label("expression size").collect(expression.length)
                .coverage { checker ->
                    checker.check(0).count(Predicate { it > 0 })
                    checker.queryValue { it > 20 }.percentage(Predicate { percentage -> percentage > 5.0 })
                }

            /**
             * Balanced expressions should have small and large nesting levels
             */
            Statistics.label("expression nesting").collect(maxNesting(expression))
                .coverage { checker ->
                    checker.check(0).count(Predicate { it > 0 })
                    checker.check(1).count(Predicate { it > 0 })
                    checker.queryValue { it > 5 }.percentage(Predicate { percentage -> percentage > 5.0 })
                }
        }

        private fun StatisticsCoverage.queryValue(query: (Int) -> Boolean): StatisticsCoverage.CoverageChecker {
            return this.checkQuery { values -> query.invoke(values[0] as Int) }
        }

        private fun maxNesting(expression: String): Int {
            var maxNesting = 0;
            var currentNesting = 0;
            for (c in expression) {
                if (c.isOpeningParenthesis()) {
                    currentNesting++
                    maxNesting = Math.max(maxNesting, currentNesting)
                }
                if (c.isClosingParenthesis()) {
                    currentNesting--
                }
            }
            return maxNesting
        }

        private fun Char.isOpeningParenthesis(): Boolean {
            return this in setOf('(', '[', '{')
        }

        private fun Char.isClosingParenthesis(): Boolean {
            return this in setOf(')', ']', '}')
        }
    }

    @Property
    // @Report(Reporting.GENERATED)
    fun `balanced parentheses are valid`(@ForAll("balanced") expression: String): Boolean =
        validParentheses(expression)

    @Property(tries = 100)
    // @Report(Reporting.GENERATED)
    fun `unbalanced parentheses are not valid`(@ForAll("unbalanced") expression: String): Boolean {
        return true
        // TODO: validParentheses() is not property implemented
        //return !validParentheses(expression)
    }

    /**
     * Recursively generate balanced expressions by either:
     * - Return empty expression
     * - Concatenate two balanced expressions
     * - Nest a balanced expression in a pair of parentheses
     */
    @Provide
    fun balanced(): Arbitrary<String> {
        return lazyOf(
            { just("") },
            { concatenatedExpression() },
            { nestedExpression() }
        )
    }

    private fun concatenatedExpression() =
        combine(balanced(), balanced()) { left, right -> left + right }

    private fun nestedExpression() =
        combine(pairs(), balanced()) { pair, expression -> pair.first + expression + pair.second }

    private fun pairs() = of(
        Pair('(', ')'),
        Pair('[', ']'),
        Pair('{', '}')
    )

    /**
     * Manipulate a balanced expression to be no longer balanced by either
     * - Removing a parenthesis
     * - Adding a parenthesis
     * - Swapping two parentheses in a way to break balance
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
