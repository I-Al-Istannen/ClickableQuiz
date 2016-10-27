package me.ialistannen.clickablequiz.data.quiz

import me.ialistannen.clickablequiz.data.parser.QuizParser
import org.bukkit.configuration.ConfigurationSection
import java.util.*

/**
 * Manages the quizzes
 */
class QuizManager : Iterable<Pair<String, Quiz>> {

    constructor(section: ConfigurationSection) {
        for (key in section.getKeys(false)) {
            val quiz = QuizParser(section.getConfigurationSection(key)).result
            add(quiz)
        }
    }

    val quizzes = HashMap<String, Quiz>()

    /**
     * Adds the [Quiz]
     *
     * @param quiz The [Quiz] to add
     */
    fun add(quiz: Quiz) {
        quizzes.put(quiz.name, quiz)
    }

    /**
     * Returns the [Quiz] with the given name, if any
     */
    operator fun get(name: String): Quiz? = quizzes[name]

    override fun iterator(): Iterator<Pair<String, Quiz>> {
        return quizzes.entries.map { Pair(it.key, it.value) }.iterator()
    }

    override fun toString(): String {
        return "QuizManager(quizzes=$quizzes)"
    }


}