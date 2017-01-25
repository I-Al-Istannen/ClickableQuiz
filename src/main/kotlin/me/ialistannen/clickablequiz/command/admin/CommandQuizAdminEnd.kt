package me.ialistannen.clickablequiz.command.admin

import me.ialistannen.bukkitutilities.command.CommandResult
import me.ialistannen.bukkitutilities.command.CommandSenderType
import me.ialistannen.bukkitutilities.command.TranslatedCommandNode
import me.ialistannen.bukkitutilities.command.argumentmapping.ConvertedParams
import me.ialistannen.clickablequiz.ClickableQuiz
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.permissions.Permission

/**
 * Ends the quiz for a player
 */
class CommandQuizAdminEnd : TranslatedCommandNode(
        Permission("quiz.admin.end"),
        "command.quiz.admin.end",
        ClickableQuiz.instance.language,
        CommandSenderType.ALL) {

    override fun tabComplete(sender: CommandSender?, wholeChat: MutableList<String>?, relativeIndex: Int): List<String> {
        return getOnlinePlayerNames(sender)
    }

    @ConvertedParams(targetClasses = arrayOf(Player::class))
    fun executeCommand(sender: CommandSender, player: Player?,
                       @Suppress("UNUSED_PARAMETER") args: Array<out String>,
                       wholeChat: Array<out String>): CommandResult {

        if (player == null) {
            sender.sendMessage(ClickableQuiz.tr("quiz.general.player.not.found", wholeChat[0]))
            return CommandResult.SEND_USAGE
        }

        if (!player.isConversing) {
            sender.sendMessage(ClickableQuiz.tr("quiz.command.admin.end.player.takes.no.quiz", player.name))
            return CommandResult.SUCCESSFULLY_INVOKED
        }

        ClickableQuiz.instance.conversationManager.abandonConversations(player)

        sender.sendMessage(ClickableQuiz.tr("quiz.command.admin.end.stopped.quiz.for.player", player.name))

        return CommandResult.SUCCESSFULLY_INVOKED
    }
}