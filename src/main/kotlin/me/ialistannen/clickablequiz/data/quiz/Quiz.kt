package me.ialistannen.clickablequiz.data.quiz

import java.time.Duration

/**
 * The base class for quizzes
 */
open class Quiz(val questions: List<Question>, val minPercentage: Double, val name: String,
                val cooldown: Duration, val showAnswers: Boolean) {

    // Allows the use of Quiz[]
    operator fun get(index: Int): Question = questions[index]

    /**
     * Returns the size of the quiz
     */
    fun getSize() = questions.size


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Quiz) return false

        if (minPercentage != other.minPercentage) return false
        if (name != other.name) return false
        if (cooldown != other.cooldown) return false

        return true
    }

    override fun hashCode(): Int {
        var result = minPercentage.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + cooldown.hashCode()
        return result
    }

    override fun toString(): String {
        return "Quiz(minPercentage=$minPercentage, name='$name', cooldown=$cooldown)"
    }
}
