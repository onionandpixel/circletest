package com.example.circletest;

import static android.content.Context.KEYGUARD_SERVICE;
import static android.content.Context.POWER_SERVICE;
import static android.os.PowerManager.ACQUIRE_CAUSES_WAKEUP;
import static android.os.PowerManager.FULL_WAKE_LOCK;
import static android.os.PowerManager.ON_AFTER_RELEASE;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.PowerManager;
import android.support.test.runner.AndroidJUnitRunner;

/**
 * CustomRunner which prevents screenlock for CI testing
 */
public final class CiEmulatorRunner extends AndroidJUnitRunner {

        private PowerManager.WakeLock wakeLock;

        @Override public void onStart() {
            super.onStart();
            // Inform the app we are an instrumentation test before the object graph is initialized.

            Context app = getTargetContext().getApplicationContext();

            String name = "CircleTest";
            // Unlock the device so that the tests can input keystrokes.
            KeyguardManager keyguard = (KeyguardManager) app.getSystemService(KEYGUARD_SERVICE);
            keyguard.newKeyguardLock(name).disableKeyguard();
            // Wake up the screen.
            PowerManager power = (PowerManager) app.getSystemService(POWER_SERVICE);
            wakeLock = power.newWakeLock(FULL_WAKE_LOCK | ACQUIRE_CAUSES_WAKEUP | ON_AFTER_RELEASE, name);
            wakeLock.acquire();

        }

        @Override public void onDestroy() {
            wakeLock.release();
            super.onDestroy();
        }
}
