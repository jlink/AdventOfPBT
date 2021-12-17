package adventOfPBT.day17

/**
 * Check if a number is a Humble number.
 *
 * Humble numbers are positive integers which have
 * no prime factors > 7.
 *
 * Source: https://rosettacode.org/wiki/Humble_numbers
 *
 * @param n - The number to be checked,
 *            superior or equal to zero and up to 2**31 -1
 */
fun isHumbleNumber(number: Long) : Boolean {

    var rest = number;

    for(divisor in 2L..7L) {
        while (rest % divisor == 0L) {
            rest /= divisor
        }
    }

    return rest == 1L;
}