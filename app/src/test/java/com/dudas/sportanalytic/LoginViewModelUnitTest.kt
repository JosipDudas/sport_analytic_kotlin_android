package com.dudas.sportanalytic

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.dudas.sportanalytic.api.SportAnalyticService
import com.dudas.sportanalytic.api.response.GetLoginResponse
import com.dudas.sportanalytic.database.SportAnalyticDB
import com.dudas.sportanalytic.database.dao.UserDao
import com.dudas.sportanalytic.preferences.MyPreferences
import com.dudas.sportanalytic.ui.login.LoginViewModel
import com.nhaarman.mockitokotlin2.*
import io.kotlintest.mock.mock
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import retrofit2.mock.Calls
import java.io.IOException
import java.lang.IllegalStateException
import java.util.*

class LoginViewModelUnitTest {
    @Mock
    lateinit var progressObserver: Observer<in Boolean>
    @Mock
    lateinit var errorObserver: Observer<in Exception>
    @Mock
    lateinit var loginIsSuccessObserver: Observer<in Boolean>
    @Mock
    lateinit var startRegistrationActivityObserver: Observer<in Boolean>
    @Mock
    lateinit var enableLoginButtonObserver: Observer<in Boolean>
    @Mock
    lateinit var passwordObserver: Observer<in String>
    @Mock
    lateinit var emailObserver: Observer<in String>

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val mockPreferences =  mock<MyPreferences>()
    private val mockSportAnalyticService =  mock<SportAnalyticService>()
    private val mockConnector = mock<SportAnalyticDB>()

    private lateinit var classUnderTest: LoginViewModel

    @ObsoleteCoroutinesApi
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")


    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(mainThreadSurrogate)

