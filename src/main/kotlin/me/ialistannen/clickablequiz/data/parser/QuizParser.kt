package me.ialistannen.clickablequiz.data.parser

import com.perceivedev.perceivecore.utilities.time.DurationParser
import me.ialistannen.clickablequiz.data.quiz.Question
import me.ialistannen.clickablequiz.data.quiz.Quiz
import org.bukkit.configuration.ConfigurationSection
import java.time.Duration
import java.util.*

/**
 * Parses a quiz
 */
class QuizParser(section: ConfigurationSection) {

    val result: Quiz

    init {
        result = parse(section)
    }

    /**
     * Parses a Quiz from a [ConfigurationSection]
     *
     * @return The parsed [Quiz]
     */
    private fun parse(section: ConfigurationSection): Quiz {
        val questionList = ArrayList<Question>()

        if (!section.isDouble("minPercentage")) {
            throw IllegalArgumentException("Key 'minPercentage' not found of wrong type in '${section.currentPath}'")
        }

        if (!section.isString("name")) {
            throw IllegalArgumentException("Key 'name' not found of wrong type in '${section.currentPath}'")
        }

        if (!section.isString("cooldown")) {
            throw IllegalArgumentException("Key 'cooldown' not found of wrong type in '${section.currentPath}'")
        }
        val cooldown: Duration
        try {
            cooldown = Duration.ofMillis(DurationParser.parseDuration(section.getString("cooldown")))
        } catch (e: RuntimeException) {
            throw IllegalArgumentException("Key 'cooldown' is not valid '${section.currentPath}'", e)
        }
        
        if(!section.isBoolean("showAnswers")) {
            throw IllegalArgumentException("Key 'showAnswers' not found of wrong type in '${section.currentPath}'")
        }

        val questionSection = section.getConfigurationSection("questions")
        for (key in questionSection.getKeys(false)) {
            val question = parseQuestion(questionSection.getConfigurationSection(key))
            questionList.add(question)
        }

        return Quiz(questionList, section.getDouble("minPercentage"), section.getString("name"), cooldown, section.getBoolean("showAnswers"))
    }

    /**
     * Parses the question from the section
     *
     * @param section The [ConfigurationSection] where the answers are in
     *
     * @return The parsed [Question]
     */
    private fun parseQuestion(section: ConfigurationSection): Question {
        if (!section.contains("question")) {
            throw IllegalArgumentException("Key 'question' not found in '${section.currentPath}'")
        }
        val questionString = section.getString("question")
                ?: throw IllegalArgumentException("Key 'question' of wrong type in '${section.currentPath}'")

        if (!section.contains("answers") || !section.isConfigurationSection("answers")) {
            throw IllegalArgumentException("Section 'answers' not found in '${section.currentPath}.answers'")
        }

        val answers = parseAnswers(section.getConfigurationSection("answers"))

        return Question(questionString, answers)
    }

    /**
     * Parses the answers from the section
     *
     * @param section The [ConfigurationSection] where the answers are in
     *
     * @return The parsed answers
     */
    private fun parseAnswers(section: ConfigurationSection): MutableMap<String, Boolean> {
        // keep the order... :P
        val answers = LinkedHashMap<String, Boolean>()

        for (key in section.getKeys(false)) {
            if (!section.isConfigurationSection(key)) {
                continue
            }
            val answerSection = section.getConfigurationSection(key)
            if (!answerSection.isString("answer")) {
                throw IllegalArgumentException("Key 'answer' not found of wrong type in '${answerSection.currentPath}'")
            }

            if (!answerSection.isBoolean("isCorrect")) {
                throw IllegalArgumentException("Key 'isCorrect' not found of wrong type in '${answerSection.currentPath}'")
            }

            answers.put(answerSection.getString("answer"), answerSection.getBoolean("isCorrect"))
        }

        return answers
    }
}
