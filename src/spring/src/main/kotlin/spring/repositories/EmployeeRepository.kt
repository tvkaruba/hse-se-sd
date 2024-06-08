package spring.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import spring.models.domain.EmployeeEntity

@Repository
interface EmployeeRepository : JpaRepository<EmployeeEntity, Long>