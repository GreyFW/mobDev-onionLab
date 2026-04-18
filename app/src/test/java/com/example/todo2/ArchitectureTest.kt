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
            .scopeFromProduction()
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
            .scopeFromProduction()
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
            .scopeFromProduction()
            .files
            .filter { it.moduleName.contains("auth") }
            .imports
            .assertFalse {
                it.name.contains("todo")
            }

        Konsist
            .scopeFromProduction()
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
            .scopeFromProduction()
            .classes()
            .withNameEndingWith("UseCase")
            .assertTrue {
                it.moduleName.contains("domain")
            }
    }

    @Test
    fun `repository interfaces should reside in domain and implementations in data`() {
        Konsist
            .scopeFromProduction()
            .interfaces()
            .withNameEndingWith("Repository")
            .assertTrue {
                it.moduleName.contains("domain")
            }

        // Проверяем классы-реализации репозиториев
        Konsist
            .scopeFromProduction()
            .classes()
            .withNameEndingWith("RepositoryImpl")
            .assertTrue {
                it.moduleName.contains("data")
            }
    }
}