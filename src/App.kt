import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.streams.toList

val words = Files.readAllLines(Paths.get("words.txt")).asSequence().map { it.toLowerCase() }
    .filter { !it.contains("-") }
    .filter { !it.contains("'") }
    .filter { !it.equals("hutukhtu") }
    .filter { !it.equals("kavakava") }
    .filter { !it.equals("kawakawa") }
    .filter { !it.equals("kivikivi") }
    .filter { !it.equals("kiwikiwi") }
    .filter { !it.equals("kolokolo") }
    .filter { !it.equals("malikala") }
    .filter { !it.equals("wanakena") }
    .filter { !it.equals("thisll") }
    .filter { !it.equals("tavell") }
    .filter { !it.equals("tewell") }
    .filter { !it.equals("thatll") }
    .filter { !it.equals("towill") }
    .filter { !it.equals("ploss") }
    .filter { !it.equals("theall") }
    .toList()

//var solution = mapOf(
//    6 to "h",
//    8 to "f"
//)

//var clues = listOf(
//    listOf(10, 2, 18, 23, 6, 17 ,5, 19)
//)

class TreeNode(val solutionSoFar: Map<Int, String>, val clue: Int) {
    val children = mutableListOf<TreeNode>()
}

fun solve(clues: List<List<Int>>, solution: Map<Int, String>): List<Map<Int, String>> {
    val possibleSolutions = mutableListOf<Map<Int, String>>()

    val nodeQueue = ArrayDeque<TreeNode>()

    nodeQueue.add(TreeNode(solution, 0))

    while (nodeQueue.isNotEmpty()) {
        val current = nodeQueue.pop()
        val seenNumbers = mutableMapOf<Int, Int>()
        val distinctLetters = clues[current.clue].distinct().size
        var filtered = words.asSequence()
            .filter { it.length == clues[current.clue].size }

        for (i in clues[current.clue].indices) {
            val number = clues[current.clue][i]
            if (current.solutionSoFar.containsKey(number)) {
                filtered = filtered.filter { it[i].toString() == current.solutionSoFar[number] }
            } else {
                if (seenNumbers.containsKey(number)) {
                    filtered = filtered.filter { it[i] == it[seenNumbers.getValue(number)] }
                } else {
                    seenNumbers[number] = i
                }
            }
        }

        val possibleWords = filtered
            .filter { it.chars().distinct().toList().size == distinctLetters }
            .toList()

        possibleWords.forEach { word ->
            val nextSolution = mutableMapOf<Int, String>()
            nextSolution.putAll(current.solutionSoFar)
            word.forEachIndexed { index, char ->
                nextSolution.put(clues[current.clue][index], char.toString())
            }
            if (current.clue == clues.size - 1) {
                possibleSolutions.add(nextSolution)
            } else {
                val newNode = TreeNode(nextSolution, current.clue + 1)
                current.children.add(newNode)
                nodeQueue.add(newNode)
            }
        }
    }

    possibleSolutions.forEach { solution ->
        println("---- ")
        println("solution: ")
        clues.forEach { clue ->
            clue.forEach { character ->
                print(solution[character])
            }
            print("\n")
        }

    }

    println("${possibleSolutions.size} solutions")

    val numSolutions = possibleSolutions.size
    for (i in 1..26) {
        val answers = mutableSetOf<String>()
        var count = 0;
        possibleSolutions.forEach { possibleSolution ->
            if (possibleSolution.containsKey(i)) {
                answers.add(possibleSolution.getValue(i))
                count++
            }
        }
        if (answers.size == 1 && count == numSolutions) {
            println("solved $i=${answers.iterator().next()}")
        }
    }

    return possibleSolutions
}

fun main() {
    val solution = solve(
        listOf
            (
            listOf(6,15,16,23,17,15,24,23),
            listOf(4,23,5,15,12,23),
            listOf(23,13,13,23,7,23),
            listOf(13,1,23,17,5),
            listOf(21,4,26,21,6,14)
        ), mapOf(
//        1 to "o",
//        2 to "t",
//            3 to "g",
            4 to "b",
        5 to "h",
            6 to "n",
            7 to "t",
//        8 to "v",
//        9 to "i",
//        10 to "-",
//        11 to "c",
        12 to "v",
        13 to "f",
//        14 to "d",
        15 to "a",
//        16 to "-",
//        17 to "a",
//            18 to "m",
        20 to "i",
        21 to "o",
//        22 to "-",
        23 to "e",
        24 to "k"
//        25 to "-",
//        26 to "n"
//    27 to "w",
//        28 to "p",
//        31 to "-",
//        35 to "-",
//        38 to "-"
        )
    )

    val i = 0
}
