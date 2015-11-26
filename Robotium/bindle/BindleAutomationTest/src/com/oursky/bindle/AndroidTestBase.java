package com.oursky.bindle;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

@SuppressWarnings("rawtypes")
public class AndroidTestBase extends ActivityInstrumentationTestCase2{
	
	private Solo mDevice;

	public Solo device() {
		return mDevice;
	}
	
	@SuppressWarnings("unchecked")
	public AndroidTestBase(String activityName) throws ClassNotFoundException {
        super(Class.forName(activityName));
    }
    
    @Override
    public void setUp() throws Exception {
    	super.setUp();
        mDevice = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        mDevice.finishOpenedActivities();
    }
}
