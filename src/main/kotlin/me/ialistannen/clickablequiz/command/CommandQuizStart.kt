package me.ialistannen.clickablequiz.command

import com.perceivedev.perceivecore.command.CommandResult
import com.perceivedev.perceivecore.command.CommandSenderType
import com.perceivedev.perceivecore.command.TranslatedCommandNode
import me.ialistannen.clickablequiz.ClickableQuiz
import me.ialistannen.clickablequiz.util.QuizRunner
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.permissions.Permission

/**
 * Allows you to start a quiz
 */
class CommandQuizStart() : TranslatedCommandNode(
        Permission("quiz.start"),
        "command.quiz.start",
        ClickableQuiz.instance.language,
        CommandSenderType.PLAYER) {

    override fun tabComplete(sender: CommandSender, chat: MutableList<String>, index: Int): MutableList<String> {
        return children.map { it.keyword }.toMutableList()
    }

    override fun executePlayer(sender: Player, vararg args: String): CommandResult {
        val runner = QuizRunner(sender, sender, args)
        
        return runner.start()
    }
}
