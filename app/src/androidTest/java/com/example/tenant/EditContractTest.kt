package com.example.tenant

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tenant.ui.view.MainActivity
import com.example.tenant.ui.view.ObjectsAdapter
import org.hamcrest.Matchers
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EditContractTest {
    @Rule
    @JvmField
    val mainActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun editContract(){
        onView(withId(R.id.objectsRecycler))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<ObjectsAdapter.ViewHolder>(2,
                    click()
                ))

        onView(withId(R.id.addContractButton)).perform(click())
        onView(withId(R.id.addUserButton)).perform(ViewActions.scrollTo()).perform(click())

        onView(withId(R.id.sum)).perform(clearText(), typeText("23000"))

        onView(withId(R.id.timeToPayList)).perform(click())
        onData(
            allOf(
                `is`(Matchers.instanceOf(String::class.java)),
                `is`("Раз в месяц")
            )
        )
            .inRoot(RootMatchers.isPlatformPopup())
            .perform(click())

        onView(withId(R.id.nextButton)).perform(scrollTo(), click())
        Espresso.pressBack()
    }
}