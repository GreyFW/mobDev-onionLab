package com.example.todo2

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.list.imports
import com.lemonappdev.konsist.api.ext.list.withNameEndingWith
import com.lemonappdev.konsist.api.verify.assertFalse
import com.lemonappdev.konsist.api.verify.assertTrue
import org.junit.Test

class ArchitectureTest {

    @Test
    fun `domain modules should not import android framework`() {
        Konsist
            .scopeFromProject()
            .files
            .filter { it.moduleName.contains("domain") }
            .imports
            .assertFalse {
                it.name.startsWith("android.") || it.name.startsWith("androidx.")
            }
    }

    @Test
    fun `data modules should not import compose UI components`() {
        Konsist
            .scopeFromProject()
            .files
            .filter { it.moduleName.contains("data") }
            .imports
            .assertFalse {
                it.name.startsWith("androidx.compose")
            }
    }

    @Test
    fun `feature modules should not depend on each other`() {
        Konsist
            .scopeFromProject()
            .files
            .filter { it.moduleName.contains("auth") }
            .imports
            .assertFalse {
                it.name.contains("todo")
            }

        Konsist
            .scopeFromProject()
            .files
            .filter { it.moduleName.contains("todo") }
            .imports
            .assertFalse {
                it.name.contains("auth")
            }
    }

    @Test
    fun `use cases should reside in domain module`() {
        Konsist
            .scopeFromProject()
            .classes()
            .withNameEndingWith("UseCase")
            .assertTrue {
                it.resideInModule("..domain..")
            }
    }

    @Test
    fun `repository interfaces should reside in domain and implementations in data`() {
        // Проверяем интерфейсы репозиториев
        Konsist
            .scopeFromProject()
            .interfaces()
            .withNameEndingWith("Repository")
            .assertTrue {
                it.resideInModule("..domain..")
            }

        // Проверяем классы-реализации репозиториев
        Konsist
            .scopeFromProject()
            .classes()
            .withNameEndingWith("RepositoryImpl")
            .assertTrue {
                it.resideInModule("..data..")
            }
    }
}