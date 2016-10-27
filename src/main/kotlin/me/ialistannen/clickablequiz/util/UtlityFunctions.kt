package me.ialistannen.clickablequiz.util

import com.perceivedev.perceivecore.util.JSONMessage
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import java.util.regex.Pattern

/**
 * Contains some utility extension functions
 */


//<editor-fold desc="Color">
/********************************************************************
 *
 *                             Color
 *
 ********************************************************************/

/**
 * Colors a string.
 *
 * Uses '&' as color char
 */
fun String.color(): String {
    return ChatColor.translateAlternateColorCodes('&', this)
}

/**
 * Strips colors from a string.
 *
 * @return The String without color
 */
fun String.stripColor(): String {
    return ChatColor.stripColor(this)
}
//</editor-fold>


//<editor-fold desc="JSON Messages">
/********************************************************************
 *
 *                             JSON Messages
 *
 ********************************************************************/

/**
 * Makes it clickable
 *
 * @param command The command to run on clicking it
 *
 * @return A [JSONMessage] with the specified command
 */
fun String.getRunCommand(command: String): JSONMessage {
    return JSONMessage.create(this).runCommand(command)
}

/**
 * Sends the [Player] the given [JSONMessage]
 *
 * @param message The [JSONMessage] to send
 */
fun Player.sendJsonMessage(message: JSONMessage) {
    message.send(this)
}
//</editor-fold>

//<editor-fold desc="Quoted Text">
private val pattern = Pattern.compile(""""(.+?)"""", Pattern.CASE_INSENSITIVE)

/**
 * @return The first text enclosed in double quotes.
 */
fun String.getFirstQuotedText(): String? {
    val matcher = pattern.matcher(this)

    if (!matcher.matches()) {
        return null
    }

    return matcher.group(1)
}
//</editor-fold>
