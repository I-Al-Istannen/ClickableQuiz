package me.ialistannen.clickablequiz.conversation

import me.ialistannen.clickablequiz.ClickableQuiz
import me.ialistannen.clickablequiz.data.quiz.Quiz
import org.bukkit.conversations.Conversation
import org.bukkit.conversations.ConversationFactory
import org.bukkit.entity.Player
import java.util.*

/**
 * Manages the conversations
 */
class ConversationManager {
    var factory: ConversationFactory? = null
        private set

    private val conversations = HashMap<UUID, Conversation>()

    /**
     * Builds a conversation for the player
     *
     * @param quiz The quiz to take
     * @param player The player to build it for
     *
     *  @return The built [Conversation]
     */
    fun buildConversation(quiz: Quiz, player: Player): Conversation {
        if (factory == null) {
            factory = ConversationFactory(ClickableQuiz.instance)
                    .withEscapeSequence("end")
                    .withModality(true)
                    .withLocalEcho(false)
        }
        return factory!!.withFirstPrompt(PlayerQuizConversation(quiz))!!.buildConversation(player)
    }

    /**
     * Starts a Conversation and registers it
     *
     * @param conversation The [Conversation] to start
     */
    fun startConversation(conversation: Conversation) {
        if (conversation.forWhom is Player) {
            val uuid = (conversation.forWhom as Player).uniqueId
            conversation.addConversationAbandonedListener {
                conversations.remove(uuid)
            }
            conversations.put(uuid, conversation)
        }
        conversation.begin()
    }

    /**
     * Abandons all conversations for the player
     *
     * @param player the Player
     */
    fun abandonConversations(player: Player) {
        conversations[player.uniqueId]?.abandon()
    }

    /**
     * Abandons all conversations
     */
    fun abandonAllConversations() {
        for ((player, conversation) in conversations) {
            conversation.abandon()
        }
        conversations.clear()
    }
}

/**
 * Abandons all conversations for this player
 */
fun Player.stopRunningQuiz() {
    if (!this.isConversing) {
        return
    }
    ClickableQuiz.instance.conversationManager.abandonConversations(this)
}