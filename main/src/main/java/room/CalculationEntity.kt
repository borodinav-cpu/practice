package ci.nsu.mobile.main.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calculations")
data class CalculationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val timestamp: Long = System.currentTimeMillis(),

    val initialAmount: Double,           // Стартовый взнос
    val termMonths: Int,                 // Срок в месяцах
    val interestRate: Double,            // Процентная ставка
    val monthlyContribution: Double,     // Ежемесячное пополнение

    val totalAmount: Double,             // Итоговая сумма
    val totalInterest: Double            // Начисленные проценты
)