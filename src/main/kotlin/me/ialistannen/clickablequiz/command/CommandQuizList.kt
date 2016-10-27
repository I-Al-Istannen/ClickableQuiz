package me.ialistannen.clickablequiz.command

import com.perceivedev.perceivecore.command.CommandResult
import com.perceivedev.perceivecore.command.CommandSenderType
import com.perceivedev.perceivecore.command.TranslatedCommandNode
import me.ialistannen.clickablequiz.ClickableQuiz
import me.ialistannen.clickablequiz.data.player.getCooldownTimeLeftWords
import me.ialistannen.clickablequiz.data.player.isOnCooldown
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.permissions.Permission
import java.util.*

/**
 * Lists the quizzes
 */
class CommandQuizList : TranslatedCommandNode(
        Permission("quiz.list"),
        "command.quiz.list",
        ClickableQuiz.instance.language,
        CommandSenderType.ALL) {

    override fun tabComplete(sender: CommandSender?, chat: MutableList<String>?, index: Int): MutableList<String> {
        return Collections.emptyList()
    }


    override fun executeGeneral(sender: CommandSender, vararg args: String): CommandResult {

        run {
            val header = ClickableQuiz.tr("quiz.command.list.header")
            if (!header.isEmpty()) {
                sender.sendMessage(header)
            }
        }
        for ((name, quiz) in ClickableQuiz.instance.quizManager) {
            if (sender is Player && sender.isOnCooldown(quiz)) {
                sender.sendMessage(ClickableQuiz.tr("quiz.command.list.format.on.cooldown", name,
                        quiz.cooldown, quiz.minPercentage, quiz.questions.size, sender.getCooldownTimeLeftWords(quiz)))
            } else {
                sender.sendMessage(ClickableQuiz.tr("quiz.command.list.format", name, quiz.cooldown, quiz.minPercentage, quiz.questions.size))
            }
        }

        run {
            val footer = ClickableQuiz.tr("quiz.command.list.footer")
            if (!footer.isEmpty()) {
                sender.sendMessage(footer)
            }
        }

        return CommandResult.SUCCESSFULLY_INVOKED
    }

}