package com.dudas.sportanalytic

import android.content.ComponentName
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.dudas.sportanalytic.ui.login.LoginActivity
import com.dudas.sportanalytic.ui.main.MainActivity
import com.dudas.sportanalytic.ui.registration.RegistrationActivity
import junit.framework.TestCase.assertEquals
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matchers
import org.hamcrest.core.Is
import org.junit.*
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginActivityUITest {

    companion object {
        const val VALID_EMAIL = "josdudas@gmail.com"
        const val VALID_PASS = "josip"
        const val INVALID_EMAIL = "test@gmail.com"
        const val INVALID_PASS = "test"
        const val INVALID_EMAIL_FORMAT = "test@foi"
    }

    @get:Rule
    val intentsTestRule = IntentsTestRule(LoginActivity::class.java)

    @Before
    fun before() {
        IdlingRegistry.getInstance().register(intentsTestRule.activity.mIdlingRes)
    }

    @After
    fun after() {
        IdlingRegistry.getInstance().unregister(intentsTestRule.activity.mIdlingRes)
    }

    @Test
    fun login_validEmailAndPassword_SuccessLogin() {
        // Given

        //When
        onView(withId(R.id.user_email)).check(matches(isDisplayed()))
        onView(withId(R.id.user_email)).check(matches(isEnabled()))

        onView(withId(R.id.user_password)).check(matches(isDisplayed()))
        onView(withId(R.id.user_password)).check(matches(isEnabled()))

        onView(withId(R.id.user_login)).check(matches(isDisplayed()))
        onView(withId(R.id.user_login)).check(matches(not(isEnabled())))

        onView(withId(R.id.txt_registration)).check(matches(isDisplayed()))

        onView(withId(R.id.txt_register)).check(matches(isDisplayed()))
        onView(withId(R.id.txt_register)).check(matches(isEnabled()))

        onView(withId(R.id.user_email)).perform(clearText(),typeText(VALID_EMAIL))
        onView(withId(R.id.user_login)).check(matches(not(isEnabled())))
        onView(withId(R.id.user_password)).perform(clearText(),typeText(VALID_PASS))
        onView(withId(R.id.user_login)).check(matches(isEnabled()))

        onView(withId(R.id.user_login)).perform(click())

        intended(hasComponent(ComponentName(intentsTestRule.activity, MainActivity::class.java)))

        onView(withText(R.string.location_is_unknown)).check(matches(isDisplayed()))

        // Then
        assertEquals(VALID_EMAIL, intentsTestRule.activity.preferences.getUser()!!.email)

        onView(withContentDescription("Navigate up")).perform(click())

        Thread.sleep(1000)

        onView(withText(R.string.logout)).perform(click())

        intended(hasComponent(ComponentName(intentsTestRule.activity, LoginActivity::class.java)))
    }

    @Test
    fun login_invalidEmailAndPassword_unsuccessfulLogin() {
        // Given

        //When
        onView(withId(R.id.user_email)).check(matches(isDisplayed()))
        onView(withId(R.id.user_email)).check(matches(isEnabled()))

        onView(withId(R.id.user_password)).check(matches(isDisplayed()))
        onView(withId(R.id.user_password)).check(matches(isEnabled()))

        onView(withId(R.id.user_login)).check(matches(isDisplayed()))
        onView(withId(R.id.user_login)).check(matches(not(isEnabled())))

        onView(withId(R.id.txt_registration)).check(matches(isDisplayed()))

        onView(withId(R.id.txt_register)).check(matches(isDisplayed()))
        onView(withId(R.id.txt_register)).check(matches(isEnabled()))

        onView(withId(R.id.user_email)).perform(clearText(),typeText(INVALID_EMAIL))
        onView(withId(R.id.user_login)).check(matches(not(isEnabled())))
        onView(withId(R.id.user_password)).perform(clearText(),typeText(INVALID_PASS))
        onView(withId(R.id.user_login)).check(matches(isEnabled()))

        onView(withId(R.id.user_login)).perform(click())

        onView(withText(R.string.invalid_email_or_password)).inRoot(RootMatchers.withDecorView(Matchers.not(Is.`is`(intentsTestRule.activity.window.decorView)))).check(matches(isDisplayed()))

        // Then
        assertEquals(null, intentsTestRule.activity.preferences.getUser())
    }

    @Test
    fun login_invalidEmailFormat_loginButtonDisabled() {
        // Given

        //When
        onView(withId(R.id.user_email)).check(matches(isDisplayed()))
        onView(withId(R.id.user_email)).check(matches(isEnabled()))

        onView(withId(R.id.user_password)).check(matches(isDisplayed()))
        onView(withId(R.id.user_password)).check(matches(isEnabled()))

        onView(withId(R.id.user_login)).check(matches(isDisplayed()))
        onView(withId(R.id.user_login)).check(matches(not(isEnabled())))

        onView(withId(R.id.txt_registration)).check(matches(isDisplayed()))

        onView(withId(R.id.txt_register)).check(matches(isDisplayed()))
        onView(withId(R.id.txt_register)).check(matches(isEnabled()))

        onView(withId(R.id.user_email)).perform(clearText(),typeText(INVALID_EMAIL_FORMAT))
        onView(withId(R.id.user_login)).check(matches(not(isEnabled())))
        onView(withId(R.id.user_password)).perform(clearText(),typeText(INVALID_PASS))
        onView(withId(R.id.user_login)).check(matches(not(isEnabled())))

       // Then
        assertEquals(null, intentsTestRule.activity.preferences.getUser())
    }

    @Test
    fun login_registerClick_openRegisterActivity() {
        // Given

        //When
        onView(withId(R.id.user_email)).check(matches(isDisplayed()))
        onView(withId(R.id.user_email)).check(matches(isEnabled()))

        onView(withId(R.id.user_password)).check(matches(isDisplayed()))
        onView(withId(R.id.user_password)).check(matches(isEnabled()))

        onView(withId(R.id.user_login)).check(matches(isDisplayed()))
        onView(withId(R.id.user_login)).check(matches(not(isEnabled())))

        onView(withId(R.id.txt_registration)).check(matches(isDisplayed()))

        onView(withId(R.id.txt_register)).check(matches(isDisplayed()))
        onView(withId(R.id.txt_register)).check(matches(isEnabled()))

        onView(withId(R.id.user_email)).perform(clearText(),typeText(INVALID_EMAIL))
        onView(withId(R.id.user_password)).perform(clearText(),typeText(INVALID_PASS))

        onView(withId(R.id.txt_register)).perform(click())

        intended(hasComponent(ComponentName(intentsTestRule.activity, RegistrationActivity::class.java)))

        // Then
        assertEquals(null, intentsTestRule.activity.preferences.getUser())
    }
}