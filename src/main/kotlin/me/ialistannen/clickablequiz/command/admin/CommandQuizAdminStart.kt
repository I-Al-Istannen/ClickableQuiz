package me.ialistannen.clickablequiz.command.admin

import me.ialistannen.bukkitutilities.command.CommandResult
import me.ialistannen.bukkitutilities.command.CommandSenderType
import me.ialistannen.bukkitutilities.command.TranslatedCommandNode
import me.ialistannen.bukkitutilities.command.argumentmapping.ConvertedParams
import me.ialistannen.clickablequiz.ClickableQuiz
import me.ialistannen.clickablequiz.util.QuizRunner
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.permissions.Permission

/**
 * Starts the quiz for another player
 */
class CommandQuizAdminStart : TranslatedCommandNode(
        Permission("quiz.admin.start"),
        "command.quiz.admin.start",
        ClickableQuiz.instance.language,
        CommandSenderType.ALL) {

    override fun tabComplete(sender: CommandSender?, wholeChat: MutableList<String>?, relativeIndex: Int): List<String> {
        return getOnlinePlayerNames(sender)
    }


    @ConvertedParams(targetClasses = arrayOf(Player::class))
    fun executeCommand(sender: CommandSender, player: Player?, args: Array<out String>, wholeChat: Array<out String>): CommandResult {
        if (player == null) {
            sender.sendMessage(ClickableQuiz.tr("quiz.general.player.not.found", wholeChat[0]))
            return CommandResult.SEND_USAGE
        }

        val runner = QuizRunner(sender, player, args)

        return runner.start()
    }
}