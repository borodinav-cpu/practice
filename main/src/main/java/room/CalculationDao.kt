package ci.nsu.mobile.main.data.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CalculationDao {
    @Insert
    suspend fun insert(calculation: CalculationEntity)

    @Query("SELECT * FROM calculations ORDER BY timestamp DESC")
    fun getAllCalculations(): Flow<List<CalculationEntity>>

    @Query("SELECT * FROM calculations WHERE id = :id")
    suspend fun getCalculationById(id: Int): CalculationEntity?

    @Delete
    suspend fun delete(calculation: CalculationEntity)
}