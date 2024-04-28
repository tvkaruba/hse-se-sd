package spring.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import spring.models.domain.EmployeeEntity
import spring.services.EmployeeService
import java.util.NoSuchElementException

@RestController
@RequestMapping("/api/employee")
class EmployeeController (val service: EmployeeService) {

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(e: NoSuchElementException): ResponseEntity<String>
        = ResponseEntity(e.message, HttpStatus.NOT_FOUND)

    @GetMapping()
    fun index(): List<EmployeeEntity> = service.findMessages()

    @GetMapping("/{id}")
    fun index(@PathVariable id: Long): List<EmployeeEntity>
        = service.findMessageById(id)

    @PostMapping()
    fun post(@RequestBody message: EmployeeEntity) {
        service.save(message)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        service.delete(id)
    }
}