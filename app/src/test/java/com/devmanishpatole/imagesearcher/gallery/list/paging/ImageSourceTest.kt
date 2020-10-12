package com.devmanishpatole.imagesearcher.gallery.list.paging

import com.devmanishpatole.imagesearcher.exception.NetworkException
import com.devmanishpatole.imagesearcher.gallery.list.service.ImgurService
import com.devmanishpatole.imagesearcher.model.Image
import com.devmanishpatole.imagesearcher.model.ImageData
import com.devmanishpatole.imagesearcher.model.ImageWrapper
import com.devmanishpatole.imagesearcher.model.PhotoRequest
import com.devmanishpatole.imagesearcher.util.NetworkHelper
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class ImageSourceTest {

    private lateinit var source: ImageSource

    @MockK
    lateinit var service: ImgurService

    @MockK
    lateinit var networkHelper: NetworkHelper

    @MockK
    lateinit var request: PhotoRequest

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        source = ImageSource(service, networkHelper, request)
    }

    @Test
    fun loadFromNetwork_whenNoInternet_shouldThrowException() = runBlocking {
        every { networkHelper.isNetworkConnected() } returns false
        try {
            source.loadFromNetwork()
            fail()
        } catch (e: Exception) {
            assertTrue(e is NetworkException)
        }
    }

    @Test
    fun loadFromNetwork_withResponseFailure_shouldThrowException() = runBlocking {
        every { networkHelper.isNetworkConnected() } returns true

        val response = mockk<Response<ImageWrapper>>()
        every { response.isSuccessful } returns false

        try {
            source.loadFromNetwork()
            fail()
        } catch (e: Exception) {
            assertNotNull(e)
        }
    }

    @Test
    fun loadFromNetwork_withSuccessResponse_shouldReturnData() = runBlocking {
        every { networkHelper.isNetworkConnected() } returns true
        every { request.query } returns "test"

        val response = mockk<Response<ImageWrapper>>()
        every { response.isSuccessful } returns true
        every { response.body() } returns getFakeImageData()

        coEvery { service.searchImages("test", 0) } returns response

        val data = source.loadFromNetwork()

        assertNotNull(data)

        assertEquals(2, data?.size)
        assertEquals(2, data?.get(0)?.images?.size)
        assertEquals(2, data?.get(1)?.images?.size)

        assertEquals(123, data?.get(0)?.datetime)
        assertEquals(456, data?.get(1)?.datetime)

        assertEquals("Spv4xjP", data?.get(0)?.id)
        assertEquals("gUeUj", data?.get(1)?.id)

        assertEquals("https://imgur.com/a/Spv4xjP", data?.get(0)?.link)
        assertEquals("https://imgur.com/a/gUeUj", data?.get(1)?.link)

        assertEquals("Spv4xjP title", data?.get(0)?.title)
        assertEquals("gUeUj title", data?.get(1)?.title)

        assertEquals("Spv4xjP description", data?.get(0)?.description)
        assertEquals("gUeUj description", data?.get(1)?.description)

        assertEquals(100, data?.get(0)?.ups)
        assertEquals(1000, data?.get(1)?.ups)

        assertEquals(50, data?.get(0)?.downs)
        assertEquals(500, data?.get(1)?.downs)

        assertEquals(1000, data?.get(0)?.score)
        assertEquals(100, data?.get(1)?.score)

        assertTrue(data?.get(0)?.isViral ?: false)
        assertFalse(data?.get(1)?.isViral ?: true)
    }

    @Test
    fun loadFromNetwork_withSuccessResponse_andIncorrectData_shouldThrowException() = runBlocking {
        every { networkHelper.isNetworkConnected() } returns true
        every { request.query } returns "test"

        val response = mockk<Response<ImageWrapper>>()
        every { response.isSuccessful } returns true
        every { response.body() } returns getFakeFailureData()

        coEvery { service.searchImages("test", 0) } returns response

        try {
            source.loadFromNetwork()
            fail()
        } catch (e: Exception) {
            assertNotNull(e)
        }
    }


    private fun getFakeFailureData() = ImageWrapper(listOf(), false, 200)

    private fun getFakeImageData(): ImageWrapper {
        val imageList1 = listOf(
            Image("pF3tqRs", "https://i.imgur.com/pF3tqRs.jpg", "pF3tqRs Description"),
            Image("8EpZYJl", "https://i.imgur.com/8EpZYJl.jpg", "8EpZYJl Description")
        )
        val imageList2 = listOf(
            Image("7uFNtV6", "https://i.imgur.com/7uFNtV6.jpg", "7uFNtV6 Description"),
            Image("0zpPg06", "https://i.imgur.com/0zpPg06.jpg", "0zpPg06 Description")
        )

        val imageDataList = listOf(
            ImageData(
                123, "Spv4xjP", "https://imgur.com/a/Spv4xjP", "Spv4xjP title",
                "Spv4xjP description", 100, 50, 1000, true, imageList1
            ),
            ImageData(
                456, "gUeUj", "https://imgur.com/a/gUeUj", "gUeUj title",
                "gUeUj description", 1000, 500, 100, false, imageList2
            )
        )

        return ImageWrapper(imageDataList, true, 200)
    }

    @After
    fun tearDown() = clearAllMocks()
}