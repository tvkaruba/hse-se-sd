package spring.models.domain

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseAuditEntity<T> : BaseEntity<T>() {

    @CreatedDate
    @Column(updatable = false, nullable = false)
    lateinit var created: LocalDateTime

    @LastModifiedDate
    @Column(nullable = false)
    lateinit var modified: LocalDateTime
}