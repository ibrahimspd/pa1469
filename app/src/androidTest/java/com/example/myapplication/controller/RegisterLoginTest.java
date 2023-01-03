package com.example.myapplication.controller;


import java.util.concurrent.TimeUnit;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.myapplication.R;


import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


/***************IMPORTANT: DISABLE ANIMATIONS ON DEVICE BEFORE RUNNING TEST***************/
/***************Window animation scale -> off***************/
/***************Transition animation scale -> off***************/

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RegisterLoginTest {
    @Rule
    public ActivityScenarioRule<RegisterActivity> RegisterActivityRule =
            new ActivityScenarioRule<>(RegisterActivity.class);


    @Test
    public void registerUser() throws InterruptedException {

        RandomStore.setNumber();
        int randomNumber = RandomStore.getNumber();
        String randomNumberString = Integer.toString(randomNumber);
        String email = "emil-TEST" +randomNumberString +"@live.se";
        String username = "emil_baller";
        String password = "123456";
        String confirmPassword = "123456";
        String squadNumber = "420";

        onView(withId(R.id.textEmail)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.textUsername)).perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.textPassword)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.textConfirmPassword)).perform(typeText(confirmPassword), closeSoftKeyboard());
        onView(withId(R.id.number)).perform(typeText(squadNumber), closeSoftKeyboard());

        ViewInteraction countryPickerView = onView(
                allOf(withId(R.id.countryPicker),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()));
        countryPickerView.perform(click());

        ViewInteraction constraintLayout = onView(
                allOf(withId(com.hbb20.countrypicker.R.id.countryRow),
                        childAtPosition(
                                childAtPosition(
                                        withId(com.hbb20.countrypicker.R.id.rvCountryList),
                                        7),
                                0),
                        isDisplayed()));
        constraintLayout.perform(click());

        onView(withId(R.id.registerButton)).perform(click());

        TimeUnit.SECONDS.sleep(3);

        onView(withId(R.id.emailTxt)).check(matches(isDisplayed()));
        onView(withId(R.id.passwordTxt)).check(matches(isDisplayed()));

        onView(withId(R.id.emailTxt)).perform(typeText(email));
        onView(withId(R.id.passwordTxt)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.loginBtn)).perform(click());

        TimeUnit.SECONDS.sleep(3);

        onView(allOf(withId(R.id.positionInput),
                        childAtPosition(
                                allOf(withId(R.id.lineupConstraintLayout),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                1)),
                                1),
                        isDisplayed()));
    }




    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
