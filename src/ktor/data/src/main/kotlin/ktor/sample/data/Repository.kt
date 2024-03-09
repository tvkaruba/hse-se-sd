package ktor.sample.data

sealed interface Repository<T> {
    interface Create<T> : Repository<T> {
        suspend fun create(entity: T) : T?
    }

    interface Read<T> : Repository<T> {
        suspend fun getAll(page: Int, limit: Int) : List<T>
        suspend fun getById(id: Int) : T?
    }

    interface Update<T> : Repository<T> {
        suspend fun update(entity: T) : T
        suspend fun transform(transformer: (T) -> T) : T
    }

    interface Delete<T> : Repository<T> {
        suspend fun deleteById(id: String) : T
    }

    interface Crud<T> : Create<T>, Read<T>, Update<T>, Delete<T>
}