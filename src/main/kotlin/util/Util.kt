import kotlin.math.abs

fun extented_euclid(a: Long, b: Long): Triple<Long, Long, Long> {
    if (b == 0L) {
        return Triple(a, 1, 0)
    } else {
        val (dd, ss, tt) = extented_euclid(b, a % b)
        val t = ss - (a / b) * tt
        return Triple(dd, tt, t)
    }
}

fun lcm(a: Long, b: Long): Long {
    return abs(a * b) / extented_euclid(a, b).first
}

fun lcm(a: Int, b: Int): Int {
    return abs(a * b) / extented_euclid(a.toLong(), b.toLong()).first.toInt()
}

fun lcm(numbers: List<Long>): Long {
    return numbers.reduceRight { a, b -> lcm(a, b)}
}
