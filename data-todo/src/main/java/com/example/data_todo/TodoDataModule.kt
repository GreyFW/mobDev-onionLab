package com.example.data_todo

import com.example.domain_todo.repository.ITodoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TodoDataModule {

    @Binds
    @Singleton
    abstract fun bindTodoRepository(
        impl: TodoRepositoryImpl
    ): ITodoRepository
}