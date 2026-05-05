package ci.nsu.mobile.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.CalculationRepository
import ci.nsu.mobile.main.data.room.CalculationEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class DepositViewModel(private val repository: CalculationRepository) : ViewModel() {

    // Поток всех сохранённых расчётов (обновляется автоматически при изменении БД)
    val allCalculations: Flow<List<CalculationEntity>> = repository.allCalculations

    /**
     * Сохраняет результат расчёта в базу данных.
     */
    fun saveCalculation(
        initialAmount: Double,
        termMonths: Int,
        interestRate: Double,
        monthlyContribution: Double,
        totalAmount: Double,
        totalInterest: Double
    ) {
        viewModelScope.launch {
            val calculation = CalculationEntity(
                initialAmount = initialAmount,
                termMonths = termMonths,
                interestRate = interestRate,
                monthlyContribution = monthlyContribution,
                totalAmount = totalAmount,
                totalInterest = totalInterest
                // timestamp генерируется автоматически в Entity
            )
            repository.insert(calculation)
        }
    }

    /**
     * Рассчитывает итоговую сумму вклада с учётом ежемесячного пополнения.
     * @return Pair(totalAmount, totalInterest)
     */
    fun calculateResult(
        initialAmount: Double,
        termMonths: Int,
        interestRate: Double,
        monthlyContribution: Double
    ): Pair<Double, Double> {
        var total = initialAmount
        var interestEarned = 0.0

        val monthlyRate = interestRate / 100 / 12

        for (month in 1..termMonths) {
            val monthlyInterest = total * monthlyRate
            interestEarned += monthlyInterest
            total += monthlyInterest + monthlyContribution
        }

        return Pair(total, interestEarned)
    }

    /**
     * Возвращает процентную ставку в зависимости от срока вклада.
     * - < 6 месяцев → 15%
     * - >= 6 и < 12 месяцев → 10%
     * - >= 12 месяцев → 5%
     * - если срок не указан или <= 0 → 0%
     */
    fun getInterestRate(termMonths: Int?): Double {
        return when {
            termMonths == null || termMonths <= 0 -> 0.0
            termMonths < 6 -> 15.0
            termMonths < 12 -> 10.0
            else -> 5.0
        }
    }
}