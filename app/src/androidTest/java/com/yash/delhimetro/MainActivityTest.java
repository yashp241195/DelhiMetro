package com.yash.delhimetro;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule(MainActivity.class);

    @Before
    public void setUp() throws Exception{
        Intents.init();
    }


    @Test
    public void TestInputs(){
        TestInputForm();
    }

    @Test
    public void TestNavigationView(){

        // don't run multiple TestNavigation here
        // because of intent
        // init and release ..

        int checkMap = R.id.nav_map;
        int checkAllStations = R.id.nav_allStation;
        int checkAllPlace = R.id.nav_allPlace;
        int checkWebQ = R.id.nav_webQ;

        // Test the working of checkMap
        TestNavigation(checkMap);


    }


    @After
    public void tearDown() throws Exception{
        Intents.release();
    }



    // Custom functions for implementation details of the testing


    private void TestInputForm(){
        String From = "VAISHALI";
        String To = "RAJIV CHOWK";

        onView(withId(R.id.optToStation)).perform(click());
        TestInputsUtil(From,To,"station");

        Espresso.pressBack();

        To = "PACIFIC MALL";
        onView(withId(R.id.optToPlace)).perform(click());
        TestInputsUtil(From,To,"place");

        Espresso.pressBack();

    }


    private void TestInputsUtil(String From, String To,String opt){

        // clear From Station Text by clicking on clear text

        onView(withId(R.id.actM_actvFrom)).perform(
                new ClickDrawableAction(ClickDrawableAction.Right));

        // input text in acTvFrom
        onView(withId(R.id.actM_actvFrom)).perform(typeText(From));
        Espresso.closeSoftKeyboard();

        // input text in acTvTo
        onView(withId(R.id.actM_actvTo)).perform(typeText(To));
        Espresso.closeSoftKeyboard();

        // submit the test values
        onView(withId(R.id.actM_searchRoutes)).perform(click());

        // checking the Test

        onView(withId(R.id.actE_valFromStation)).check(matches(withText(From)));

        int ToId = (opt.equals("station"))?R.id.actE_valToStation:R.id.actE_valToPlace;
        onView(withId(ToId)).check(matches(withText(To)));

    }


    private void TestNavigation(int id){

        switch (id){
            case R.id.nav_map:
                TestMap(id);
                break;
            case R.id.nav_allStation:
                TestAllStation(id);
                break;
            case R.id.nav_allPlace:
                TestAllPlaces(id);
                break;
            case R.id.nav_webQ:
                TestWebQ(id);
                break;

        }



    }


    private void TestMap(int viewId){
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());

        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open()); // Open Drawer


        // Start the screen of your activity.
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(viewId));

        intended(hasComponent(MetroMap.class.getName()));

    }

    private void TestAllStation(int viewId){

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());

        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open()); // Open Drawer


        // Start the screen of your activity.
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(viewId));

        intended(hasComponent(AllStations.class.getName()));

    }

    private void TestAllPlaces(int viewId){
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());

        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open()); // Open Drawer


        // Start the screen of your activity.
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(viewId));

        intended(hasComponent(AllPlaces.class.getName()));

    }

    private void TestWebQ(int viewId){
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());

        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open()); // Open Drawer

        // Start the screen of your activity.
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(viewId));

        intended(hasComponent(WebViewExplorePlace.class.getName()));

    }


}