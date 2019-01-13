package android.support.test;

import android.content.Context;

/*
 * Workaround for the following issue:- https://github.com/mockito/mockito/issues/1472
 * TODO Remove when Mockito fix their library
 */
@SuppressWarnings("unused")
public class InstrumentationRegistry {
    public static Context getTargetContext() {
        return androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().getTargetContext();
    }
}