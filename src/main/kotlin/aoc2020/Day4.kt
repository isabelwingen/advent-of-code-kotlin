package aoc2020

import getInputAsLines
import util.Day

class Day4: Day("4") {
    private fun validPassportPart1(passport: String): Int {
        val x = passport
            .split(" ")
            .filter {it.isNotBlank()}
            .map { it.split(":")[0] }
        return if (x.count() == 8) {
            1
        } else if (x.count() == 7 && !x.contains("cid")) {
            1
        } else {
            0
        }
    }

    private fun validPassportPart2(passport: String): Int {
        if (validPassportPart1(passport) == 0) {
            return 0
        }
        val map = passport
            .split(" ")
            .filter { it.isNotBlank() }
            .map {it.split(":")}
            .associate { Pair(it[0], it[1]) }
            .toMap()

        // byr
        val birthYear = map.getOrDefault("byr", "1900")
        if (birthYear.count() != 4 || !IntRange(1920, 2002).contains(Integer.parseInt(birthYear))) {
            return 0
        }

        // iyr
        val iyr = map.getOrDefault("iyr", "1900")
        if (iyr.count() != 4 || !IntRange(2010, 2020).contains(Integer.parseInt(iyr))) {
            return 0
        }

        // eyr
        val eyr = map.getOrDefault("eyr", "1900")
        if (eyr.count() != 4 || !IntRange(2020, 2030).contains(Integer.parseInt(eyr))) {
            return 0
        }

        // hgt
        val hgt = map.getOrDefault("hgt", "1in")
        if (hgt.endsWith("in")) {
            if (!IntRange(59,76).contains(Integer.parseInt(hgt.split("in")[0]))) {
                return 0
            }
        } else {
            if (!IntRange(150,193).contains(Integer.parseInt(hgt.split("cm")[0]))) {
                return 0
            }
        }

        // hcl
        val hcl = map.getOrDefault("hcl", "###")
        if (hcl.count() != 7) {
            return 0
        }
        if (!hcl.startsWith("#")) {
            return 0
        }
        if (hcl.count { setOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f').contains(it) } != 6) {
            return 0
        }

        // ecl
        val ecl = map.getOrDefault("ecl", "###")
        val valid = setOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")
        if (!valid.contains(ecl)) {
            return 0
        }

        //pid
        val pid = map.getOrDefault("pid", "000")
        if (pid.count() != 9 || pid.count { setOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9').contains(it) } != 9) {
            return 0
        }

        return 1
    }

    private fun readBatchFile(path: String, validFun: (String) -> Int): Int {
        val lines = getInputAsLines(path)
        var currentData = ""
        var validPassports = 0
        for (line in lines) {
            if (line.isBlank()) {
                validPassports += validFun(currentData)
                currentData = ""
            } else {
                currentData += " "
                currentData += line
            }
        }
        return validPassports
    }

    override fun executePart1(name: String): Long {
        return readBatchFile(name) { validPassportPart1(it) }.toLong()
    }

    override fun executePart2(name: String): Long {
        return readBatchFile(name) { validPassportPart2(it) }.toLong()
    }

}

