package me.ialistannen.clickablequiz.command

import com.perceivedev.perceivecore.command.CommandResult
import com.perceivedev.perceivecore.command.CommandSenderType
import com.perceivedev.perceivecore.command.TranslatedCommandNode
import me.ialistannen.clickablequiz.ClickableQuiz
import me.ialistannen.clickablequiz.command.admin.CommandQuizAdmin
import org.bukkit.command.CommandSender
import org.bukkit.permissions.Permission

/**
 * The base relay command for quizzes
 */
class CommandQuiz() : TranslatedCommandNode(
        Permission("quiz.main"),
        "command.quiz",
        ClickableQuiz.instance.language,
        CommandSenderType.ALL) {

    init {
        addChild(CommandQuizStart())
        addChild(CommandQuizList())
        addChild(CommandQuizEnd())
        addChild(CommandQuizAdmin())
    }

    override fun tabComplete(sender: CommandSender, chat: MutableList<String>, index: Int): MutableList<String> {
        return children.map { it.keyword }
                .filter { it.toLowerCase().startsWith(chat.last().toLowerCase()) }
                .toMutableList()
    }

    override fun executeGeneral(sender: CommandSender?, vararg args: String?): CommandResult {
        return CommandResult.SEND_USAGE
    }
}