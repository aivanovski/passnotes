package com.ivanovsky.passnotes.presentation.core.widget

import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.core.view.isVisible
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.common.truth.Truth.assertThat
import com.ivanovsky.passnotes.TestApp
import com.ivanovsky.passnotes.util.filterViewTreeByClass
import com.ivanovsky.passnotes.util.filterViewTree
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28], application = TestApp::class)
class ExpandableFloatingActionButtonTest {

    private lateinit var fab: ExpandableFloatingActionButton

    @Before
    fun setUp() {
        val activityController = Robolectric.buildActivity(AppCompatActivity::class.java)
        val activity = activityController.get()

        // Create the view using the activity context
        fab = ExpandableFloatingActionButton(activity, null)
    }

    @Test
    fun `init should inflate layout with fab and hidden container`() {
        // arrange
        // act
        // assert
        assertCollapsed(fab)
    }

    @Test
    fun `inflate should create inner buttons`() {
        // arrange
        val items = listOf("1", "2", "3")

        // act
        fab.inflate(items)

        // assert
        val fabButton = fab.filterViewTree { view ->
            view is FloatingActionButton
        }.firstOrNull()

        val container = fab.children.filter { view ->
            view !is FloatingActionButton
        }.firstOrNull()

        val innerFabButtons = fab.filterViewTree { view -> view is ExtendedFloatingActionButton }

        assertThat(fabButton).isNotNull()
        assertThat(container).isNotNull()
        assertThat(innerFabButtons.size).isEqualTo(items.size)
    }

    @Test
    fun `inflate should create button with text`() {
        // arrange
        val items = listOf(TEXT)

        // act
        fab.inflate(items)

        // assert
        val innerFabButton = fab.filterViewTreeByClass(ExtendedFloatingActionButton::class)
            .firstOrNull()

        assertThat(innerFabButton).isNotNull()
        assertThat(innerFabButton?.text.toString()).isEqualTo(TEXT)
    }

    @Test
    fun `onMainFabClicked should expand button`() {
        // arrange
        val items = listOf(TEXT)

        // act
        fab.inflate(items)
        fab.onMainFabClicked()

        // assert
        assertExpanded(fab)
    }

    @Test
    fun `onMainFabClicked should collapse button`() {
        // arrange
        val items = listOf(TEXT)

        // act
        fab.inflate(items)
        fab.expand()
        fab.onMainFabClicked()

        // assert
        assertCollapsed(fab)
    }

    private fun assertCollapsed(fab: ExpandableFloatingActionButton) {
        val fabButton = fab.filterViewTreeByClass(FloatingActionButton::class)
            .firstOrNull()

        val container = fab.filterViewTreeByClass(ViewGroup::class)
            .firstOrNull()

        assertThat(fabButton).isNotNull()
        assertThat(container).isNotNull()

        assertThat(fabButton?.isVisible).isTrue()
        assertThat(container?.isVisible).isFalse()
    }

    private fun assertExpanded(fab: ExpandableFloatingActionButton) {
        val fabButton = fab.filterViewTreeByClass(FloatingActionButton::class)
            .firstOrNull()

        val container = fab.filterViewTreeByClass(ViewGroup::class)
            .firstOrNull()

        assertThat(fabButton).isNotNull()
        assertThat(container).isNotNull()

        assertThat(fabButton?.isVisible).isTrue()
        assertThat(container?.isVisible).isTrue()
    }

    companion object {
        private const val TEXT = "Text"
    }
}