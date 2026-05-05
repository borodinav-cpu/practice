package ci.nsu.mobile.main.data

import ci.nsu.mobile.main.data.room.CalculationDao
import ci.nsu.mobile.main.data.room.CalculationEntity
import kotlinx.coroutines.flow.Flow

class CalculationRepository(private val dao: CalculationDao) {

    val allCalculations: Flow<List<CalculationEntity>> = dao.getAllCalculations()

    suspend fun insert(calculation: CalculationEntity) {
        dao.insert(calculation)
    }

    suspend fun getCalculationById(id: Int): CalculationEntity? {
        return dao.getCalculationById(id)
    }
}