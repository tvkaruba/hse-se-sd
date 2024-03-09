package com.api.persistece

import java.util.UUID

sealed interface Repository<T> {
    interface Create<T> : Repository<T> {
        fun create(entity: T) : T?
    }

    interface Read<T> : Repository<T> {
        fun getAll() : List<T>
        fun getById(id: UUID) : T?
    }

    interface Update<T> : Repository<T> {
        fun update(entity: T) : T
    }

    interface Delete<T> : Repository<T> {
        fun deleteById(id: UUID) : Int
    }

    interface Crud<T> : Create<T>, Read<T>, Update<T>, Delete<T>
}