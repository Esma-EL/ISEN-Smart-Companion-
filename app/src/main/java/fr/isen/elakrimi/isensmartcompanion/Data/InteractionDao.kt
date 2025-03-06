package fr.isen.elakrimi.isensmartcompanion.Data

import androidx.room.*

@Dao
interface InteractionDao {
    @Insert
    suspend fun insertInteraction(interaction: Interaction) // ID généré

    @Query("SELECT * FROM interactions ORDER BY date DESC")
    fun getAllInteractions(): List<Interaction>

    @Delete
    suspend fun deleteInteraction(interaction: Interaction)

    @Query("DELETE FROM interactions")
    fun deleteAllInteractions()

    @Query("SELECT * FROM interactions WHERE question = :question ORDER BY date DESC LIMIT 1")
    suspend fun getLastInteraction(question: String): Interaction?


    @Update
    suspend fun updateInteraction(interaction: Interaction)
}