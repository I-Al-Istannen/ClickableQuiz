package me.ialistannen.clickablequiz.conversation

import me.ialistannen.clickablequiz.ClickableQuiz
import me.ialistannen.clickablequiz.data.quiz.Quiz
import me.ialistannen.clickablequiz.util.color
import me.ialistannen.clickablequiz.util.getRunCommand
import me.ialistannen.clickablequiz.util.sendJsonMessage
import org.bukkit.conversations.ConversationContext
import org.bukkit.conversations.Prompt
import org.bukkit.conversations.StringPrompt
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

/**
 * Converses with a player trying to solve a quiz
 *
 * The answers will be saved under '`answers`' in [ConversationContext#getSessionData]
 */
class PlayerQuizConversation(val quiz: Quiz) : StringPrompt() {

    private var currentQuestion = 0

    override fun acceptInput(context: ConversationContext, input: String): Prompt? {
        val correct = quiz[currentQuestion].isCorrect(input)

        addToAnswerList(correct, context)

        currentQuestion++

        if (currentQuestion >= quiz.getSize()) {
            // finished!
            return null
        }

        return this
    }
    
    override fun getPromptText(context: ConversationContext): String {
        val runnable = object : BukkitRunnable() {
            override fun run() {
                quiz[currentQuestion].answers
                        .map { it.key }
                        .forEach {
                            if (context.forWhom!! is Player) {
                                val jsonMessage = it.color().getRunCommand(it)
                                ((context.forWhom as Player).sendJsonMessage(jsonMessage))
                            }
                        }
            }
        }

        runnable.runTask(ClickableQuiz.instance)

        return quiz[currentQuestion].question.color()
    }

    /**
     * Adds the answer to the answer list
     *
     * The list is saved under the key `answer`
     */
    private fun addToAnswerList(correct: Boolean, context: ConversationContext) {
        // I store it, it is safe
        @Suppress("UNCHECKED_CAST")
        val list: MutableList<Boolean> = context.getSessionData("answers") as MutableList<Boolean>? ?: ArrayList<Boolean>()
        list.add(correct)
        context.setSessionData("answers", list)
    }
}
