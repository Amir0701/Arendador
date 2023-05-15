package com.example.tenant

import android.widget.DatePicker
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.contrib.PickerActions.setDate
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tenant.ui.view.MainActivity
import com.example.tenant.ui.view.ObjectsAdapter
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class AddContractTest {
    @Rule
    @JvmField
    val mainActivityTestScenario = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun addContractTest(){
        onView(withId(R.id.objectsRecycler))
            .perform(RecyclerViewActions.actionOnItemAtPosition<ObjectsAdapter.ViewHolder>(2, click()))

        onView(withId(R.id.addContractButton)).perform(click())
        onView(withId(R.id.nameEditText)).perform(typeText("Amir"))
        onView(withId(R.id.lastNameEditText)).perform(typeText("Usmonov"))
        onView(withId(R.id.passportNumberEditText)).perform(typeText("400500700"))
        onView(withId(R.id.phoneNumberEditText)).perform(typeText("89870512246"))
        onView(withId(R.id.addUserButton)).perform(scrollTo()).perform(click())

        onView(withId(R.id.dateOfConclusionOfAnAgreement))
            .perform(click())

        onView(isAssignableFrom(DatePicker::class.java)).perform(setDate(2023, 5, 15))

        onView(withId(android.R.id.button1)).perform(click())

        onView(withId(R.id.dateOfEndConclusion))
            .perform(click())

        onView(isAssignableFrom(DatePicker::class.java)).perform(setDate(2024, 5, 15))

        onView(withId(android.R.id.button1)).perform(click())

        onView(withId(R.id.sum)).perform(typeText("25000"))
        onView(withId(R.id.timeToPayList)).perform(click())

        Espresso.onData(
            Matchers.allOf(
                Matchers.`is`(Matchers.instanceOf(String::class.java)),
                Matchers.`is`("Раз в месяц")
            )
        )
            .inRoot(RootMatchers.isPlatformPopup())
            .perform(click())

        onView(withId(R.id.nextButton)).perform(scrollTo(), click())
        pressBack()
    }
}