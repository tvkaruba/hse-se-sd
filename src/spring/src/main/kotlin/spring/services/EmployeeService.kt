package spring.services

import org.springframework.stereotype.Service
import spring.models.domain.EmployeeEntity
import spring.repositories.EmployeeRepository
import java.util.*

@Service
class EmployeeService(val db: EmployeeRepository) {
    fun findMessages(): List<EmployeeEntity>
        = db.findAll().toList();

    fun findMessageById(id: Long): List<EmployeeEntity>
        = db.findById(id).toList()

    fun save(message: EmployeeEntity)
        = db.save(message)

    fun delete(id: Long)
        = db.deleteById(id)

    fun <T : Any> Optional<out T>.toList(): List<T>
        = if (isPresent) listOf(get()) else emptyList()
}