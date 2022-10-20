package com.isaacmanu.notesapp

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NoteListFragmentTest {

    private lateinit var scenario: FragmentScenario<NotesListFragment>

    @Before
    fun setup() {
        scenario = launchFragmentInContainer(themeResId = R.style.NotesAppNoActionBar)
        scenario.moveToState(Lifecycle.State.STARTED)
    }


    // Test fails if there are no notes
    @Test
    fun test_recycler_view_scroll() {
        onView(withId(R.id.recycler_view)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(2)
        )
    }

    @Test
    fun test_navigation_on_fab_click() {

        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )
        //Declare which nav graph to use in fragment
        scenario.onFragment { fragment ->
            navController.setGraph(R.navigation.nav_graph)

            Navigation.setViewNavController(fragment.requireView(), navController)
        }



        onView(withId(R.id.fab))
            .perform(click())

        assertEquals(navController.currentDestination?.id, R.id.newNoteFragment)

    }

    @Test
    fun test_navigation_on_recycler_view_click() {

        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )
        //Declare which nav graph to use in fragment
        scenario.onFragment { fragment ->
            navController.setGraph(R.navigation.nav_graph)

            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        onView(withId(R.id.recycler_view))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(2, click()))

        assertEquals(navController.currentDestination?.id, R.id.noteFragment)

    }


}