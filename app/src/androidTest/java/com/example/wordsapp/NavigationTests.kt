package com.example.wordsapp

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.runner.AndroidJUnit4
import junit.framework.TestCase.assertEquals
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationTests {
    fun navigate_to_words_nav_component(){
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )

        // launch a fragment without writing code to navigate to it first. Useful when working with big apps
        // Fragment equivalent of ActivityScenarioRule - Isolating fragment for testing
        val letterListScenario = launchFragmentInContainer<LetterListFragment>(themeResId = R.style.Theme_Words) // pass theme, or test may crash
        letterListScenario.onFragment {  fragment ->
            navController.setGraph(R.navigation.nav_graph)
            Navigation.setViewNavController(fragment.requireView(), navController)
            onView(withId(R.id.recycler_view))
                .perform(
                    RecyclerViewActions
                        .actionOnItemAtPosition<RecyclerView.ViewHolder>(2, click())
                )
            // container is not aware of other fragments and activity, so mention the expected fragmentId after navigation
            assertEquals(navController.currentDestination?.id, R.id.wordListFragment)
        }
    }
}