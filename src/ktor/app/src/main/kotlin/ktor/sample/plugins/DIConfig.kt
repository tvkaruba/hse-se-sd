package ktor.sample.plugins

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

object ModulesConfig {
    private val userModule = Kodein.Module("USER") {
        bind() from singleton { UserController(instance()) }
        bind() from singleton { UserService(JwtProvider, instance()) }
        bind() from singleton { UserRepository() }
    }

    internal val kodein = Kodein {
        import(userModule)
    }
}