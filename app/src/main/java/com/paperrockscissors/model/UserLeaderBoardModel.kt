package com.paperrockscissors.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class UserLeaderBoardModel (
    val username: String? = null,
    val userId: String? = null,
    val roundsWon: Int? = null,
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "username" to username,
            "roundsWon" to roundsWon,
            "userId" to userId,
        )
    }
}
