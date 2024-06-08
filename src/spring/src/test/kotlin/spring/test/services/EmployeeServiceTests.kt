package spring.test.services

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import spring.models.domain.DepartmentEntity
import spring.models.domain.EmployeeEntity
import spring.repositories.EmployeeRepository
import spring.services.EmployeeService
import java.util.*

@ActiveProfiles("test")
@RunWith(SpringRunner::class)
@SpringBootTest
class EmployeeServiceTest {

//    @Mock
//    lateinit var employeeRepository: EmployeeRepository

//    @InjectMocks
//    lateinit var employeeService: EmployeeService

    private val employeeRepositoryMock: EmployeeRepository = mock()

    private val employeeService = EmployeeService(employeeRepositoryMock)

    @Test
    fun findMessageById_tryFindsExistedMessage_succeed() {

        val department = DepartmentEntity(name = "department")
        val employee = EmployeeEntity(firstName = "name", department = department)

        `when`(employeeRepositoryMock.findById(any())).thenReturn(Optional.of(employee))

        val messageId = 42L
        val result = employeeService.findMessageById(messageId)

        assertNotNull(result)
        assertEquals(result, listOf(employee))

        verify(employeeRepositoryMock, times(1)).findById(any())
    }
}