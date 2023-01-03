package com.example.myapplication.controller.RegisterLongInput;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.myapplication.R;
import com.example.myapplication.controller.RegisterActivity;
import com.example.myapplication.controller.RandomStore;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
public class FieldTest4 {
    /*limit for email length is about 250 characters from firestore it seems, so wont test higher*/
    @Rule
    public ActivityScenarioRule<RegisterActivity> RegisterActivityRule =
            new ActivityScenarioRule<>(RegisterActivity.class);

    @Test
    public void usernameNotLong() {
        RandomStore obj = new RandomStore();
        obj.setNumber();
        int randomNum = obj.getNumber();
        String randomNumberString = Integer.toString(randomNum);

        String lagomUsername = "user" + randomNumberString;
        String lagomPassword = "123456";



        String lagomEmail = "emil-TEST" +randomNumberString + "@live.se";


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

        onView(withId(R.id.textEmail)).perform(replaceText(lagomEmail), closeSoftKeyboard());
        onView(withId(R.id.textUsername)).perform(replaceText(lagomUsername), closeSoftKeyboard());
        onView(withId(R.id.textPassword)).perform(replaceText(lagomPassword), closeSoftKeyboard());
        onView(withId(R.id.textConfirmPassword)).perform(replaceText(lagomPassword), closeSoftKeyboard());
        onView(withId(R.id.number)).perform(replaceText("69"), closeSoftKeyboard());
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.registerButton)).perform(click());
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.emailTxt)).check(matches(isDisplayed()));
        onView(withId(R.id.passwordTxt)).check(matches(isDisplayed()));
        /*should pass to login activity since all fields should be valid*/
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
