package com.example.tenant

import android.view.View
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tenant.ui.view.MainActivity
import com.example.tenant.ui.view.ObjectsAdapter
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UpdateObjectTest {
    @Rule
    @JvmField
    val mainActivityTestScenario = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun updateObjectTest(){
        onView(withId(R.id.objectsRecycler))
            .perform(RecyclerViewActions.actionOnItemAtPosition<ObjectsAdapter.ViewHolder>(2,
                clickItemOnUpdateButtonWithId(R.id.editObjectButton)))

        onView(withId(R.id.squareEditText))
            .perform(clearText())
            .perform(typeText("72"))
        onView(withId(R.id.categoryList)).perform(click())

        Espresso.onData(
            Matchers.allOf(
                Matchers.`is`(Matchers.instanceOf(String::class.java)),
                Matchers.`is`("Квартира")
            )
        )
            .atPosition(0)
            .inRoot(RootMatchers.isPlatformPopup())
            .perform(click())

        onView(withId(R.id.addObjectButton)).perform(click())
    }

    private fun clickItemOnUpdateButtonWithId(id: Int): ViewAction{
        return object : ViewAction{
            override fun getDescription(): String {
                return "click edit button on recyclerview item"
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