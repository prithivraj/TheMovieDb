package com.robdir.themoviedb.domain.movies

import com.robdir.themoviedb.MockDataProvider.createMockMovie
import com.robdir.themoviedb.MockDataProvider.createMockMovieEntity
import com.robdir.themoviedb.MockDataProvider.mockMovieGenreIds
import com.robdir.themoviedb.MockDataProvider.mockMovieGenreMap
import com.robdir.themoviedb.MockDataProvider.mockMovieGenreNames
import com.robdir.themoviedb.domain.GenreNameMapperTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock

class MovieMapperTest {

    private val mockGenreNameMapper = mock(GenreNameMapperTest::class.java)
    private val movieMapper = MovieMapper(mockGenreNameMapper)

    private val movieEntity = createMockMovieEntity()
    private val movie = createMockMovie()


    @Before
    fun setup() {
        given(mockGenreNameMapper.toNameList(mockMovieGenreIds, mockMovieGenreMap))
            .willReturn(mockMovieGenreNames)
    }

    @Test
    fun `WHEN toDomainModel is called AND a list of MovieEntity is received THEN a list of Movie is returned`() {
        // Arrange, Act & Assert
        assertEquals(listOf(movie), movieMapper.toDomainModel(listOf(movieEntity), mockMovieGenreMap))
    }

    @Test
    fun `WHEN toDomainModel is called AND a MovieEntity is received THEN a Movie is returned`() {
        // Arrange, Act & Assert
        assertEquals(movie, movieMapper.toDomainModel(movieEntity, mockMovieGenreMap))
    }
}
