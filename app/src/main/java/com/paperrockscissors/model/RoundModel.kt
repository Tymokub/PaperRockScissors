package com.paperrockscissors.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class RoundModel (
    val user1: FigureEnum? = null,
    val user2: FigureEnum? = null,
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "user1" to user1,
            "user2" to user2,
        )
    }
}
