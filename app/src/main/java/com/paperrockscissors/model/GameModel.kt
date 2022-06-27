package com.paperrockscissors.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class GameModel (
    var username1: String? = null,
    var username2: String? = null,
    var maxRounds: Int? = null,
    var round: Int?= null,
    var gameCode: Int? = null,
    var user1choice: Int? = null,
    var user2choice: Int? = null
) {
   /* @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "usernames" to usernames,
            "maxRounds" to maxRounds,
            "rounds" to rounds,
            "gameCode" to gameCode,
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GameModel

        if (!usernames.contentEquals(other.usernames)) return false
        if (maxRounds != other.maxRounds) return false
        if (!rounds.contentEquals(other.rounds)) return false
        if (gameCode != other.gameCode) return false

        return true
    }

    override fun hashCode(): Int {
        var result = usernames.contentHashCode()
        result = 31 * result + maxRounds!!
        result = 31 * result + rounds.contentHashCode()
        result = 31 * result + gameCode!!
        return result
    }*/
}
