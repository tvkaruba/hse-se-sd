package spring.models.domain

import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "employee")
class EmployeeEntity(

        val firstName: String,

        var lastName: String? = null,

        @ManyToOne
        @JoinColumn(name = "department_id")
        val department: DepartmentEntity
) : BaseAuditEntity<Long>()