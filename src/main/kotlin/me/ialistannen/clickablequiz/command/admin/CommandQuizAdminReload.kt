package me.ialistannen.clickablequiz.command.admin

import com.perceivedev.perceivecore.command.CommandResult
import com.perceivedev.perceivecore.command.CommandSenderType
import com.perceivedev.perceivecore.command.TranslatedCommandNode
import me.ialistannen.clickablequiz.ClickableQuiz
import org.bukkit.command.CommandSender
import org.bukkit.permissions.Permission

/**
 * Reloads the quizzes
 */
class CommandQuizAdminReload : TranslatedCommandNode(
        Permission("quiz.admin.reload"),
        "command.quiz.admin.reload",
        ClickableQuiz.instance.language,
        CommandSenderType.ALL) {

    override fun tabComplete(sender: CommandSender?, wholeChat: MutableList<String>?, relativeIndex: Int): List<String> {
        return emptyList()
    }

    override fun executeGeneral(sender: CommandSender, vararg args: String): CommandResult {
        
        ClickableQuiz.instance.reload()
        
        sender.sendMessage(ClickableQuiz.tr("quiz.command.admin.reload.reloaded"))
        
        return CommandResult.SUCCESSFULLY_INVOKED
    }
}