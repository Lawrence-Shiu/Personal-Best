package com.example.lawsh.personalbest;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class Test1 {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    private UiDevice mDevice;

    @Before
    public void startMainActivity() {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }


    @Test
    public void test1() {
        UiObject mText = mDevice.findObject(new UiSelector().text("@gmail.com"));
        try {
            mText.click();
        } catch(UiObjectNotFoundException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.feet_input),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.feet_input),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("6"), closeSoftKeyboard());

        ViewInteraction editText = onView(
                allOf(withId(R.id.feet_input), withText("6"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        editText.check(matches(withText("6")));

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.in_input),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("0"), closeSoftKeyboard());

        ViewInteraction editText2 = onView(
                allOf(withId(R.id.in_input), withText("0"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        editText2.check(matches(withText("0")));

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.done_button), withText("Done"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.add500), withText("add 500 steps"),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout),
                                        childAtPosition(
                                                withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                                1)),
                                5),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.add500), withText("add 500 steps"),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout),
                                        childAtPosition(
                                                withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                                1)),
                                5),
                        isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.add500), withText("add 500 steps"),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout),
                                        childAtPosition(
                                                withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                                1)),
                                5),
                        isDisplayed()));
        appCompatButton4.perform(click());

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(R.id.add500), withText("add 500 steps"),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout),
                                        childAtPosition(
                                                withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                                1)),
                                5),
                        isDisplayed()));
        appCompatButton5.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.textSteps), withText("2000"),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                                1)),
                                1),
                        isDisplayed()));
        textView.check(matches(withText("2000")));

        ViewInteraction appCompatButton6 = onView(
                allOf(withId(R.id.startWalk), withText(" Start walk/run "),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.linearLayout),
                                        6),
                                0),
                        isDisplayed()));
        appCompatButton6.perform(click());

        ViewInteraction appCompatButton7 = onView(
                allOf(withId(R.id.startWalk), withText(" End walk/run "),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.linearLayout),
                                        6),
                                0),
                        isDisplayed()));
        appCompatButton7.perform(click());

        ViewInteraction button = onView(
                allOf(withId(R.id.startWalk),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.linearLayout),
                                        6),
                                0),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction appCompatButton8 = onView(
                allOf(withId(R.id.startWalk), withText(" Start walk/run "),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.linearLayout),
                                        6),
                                0),
                        isDisplayed()));
        appCompatButton8.perform(click());

        ViewInteraction appCompatButton9 = onView(
                allOf(withId(R.id.startWalk), withText(" End walk/run "),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.linearLayout),
                                        6),
                                0),
                        isDisplayed()));
        appCompatButton9.perform(click());

        ViewInteraction appCompatButton10 = onView(
                allOf(withId(R.id.startWalk), withText(" Start walk/run "),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.linearLayout),
                                        6),
                                0),
                        isDisplayed()));
        appCompatButton10.perform(click());

        ViewInteraction appCompatButton11 = onView(
                allOf(withId(R.id.add500), withText("add 500 steps"),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout),
                                        childAtPosition(
                                                withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                                1)),
                                5),
                        isDisplayed()));
        appCompatButton11.perform(click());

        ViewInteraction appCompatButton12 = onView(
                allOf(withId(R.id.add500), withText("add 500 steps"),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout),
                                        childAtPosition(
                                                withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                                1)),
                                5),
                        isDisplayed()));
        appCompatButton12.perform(click());

        ViewInteraction appCompatButton13 = onView(
                allOf(withId(R.id.add500), withText("add 500 steps"),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout),
                                        childAtPosition(
                                                withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                                1)),
                                5),
                        isDisplayed()));
        appCompatButton13.perform(click());

        ViewInteraction appCompatButton14 = onView(
                allOf(withId(R.id.add500), withText("add 500 steps"),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout),
                                        childAtPosition(
                                                withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                                1)),
                                5),
                        isDisplayed()));
        appCompatButton14.perform(click());

        ViewInteraction appCompatButton15 = onView(
                allOf(withId(R.id.add500), withText("add 500 steps"),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout),
                                        childAtPosition(
                                                withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                                1)),
                                5),
                        isDisplayed()));
        appCompatButton15.perform(click());

        ViewInteraction appCompatButton16 = onView(
                allOf(withId(R.id.add500), withText("add 500 steps"),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout),
                                        childAtPosition(
                                                withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                                1)),
                                5),
                        isDisplayed()));
        appCompatButton16.perform(click());

        ViewInteraction textView2 = onView(
                allOf(withId(android.R.id.message), withText("Congratulations! Do you want to setup a new goal?"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.scrollView),
                                        0),
                                0),
                        isDisplayed()));
        textView2.check(matches(withText("Congratulations! Do you want to setup a new goal?")));

        ViewInteraction button2 = onView(
                allOf(withId(android.R.id.button2),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.buttonPanel),
                                        0),
                                0),
                        isDisplayed()));
        button2.check(matches(isDisplayed()));

        ViewInteraction button3 = onView(
                allOf(withId(android.R.id.button1),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.buttonPanel),
                                        0),
                                1),
                        isDisplayed()));
        button3.check(matches(isDisplayed()));

        ViewInteraction appCompatButton17 = onView(
                allOf(withId(android.R.id.button2), withText("Not now"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.buttonPanel),
                                        0),
                                2)));
        appCompatButton17.perform(scrollTo(), click());

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.textSteps), withText("5000"),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                                1)),
                                1),
                        isDisplayed()));
        textView3.check(matches(withText("5000")));

        ViewInteraction button4 = onView(
                allOf(withId(R.id.startWalk),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.linearLayout),
                                        6),
                                0),
                        isDisplayed()));
        button4.check(matches(isDisplayed()));

        ViewInteraction appCompatButton18 = onView(
                allOf(withId(R.id.startWalk), withText(" End walk/run "),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.linearLayout),
                                        6),
                                0),
                        isDisplayed()));
        appCompatButton18.perform(click());


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
