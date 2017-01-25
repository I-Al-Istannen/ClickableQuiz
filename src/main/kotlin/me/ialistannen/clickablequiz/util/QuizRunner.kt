package me.ialistannen.clickablequiz.util

import me.ialistannen.bukkitutilities.command.CommandResult
import me.ialistannen.bukkitutilities.utilities.collections.ArrayUtils
import me.ialistannen.bukkitutilities.utilities.item.ItemFactory
import me.ialistannen.clickablequiz.ClickableQuiz
import me.ialistannen.clickablequiz.data.player.addCooldown
import me.ialistannen.clickablequiz.data.player.getCooldownTimeLeftWords
import me.ialistannen.clickablequiz.data.player.isOnCooldown
import me.ialistannen.clickablequiz.data.quiz.Quiz
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.conversations.ConversationContext
import org.bukkit.entity.Player

/**
 * Runs and checks the quiz
 */
class QuizRunner(val sender: CommandSender, val player: Player, val args: Array<out String>) {

    fun start(): CommandResult {
        if (args.size < 1) {
            return CommandResult.SEND_USAGE
        }

        val name: String
        if (args.size > 1) {
            val quotedText: String? = ArrayUtils.concat(args, " ").getFirstQuotedText()
            if (quotedText == null) {
                sender.sendMessage(ClickableQuiz.tr("quiz.command.start.no.quote.found.but.multiple.args.given"))
                return CommandResult.SUCCESSFULLY_INVOKED
            }
            name = quotedText
        } else {
            name = args[0]
        }

        val quiz = ClickableQuiz.instance.quizManager[name]

        if (quiz == null) {
            sender.sendMessage(ClickableQuiz.tr("quiz.general.quiz.not.found", name))
            return CommandResult.SUCCESSFULLY_INVOKED
        }

        if (player.isOnCooldown(quiz)) {
            sender.sendMessage(ClickableQuiz.tr("quiz.status.message.on.cooldown", quiz.name, player.getCooldownTimeLeftWords(quiz)))
            return CommandResult.SUCCESSFULLY_INVOKED
        }

        if (player.isConversing) {
            sender.sendMessage(ClickableQuiz.tr("quiz.status.message.already.conversing"))
            return CommandResult.SUCCESSFULLY_INVOKED
        }

        val conversation = ClickableQuiz.instance.conversationManager.buildConversation(quiz, player)

        conversation.addConversationAbandonedListener {
            if (!it.gracefulExit()) {
                it.context.forWhom.sendRawMessage(ClickableQuiz.tr("quiz.status.conversation.cancelled"))
                return@addConversationAbandonedListener
            }
            checkQuiz(it.context, quiz)
        }

        ClickableQuiz.instance.conversationManager.startConversation(conversation)
        return CommandResult.SUCCESSFULLY_INVOKED
    }

    private fun checkQuiz(context: ConversationContext, quiz: Quiz) {
        @Suppress("UNCHECKED_CAST") // I store it. I know that will not crash...
        val answers: MutableList<Boolean> = context.getSessionData("answers") as MutableList<Boolean>

        if (quiz.showAnswers) {
            val answerValidation: String = answers.joinToString(separator = ", ")
            context.forWhom.sendRawMessage(ClickableQuiz.tr("quiz.message.passed.answers", answerValidation))
        }

        val sum: Double = answers.map { if (it) 1 else 0 }.sum().toDouble()

        val percentage = sum / answers.size

        if (percentage < quiz.minPercentage) {
            failPlayer(context.forWhom as Player, quiz, percentage)
        } else {
            winPlayer(context.forWhom as Player, quiz, percentage)
        }

        (context.forWhom as Player).addCooldown(quiz.cooldown, quiz)
    }

    private fun winPlayer(player: Player, quiz: Quiz, percentage: Double) {
        player.sendMessage(ClickableQuiz.tr("quiz.message.passed", quiz.name, percentage))
        val item = ItemFactory.builder(Material.PAPER)
                .setName(ClickableQuiz.tr("quiz.item.passed.name", quiz.name.color(), player.name))
                .setLore(ClickableQuiz.tr("quiz.item.passed.lore1", percentage, quiz.name, player.name))
                .addLore(ClickableQuiz.tr("quiz.item.passed.lore2", percentage, quiz.name, player.name))
                .build()

        player.inventory.addItem(item)
    }

    private fun failPlayer(player: Player, quiz: Quiz, percentage: Double) {
        player.sendMessage(ClickableQuiz.tr("quiz.message.fail", quiz.name, percentage))
    }
}