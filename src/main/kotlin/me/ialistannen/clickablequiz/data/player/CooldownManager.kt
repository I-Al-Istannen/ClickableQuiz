package me.ialistannen.clickablequiz.data.player

import me.ialistannen.clickablequiz.ClickableQuiz
import me.ialistannen.clickablequiz.data.quiz.Quiz
import org.apache.commons.lang.time.DurationFormatUtils
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*

/**
 * Manages the player cooldown
 */
class CooldownManager {
    val playerCooldownMap: MutableMap<UUID, QuizCooldowns> = HashMap()

    //<editor-fold desc="Normal methods">
    /********************************************************************
     *
     *                         Normal methods
     *
     ********************************************************************/

    /**
     * Adds the player
     *
     * @param playerId The [UUID] of the player
     * @param endTime The time the cooldown ends
     * @param quiz The quiz it belongs to
     */
    fun add(playerId: UUID, endTime: LocalDateTime, quiz: Quiz) {
        if (playerId !in playerCooldownMap) {
            playerCooldownMap.put(playerId, QuizCooldowns())
        }

        playerCooldownMap[playerId]!!.add(quiz, endTime)
    }

    /**
     * Adds the player
     *
     * @param playerId The [UUID] of the player
     * @param duration The Duration of the cooldown
     * @param quiz The quiz it belongs to
     */
    fun add(playerId: UUID, duration: Duration, quiz: Quiz) {
        add(playerId, LocalDateTime.now().plus(duration), quiz)
    }

    /**
     * Checks if the player is on cooldown
     *
     * @param uuid The [UUID] of the player
     * @return `true` if the player is on cooldown
     */
    fun isOnCooldown(uuid: UUID, quiz: Quiz): Boolean {
        return playerCooldownMap[uuid]?.isOnCooldown(quiz) ?: false
    }
    //</editor-fold>

    //<editor-fold desc="Operator overloads">
    /********************************************************************
     *
     *                         Operator overloads
     *
     ********************************************************************/

    /**
     * Gets the end time for the [Player] with the given [UUID]
     *
     * @param uuid The [UUID] to get if for
     */
    operator fun get(uuid: UUID) = playerCooldownMap[uuid]

    /**
     * Gets the end time for the [Player]
     *
     * @param player The [Player] to get if for
     */
    operator fun get(player: OfflinePlayer) = playerCooldownMap[player.uniqueId]
    //</editor-fold>


    //<editor-fold desc="QuizCooldowns Helper class">
    /********************************************************************
     *
     *                            Helper class
     *
     ********************************************************************/

    class QuizCooldowns() {
        val quizMap = HashMap<Quiz, LocalDateTime>()

        /**
         * Adds the player
         *
         * @param endTime The time the cooldown ends
         * @param quiz The quiz it belongs to
         */
        fun add(quiz: Quiz, endTime: LocalDateTime) = quizMap.put(quiz, endTime)

        /**
         * Adds the player
         *
         * @param quiz The quiz it belongs to
         * @param duration The Duration of the cooldown
         */
        fun add(quiz: Quiz, duration: Duration) = add(quiz, LocalDateTime.now().plus(duration))

        /**
         * Checks if the player is on cooldown
         *
         * @param uuid The [UUID] of the player
         * @return `true` if the player is on cooldown
         */
        fun isOnCooldown(quiz: Quiz): Boolean {
            return quizMap[quiz]?.isAfter(LocalDateTime.now()) ?: false
        }

        //<editor-fold desc="Operator overloads">
        /********************************************************************
         *
         *                         Operator overloads
         *
         ********************************************************************/

        /**
         * Gets the end time for the given [Quiz]
         *
         * @param quiz The quiz to get the cooldown for
         * @return The Cooldown for the quiz, if any
         */
        operator fun get(quiz: Quiz): LocalDateTime? = quizMap[quiz]

        /**
         * Checks if the given quiz is contained in this Cooldown
         *
         * @param quiz THe [Quiz] to check
         */
        operator fun contains(quiz: Quiz) = quizMap.containsKey(quiz)
        //</editor-fold>
    }
    //</editor-fold>
}

//<editor-fold desc="Extension Functions">
/********************************************************************
 *
 *                         Extension Functions
 *
 ********************************************************************/

/**
 * Checks if the player is on cooldown
 *
 *
 * @param quiz The [Quiz] the cooldown is for
 *
 * @return `true` if the player is on cooldown
 */
fun Player.isOnCooldown(quiz: Quiz): Boolean {
    return ClickableQuiz.instance.cooldownManager.isOnCooldown(uniqueId, quiz)
}

/**
 * Returns the length of the player cooldown in words
 *
 * @param quiz The [Quiz] the cooldown is for
 *
 * @return The remaining cooldown time in words
 */
fun Player.getCooldownTimeLeftWords(quiz: Quiz): String {
    if (!ClickableQuiz.instance.cooldownManager.isOnCooldown(uniqueId, quiz)) {
        return ""
    }
    // I checked that before, so the '!!' should never be fired
    val endTime = ClickableQuiz.instance.cooldownManager[uniqueId]!![quiz]!!
    return DurationFormatUtils.formatDurationWords(ChronoUnit.MILLIS.between(LocalDateTime.now(), endTime), true, true)
}

/**
 * Adds the cooldown for the player
 *
 * @param duration The duration of the cooldown
 * @param quiz The [Quiz] the cooldown is for
 */
fun Player.addCooldown(duration: Duration, quiz: Quiz) {
    ClickableQuiz.instance.cooldownManager.add(uniqueId, duration, quiz)
}
//</editor-fold>
