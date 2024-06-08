package spring.test.repositories

import org.junit.Assert
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import spring.models.domain.DepartmentEntity
import spring.models.domain.EmployeeEntity
import spring.repositories.DepartmentRepository
import spring.test.configuration.PostgresTestContainersInitializer

@ActiveProfiles("test")
@RunWith(SpringRunner::class)
@DataJpaTest
@ContextConfiguration(initializers = [PostgresTestContainersInitializer::class])
class DepartmentRepositoryTests {

    @Autowired
    protected lateinit var departmentRepository: DepartmentRepository

    @Test
    fun saveAndUpdateDepartment() {
        val id = departmentRepository.save(
            DepartmentEntity(name = "Department One")).id!!

        Assert.assertEquals(1, departmentRepository.count())

        val savedDepartment = departmentRepository.findById(id).get()
        savedDepartment.addEmployee { EmployeeEntity(department = this, firstName = "Ivan", lastName = "Petrov") }
        savedDepartment.addEmployee { EmployeeEntity(department = this, firstName = "Masha", lastName = "Ivanova") }
        departmentRepository.save(savedDepartment)
        val updatedDepartment = departmentRepository.findById(id).get()

        Assert.assertEquals("Department One", updatedDepartment.name)
        Assert.assertEquals(2, updatedDepartment.employees.size)
    }

    @Test
    fun `test find one by name`() {
        departmentRepository.saveAll(
            listOf(
                DepartmentEntity(name = "Department One"),
                DepartmentEntity(name = "Department Two")))

        Assert.assertEquals(2, departmentRepository.count())

        val department = departmentRepository.findOneByName("Department One")

        Assert.assertEquals("Department One", department.name)
    }
}