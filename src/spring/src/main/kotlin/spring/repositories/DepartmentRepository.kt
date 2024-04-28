package spring.repositories

import org.springframework.data.jpa.repository.JpaRepository
import spring.models.domain.DepartmentEntity

interface DepartmentRepository : JpaRepository<DepartmentEntity, Long> {
    fun findOneByName(name: String) : DepartmentEntity
}