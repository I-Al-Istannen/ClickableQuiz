package me.ialistannen.clickablequiz.command

import com.perceivedev.perceivecore.command.CommandResult
import com.perceivedev.perceivecore.command.CommandSenderType
import com.perceivedev.perceivecore.command.TranslatedCommandNode
import me.ialistannen.clickablequiz.ClickableQuiz
import me.ialistannen.clickablequiz.conversation.stopRunningQuiz
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.permissions.Permission

/**
 * Ends a quiz
 */
class CommandQuizEnd : TranslatedCommandNode(
        Permission("quiz.end"),
        "command.quiz.end",
        ClickableQuiz.instance.language,
        CommandSenderType.PLAYER) {

    override fun tabComplete(sender: CommandSender, chat: MutableList<String>, index: Int): List<String> {
        return emptyList()
    }

    override fun executePlayer(player: Player, vararg args: String): CommandResult {
        if (!player.isConversing) {
            player.sendMessage(ClickableQuiz.tr("quiz.command.end.not.conversing"))
            return CommandResult.SUCCESSFULLY_INVOKED
        }

        player.stopRunningQuiz()

        return CommandResult.SUCCESSFULLY_INVOKED
    }
}
