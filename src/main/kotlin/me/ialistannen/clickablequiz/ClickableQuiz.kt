package me.ialistannen.clickablequiz

import com.perceivedev.perceivecore.command.CommandSystemUtil
import com.perceivedev.perceivecore.command.CommandTree
import com.perceivedev.perceivecore.command.DefaultCommandExecutor
import com.perceivedev.perceivecore.command.DefaultTabCompleter
import com.perceivedev.perceivecore.language.I18N
import me.ialistannen.clickablequiz.command.CommandQuiz
import me.ialistannen.clickablequiz.conversation.ConversationManager
import me.ialistannen.clickablequiz.data.player.CooldownManager
import me.ialistannen.clickablequiz.data.quiz.QuizManager
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

/**
 * The main class for the plugin
 */
class ClickableQuiz : JavaPlugin() {

    val language = I18N(this, "me.ialistannen.clickablequiz.language")

    /**
     * The [CooldownManager] this plugin instance uses
     */
    val cooldownManager = CooldownManager()

    /**
     * The [QuizManager] for all quizzes
     */
    var quizManager = QuizManager(config.getConfigurationSection("quizzes"))
        private set

    /**
     * The [ConversationManager] for all quizzes
     */
    val conversationManager = ConversationManager()

    companion object {
        lateinit var instance: ClickableQuiz
            private set

        /**
         * Translates a String
         *
         * @param key The key for the language file
         * @param formattingObjects The formatting objects
         */
        fun <T : Any> tr(key: String, vararg formattingObjects: T): String {
            return instance.language.translate(key, *formattingObjects)
        }

        /**
         * Translates a String without any formatting objects
         *
         * @param key The key for the language file
         */
        fun tr(key: String): String {
            return instance.language.translate(key)
        }
    }


    override fun onEnable() {
        instance = this

        I18N.copyDefaultFiles(this, false, "me.ialistannen.clickablequiz.language")

//        File(dataFolder, "config.yml").delete()

        saveDefaultConfig()

        reload()
    }

    override fun onDisable() {
        conversationManager.abandonAllConversations()
    }

    fun reload() {
        reloadConfig()

        // unregister command using old language file, to allow re-registering
        CommandSystemUtil.unregisterCommand(language.translate("command.quiz.keyword"))

        language.reload()
        language.language = Locale.forLanguageTag(config.getString("language"))
        
        logger.info("Using language '${language.language.displayName}'")

        quizManager = QuizManager(config.getConfigurationSection("quizzes"))

        initCommands()
    }

    private fun initCommands() {
        val tree = CommandTree()
        val executor = DefaultCommandExecutor(tree, language)
        val tabCompleter = DefaultTabCompleter(tree)

        val command = CommandQuiz()
        tree.addTopLevelChildAndRegister(command, executor, tabCompleter, this)
        tree.attachHelp(command, "quiz.help", language)
    }
}
