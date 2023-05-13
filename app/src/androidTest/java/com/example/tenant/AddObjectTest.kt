package com.example.tenant

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.openLinkWithText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tenant.ui.view.MainActivity
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddObjectTest {
    @Rule
    @JvmField
    val activityTestRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun addNewObjectTest(){
        onView(withId(R.id.add_new_object)).perform(click())

        onView(withId(R.id.objectNameEditText))
            .perform(ViewActions.typeText("Flat on Baumana"))

        onView(withId(R.id.categoryList))
            .perform(click()).check(ViewAssertions.matches(withText("Квартира"))).perform(click())

        onView(withId(R.id.squareEditText))
            .perform(ViewActions.typeText("65"))

        onView(withId(R.id.addressEditText))
            .perform(ViewActions.typeText("Baumana 54"))

        onView(withId(R.id.addObjectButton)).perform(click())
    }
}