        classUnderTest = LoginViewModel(
            mockPreferences,
            mockSportAnalyticService,
            mockConnector
        )
        classUnderTest.progress.observeForever(progressObserver)
        classUnderTest.error.observeForever(errorObserver)
        classUnderTest.loginIsSuccess.observeForever(loginIsSuccessObserver)
        classUnderTest.email.observeForever(emailObserver)
        classUnderTest.password.observeForever(passwordObserver)
        classUnderTest.enableLoginButton.observeForever(enableLoginButtonObserver)
        classUnderTest.startRegistrationActivity.observeForever(startRegistrationActivityObserver)
    }

    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun isFormValid_true_enableLoginButtonPostOnTrue() {
        // given
        val email = "test@foi.hr"
        val password= "test"
        classUnderTest.email.value = email
        classUnderTest.password.value = password

        // when
        classUnderTest.isFormValid()

        // then
        argumentCaptor<Boolean>{
            verify(enableLoginButtonObserver, times(1)).onChanged(capture())
            assertEquals(true, allValues[0])
        }
    }

    @Test
    fun isFormValid_emailIsIncorrect_enableLoginButtonPostOnFalse() {
        // given
        val email = "test"
        val password= "test"
        classUnderTest.email.value = email
        classUnderTest.password.value = password

        // when
        classUnderTest.isFormValid()

        // then
        argumentCaptor<Boolean>{
            verify(enableLoginButtonObserver, times(1)).onChanged(capture())
            assertEquals(false, allValues[0])
        }
    }

    @Test
    fun isFormValid_emailOrPasswordIsEmpty_enableLoginButtonPostOnFalse() {
        // given
        val password= "test"
        classUnderTest.password.value = password

        // when
        classUnderTest.isFormValid()

        // then
        argumentCaptor<Boolean>{
            verify(enableLoginButtonObserver, times(1)).onChanged(capture())
            assertEquals(false, allValues[0])
        }
    }

    @Test
    fun isFormValid_emailAndPasswordIsEmpty_enableLoginButtonPostOnFalse() {
        // given

        // when
        classUnderTest.isFormValid()

        // then
        argumentCaptor<Boolean>{
            verify(enableLoginButtonObserver, times(1)).onChanged(capture())
            assertEquals(false, allValues[0])
        }
    }

    @Test
    fun onRegisterClick_registerButtonIsClicked_startRegistrationActivityPostTrue() {
        // given

        // when
        classUnderTest.onRegisterClick()

        // then
        argumentCaptor<Boolean>{
            verify(startRegistrationActivityObserver, times(1)).onChanged(capture())
            assertEquals(true, allValues[0])
        }
    }

    @Test
    fun login_success_loginIsSuccessTrue() {
        runBlocking {
            // given
            val id = UUID.randomUUID().toString().toUpperCase()
            val getLoginResponse = GetLoginResponse()
            getLoginResponse.id = id
            getLoginResponse.firstname = "Test"
            getLoginResponse.lastname = "Test"
            getLoginResponse.password = "test"
            getLoginResponse.email = "test@foi.hr"
            getLoginResponse.position = "Admin"
            getLoginResponse.address = "Test"
            getLoginResponse.sex = "F"
            getLoginResponse.company_id = id
            getLoginResponse.status = true
            getLoginResponse.message = "Test"
            classUnderTest.email.value = "test@foi.hr"
            classUnderTest.password.value = "test"
            val mockUserDao = mock<UserDao>()

            // when
            whenever(mockSportAnalyticService.userLogin(any(), any())).thenReturn(Calls.response(getLoginResponse))
            whenever(mockConnector.userDao()).thenReturn(mockUserDao)
            classUnderTest.login()

            // then
            verify(errorObserver, never()).onChanged(any())
            verify(mockPreferences, times(1)).setUser(any())
            verify(mockConnector.userDao(), times(1)).insertUser(any())

            argumentCaptor<Boolean>{
                verify(progressObserver, times(2)).onChanged(capture())
                assertEquals(true, allValues[0])
                assertEquals(false, allValues[1])
            }

            argumentCaptor<Boolean>{
                verify(loginIsSuccessObserver, times(1)).onChanged(capture())
                assertEquals(true, allValues[0])
            }
        }
    }

    @Test
    fun login_exception_errorMessageShow() {
        runBlocking {
            // given

            // when
            whenever(mockSportAnalyticService.userLogin(any(), any())).thenReturn(Calls.failure(IOException()))
            classUnderTest.login()

            // then
            verify(loginIsSuccessObserver, never()).onChanged(any())
            verify(mockPreferences, never()).setUser(any())

            argumentCaptor<IOException>{
                verify(errorObserver, times(1)).onChanged(capture())
            }

            argumentCaptor<Boolean>{
                verify(progressObserver, times(2)).onChanged(capture())
                assertEquals(true, allValues[0])
                assertEquals(false, allValues[1])
            }
        }
    }

    @Test
    fun login_responseIsFailed_errorMessageShow() {
        runBlocking {
            // given
            val id = UUID.randomUUID().toString().toUpperCase()
            val getLoginResponse = GetLoginResponse()
            getLoginResponse.id = id
            getLoginResponse.firstname = "Test"
            getLoginResponse.lastname = "Test"
            getLoginResponse.password = "test"
            getLoginResponse.email = "test@foi.hr"
            getLoginResponse.position = "Admin"
            getLoginResponse.address = "Test"
            getLoginResponse.sex = "F"
            getLoginResponse.company_id = id
            getLoginResponse.status = false
            getLoginResponse.message = "Test"
            classUnderTest.email.value = "test@foi.hr"
            classUnderTest.password.value = "test"
            val mockUserDao = mock<UserDao>()

            // when
            whenever(mockSportAnalyticService.userLogin(any(), any())).thenReturn(Calls.response(getLoginResponse))
            whenever(mockConnector.userDao()).thenReturn(mockUserDao)
            classUnderTest.login()

            // then
            verify(mockPreferences, never()).setUser(any())
            verify(mockConnector.userDao(), never()).insertUser(any())

            argumentCaptor<Boolean>{
                verify(progressObserver, times(2)).onChanged(capture())
                assertEquals(true, allValues[0])
                assertEquals(false, allValues[1])
            }

            argumentCaptor<Boolean>{
                verify(loginIsSuccessObserver, times(1)).onChanged(capture())
                assertEquals(false, allValues[0])
            }

            argumentCaptor<IllegalStateException>{
                verify(errorObserver, times(1)).onChanged(capture())
            }
        }
    }
}