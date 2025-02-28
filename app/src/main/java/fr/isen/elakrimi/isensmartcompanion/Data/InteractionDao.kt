package fr.isen.elakrimi.isensmartcompanion.Data

import androidx.room.*

@Dao
interface InteractionDao {
    @Insert
    suspend fun insertInteraction(interaction: Interaction) // ✅ Retourne `Long` (ID généré)

    @Query("SELECT * FROM interactions ORDER BY date DESC")
    fun getAllInteractions(): List<Interaction>

    @Delete
    suspend fun deleteInteraction(interaction: Interaction) // ✅ Retourne `Int` (nombre supprimé)

    @Query("DELETE FROM interactions")
    fun deleteAllInteractions() // ✅ Supprime tout et retourne `Int`

    @Query("SELECT * FROM interactions WHERE question = :question ORDER BY date DESC LIMIT 1")
    suspend fun getLastInteraction(question: String): Interaction?
}