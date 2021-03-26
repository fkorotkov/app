package com.github.steroidteam.todolist;


import android.content.Intent;
import android.os.IBinder;
import android.view.View;
import android.view.WindowManager;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Root;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.github.steroidteam.todolist.util.TodoAdapter;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withChild;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class ItemViewActivityTest {

    @Rule
    public ActivityScenarioRule<ItemViewActivity> activityRule =
            new ActivityScenarioRule<>(ItemViewActivity.class);

    @Test
    public void createTaskWorks() {
        final String TASK_DESCRIPTION = "Buy bananas";

        // Type a task description in the "new task" text field.
        onView(withId(R.id.new_task_text))
                .perform(typeText(TASK_DESCRIPTION), closeSoftKeyboard());

        // Hit the button to create a new task.
        onView(withId(R.id.new_task_btn))
                .perform(click());

        // The task description text field should now be empty.
        onView(withId(R.id.new_task_text))
                .check(matches(withText("")));

        // TODO: Check that the ListView actually contains a new item with the tested description.
    }

    @Test
    public void cannotCreateTaskWithoutText() {
        // Clear the text input.
        onView(withId(R.id.new_task_text)).
                perform(clearText());

        // Hit the button to create a new task.
        onView(withId(R.id.new_task_btn))
                .perform(click());

        onView(withId(R.id.activity_itemview_itemlist))
                .check(matches(isDisplayed()));
    }

    @Test
    public void removeTaskLongClickWorks() {
        Intent itemViewActivity = new Intent(ApplicationProvider.getApplicationContext(), ItemViewActivity.class);

        try (ActivityScenario<ItemViewActivity> scenario = ActivityScenario.launch(itemViewActivity)) {

            final String TASK_DESCRIPTION = "Buy bananas";
            final String TASK_DESCRIPTION_2 = "Buy cheese";

            // Type a task description in the "new task" text field.
            onView(withId(R.id.new_task_text))
                    .perform(typeText(TASK_DESCRIPTION), closeSoftKeyboard());

            // Hit the button to create a new task.
            onView(withId(R.id.new_task_btn))
                    .perform(click());

            onView(withId(R.id.new_task_text))
                    .perform(typeText(TASK_DESCRIPTION_2), closeSoftKeyboard());

            onView(withId(R.id.new_task_btn))
                    .perform(click());

            // Try to remove the first task
            onView(withId(R.id.activity_itemview_itemlist)).perform(actionOnItemAtPosition(0, longClick()));

            onView(withText("You are about to delete a task!")).check(matches(isDisplayed()));

            onView(withText("Yes")).perform(click());

            //after deleting the first item we check that we have the second one at position 0.
            onView(withId(R.id.layout_task_body)).check(matches(withText(TASK_DESCRIPTION_2)));
        }
    }

    /**
    @Test
    public void removeTaskButtonWorks() {
        Intent itemViewActivity = new Intent(ApplicationProvider.getApplicationContext(), ItemViewActivity.class);

        try (ActivityScenario<ItemViewActivity> scenario = ActivityScenario.launch(itemViewActivity)) {

            final String TASK_DESCRIPTION = "Buy bananas";
            final String TASK_DESCRIPTION_2 = "Buy cheese";

            // Type a task description in the "new task" text field.
            onView(withId(R.id.new_task_text))
                    .perform(typeText(TASK_DESCRIPTION), closeSoftKeyboard());

            // Hit the button to create a new task.
            onView(withId(R.id.new_task_btn))
                    .perform(click());

            onView(withId(R.id.new_task_text))
                    .perform(typeText(TASK_DESCRIPTION_2), closeSoftKeyboard());

            onView(withId(R.id.new_task_btn))
                    .perform(click());

            openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getTargetContext());
            onView(withText("Delete")).perform(click());

            openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getTargetContext());
            onView(withText("Delete")).perform(click());

            openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getTargetContext());
            onView(withText("Delete")).perform(click());
            
            onView(withId(R.id.activity_itemview_itemlist)).perform(
                    RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id.layout_task_delete_button)));

            onView(withText("You are about to delete a task!")).check(matches(isDisplayed()));

            onView(withText("Yes")).perform(click());

            //after deleting the first item we check that we have the second one at position 0.
            onView(withId(R.id.layout_task_body)).check(matches(withText(TASK_DESCRIPTION_2)));

            //check that cancel the deletion works
            onView(withId(R.id.activity_itemview_itemlist)).perform(
                    RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id.layout_task_delete_button)));
            onView(withText("No")).perform(click());
            onView(withId(R.id.layout_task_body)).check(matches(withText(TASK_DESCRIPTION_2)));
        }
    }
    **/
    
    // Simple ViewAction to click on the button within a item of the recyclerView
    public static class MyViewAction {

        public static ViewAction clickChildViewWithId(final int id) {
            return new ViewAction() {
                @Override
                public Matcher<View> getConstraints() {
                    return null;
                }

                @Override
                public String getDescription() {
                    return "Click on a child view with specified id.";
                }

                @Override
                public void perform(UiController uiController, View view) {
                    View v = view.findViewById(id);
                    v.performClick();
                }
            };
        }
    }

    @Test
    public void confirmDeletionWorks() {
        Intent itemViewActivity = new Intent(ApplicationProvider.getApplicationContext(), ItemViewActivity.class);

        try (ActivityScenario<ItemViewActivity> scenario = ActivityScenario.launch(itemViewActivity)) {

            final String TASK_DESCRIPTION = "Buy bananas";

            // Type a task description in the "new task" text field.
            onView(withId(R.id.new_task_text))
                    .perform(typeText(TASK_DESCRIPTION), closeSoftKeyboard());

            // Hit the button to create a new task.
            onView(withId(R.id.new_task_btn))
                    .perform(click());

            onView(withId(R.id.activity_itemview_itemlist)).perform(actionOnItemAtPosition(0, longClick()));

            onView(withText("You are about to delete a task!")).check(matches(isDisplayed()));

            onView(withText("No")).perform(click());

            onView(withId(R.id.layout_task_body)).check(matches(withText(TASK_DESCRIPTION)));
        }
    }

    @Test
    public void notificationDeleteWorks() {
        Intent itemViewActivity = new Intent(ApplicationProvider.getApplicationContext(), ItemViewActivity.class);

        try (ActivityScenario<ItemViewActivity> scenario = ActivityScenario.launch(itemViewActivity)) {
            final String TASK_DESCRIPTION = "Buy bananas";
            // Type a task description in the "new task" text field.
            onView(withId(R.id.new_task_text))
                    .perform(typeText(TASK_DESCRIPTION), closeSoftKeyboard());

            // Hit the button to create a new task.
            onView(withId(R.id.new_task_btn))
                    .perform(click());

            onView(withId(R.id.activity_itemview_itemlist)).perform(actionOnItemAtPosition(0, longClick()));

            onView(withText("You are about to delete a task!")).check(matches(isDisplayed()));

            onView(withText("Yes")).perform(click());

            onView(withText("Successfully removed the task !")).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
        }
    }

}
