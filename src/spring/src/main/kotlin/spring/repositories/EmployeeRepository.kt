package spring.repositories

import org.springframework.data.jpa.repository.JpaRepository
import spring.models.domain.EmployeeEntity

interface EmployeeRepository : JpaRepository<EmployeeEntity, Long>