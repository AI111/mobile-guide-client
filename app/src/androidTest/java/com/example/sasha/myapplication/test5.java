package com.example.sasha.myapplication;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;
import com.robotium.solo.Timeout;


@SuppressWarnings("rawtypes")
public class test5 extends ActivityInstrumentationTestCase2 {
    private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "com.example.sasha.myapplication.views.MainActivity";
    private static Class<?> launcherActivityClass;

    static {
        try {
            launcherActivityClass = Class.forName(LAUNCHER_ACTIVITY_FULL_CLASSNAME);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private Solo solo;

    @SuppressWarnings("unchecked")
    public test5() throws ClassNotFoundException {
        super(launcherActivityClass);
    }

    public void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());

    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }

    public void testDetailFragment() {
        //Wait for activity: 'com.example.sasha.myapplication.views.MainActivity'
        solo.waitForActivity("MainActivity", 2000);
        //Click on ImageView
        solo.clickOnView(solo.getView(android.widget.ImageButton.class, 0));
        //Click on Install
        solo.clickOnView(solo.getView(android.widget.TextView.class, 5));
        //Click on ImageView

        //Set default small timeout to 10196 milliseconds
        Timeout.setSmallTimeout(5196);
        //Click on ImageView
        solo.clickInRecyclerView(0);
        //Wait for activity: 'com.example.sasha.myapplication.views.DetailGuideInfoActivity'
        assertTrue("DetailGuideInfoActivity is not found!", solo.waitForActivity("MainMapActivity"));
        //Press menu back key
        solo.goBack();
    }

}
