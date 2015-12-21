package com.oursky.bindle;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.robotium.solo.Solo;
import com.robotium.solo.Solo.Config;

@SuppressWarnings("rawtypes")
public class AndroidTestBase extends ActivityInstrumentationTestCase2{
	
	public final String TAG = "bindle-test";
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
    	Solo.Config config = new Config();
    	config.timeout_large = 20000;
    	config.timeout_small = 30000;
        mDevice = new Solo(getInstrumentation(), config, getActivity());
        mDevice.unlockScreen();
        Log.d(TAG, "************************************");
    }

    @Override
    public void tearDown() throws Exception {
        mDevice.finishOpenedActivities();
        try {
			mDevice.finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
