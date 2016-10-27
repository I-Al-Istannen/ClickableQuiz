package me.ialistannen.clickablequiz.command.admin

import com.perceivedev.perceivecore.command.CommandSenderType
import com.perceivedev.perceivecore.command.TranslatedCommandNode
import me.ialistannen.clickablequiz.ClickableQuiz
import org.bukkit.command.CommandSender
import org.bukkit.permissions.Permission

/**
 * The main admin command
 */
class CommandQuizAdmin : TranslatedCommandNode(
        Permission("quiz.admin"),
        "command.quiz.admin",
        ClickableQuiz.instance.language,
        CommandSenderType.ALL) {

    init {
        addChild(CommandQuizAdminStart())
        addChild(CommandQuizAdminEnd())
        addChild(CommandQuizAdminReload())
    }

    override fun tabComplete(sender: CommandSender, chat: MutableList<String>, index: Int): MutableList<String> {
        return children.map { it.keyword }
                .filter { it.toLowerCase().startsWith(chat.last().toLowerCase()) }
                .toMutableList()
    }

}