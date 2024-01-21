package com.endcodev.saber_y_beber.domain

import com.endcodev.saber_y_beber.data.database.entities.toDB
import com.endcodev.saber_y_beber.domain.model.QuestModel
import com.endcodev.saber_y_beber.data.repository.GameRepository
import com.endcodev.saber_y_beber.domain.usecases.GetQuestUseCase

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class GetQuestUseCaseTest {

    @Mock
    private lateinit var repository: GameRepository

    private lateinit var getQuestUseCase: GetQuestUseCase

    private val challenge = QuestModel(quest = "Challenge", author = "author", difficulty = 1, option1 = "option1", option2 = "option2", option3 = "option3", post = true, correctors = ArrayList())
    private val challenge2 = QuestModel(quest = "Challenge2", author = "author2", difficulty = 2, option1 = "2option1", option2 = "2option2", option3 = "3option3", post = true, correctors = ArrayList())

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        getQuestUseCase = GetQuestUseCase(repository)
    }

    @Test
    fun `invoke should return quest list from API`() = runBlocking {
        // Arrange
        val questList = listOf(challenge, challenge2)
        `when`(repository.getAllQuestFromApi()).thenReturn(questList)

        // Act
        val result = getQuestUseCase.invoke()

        // Assert
        assertEquals(questList, result)
    }

    @Test
    fun `invoke should return quest list from DB when API call empty`() = runBlocking {
        // Arrange
        val questList = listOf(challenge, challenge2)
        `when`(repository.getAllQuestFromApi()).thenReturn(emptyList())
        `when`(repository.getAllQuestFromDB()).thenReturn(questList)

        // Act
        val result = getQuestUseCase.invoke()

        // Assert
        assertEquals(questList, result)
    }

    @Test
    fun `invoke should update DB with new quests when API call succeeds`() = runBlocking {
        // Arrange
        val questList = listOf(challenge, challenge2)
        `when`(repository.getAllQuestFromApi()).thenReturn(questList)

        // Act
        val result = getQuestUseCase.invoke()

        // Assert
        assertEquals(questList, result)
        verify(repository).clearQuest()
        verify(repository).insertQuest(questList.map { it.toDB() })
    }
}