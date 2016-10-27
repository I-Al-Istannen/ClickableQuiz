package me.ialistannen.clickablequiz.data.quiz

import me.ialistannen.clickablequiz.util.stripColor
import java.util.*

/**
 * A question
 */
data class Question(val question: String, val answers: MutableMap<String, Boolean>) {

    constructor(question: String, answers: MutableMap<String, Boolean>.() -> Unit) : this(question, HashMap<String, Boolean>().apply(answers)) {
    }

    /**
     * Checks if the question was answered correctly
     */
    fun isCorrect(answer: String) : Boolean  {
        for((answ, value) in answers) {
            if(answ.trim().stripColor().equals(answer.trim().stripColor())) {
                return value
            }
        }
        return false
    }
}
