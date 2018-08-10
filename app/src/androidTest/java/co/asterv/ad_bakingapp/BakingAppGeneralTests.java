package co.asterv.ad_bakingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class BakingAppGeneralTests {
    @Rule
    public ActivityTestRule<MainActivity> mMainActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void clickRecipeItem_OpensDetailFragment() {
        // Find the view & Perform action on view
        onView((withId(R.id.recipeNameRecyclerView)))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Check if the view does as expected
        onView(withId(R.id.recipe_detail_container)).check(matches(isDisplayed ()));
    }
}
