package com.example.tenant

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tenant.ui.view.MainActivity
import com.example.tenant.ui.view.ObjectsAdapter
import org.hamcrest.Matcher
import org.hamcrest.core.AllOf.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DeleteObjectTest {
    @Rule
    @JvmField
    val activityTestRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun deleteObject(){
        onView((withId(R.id.objectsRecycler)))
            .perform(RecyclerViewActions.actionOnItemAtPosition<ObjectsAdapter.ViewHolder>(2,
                clickItemDeleteButtonWithId(R.id.deleteObjectButton)))

        onView(withText("ДА"))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
            .perform(click())
    }

    private fun clickItemDeleteButtonWithId(id: Int): ViewAction{
        return object : ViewAction{
            override fun getDescription(): String {
                return "click delete button on recyclerview item"
            }

            override fun getConstraints(): Matcher<View>? {
                return null
            }

            override fun perform(uiController: UiController?, view: View?) {
                val v = view?.findViewById<View>(id)
                v?.performClick()
            }
        }
    }
}