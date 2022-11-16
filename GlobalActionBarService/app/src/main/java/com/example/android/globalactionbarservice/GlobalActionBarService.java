// Copyright 2016 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.example.android.globalactionbarservice;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.FrameLayout;
import androidx.test.uiautomator.UiDevice;

import java.util.ArrayDeque;
import java.util.Deque;

import static java.sql.DriverManager.println;

public class GlobalActionBarService extends AccessibilityService {

    FrameLayout mLayout;

    @Override
    protected void onServiceConnected() {
        println("onServiceConnected");
        // Create an overlay and display the action bar
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        mLayout = new FrameLayout(this);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY;
        lp.format = PixelFormat.TRANSLUCENT;
        lp.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.TOP;
        LayoutInflater inflater = LayoutInflater.from(this);
        inflater.inflate(R.layout.action_bar, mLayout);
        wm.addView(mLayout, lp);

        configurePowerButton();

    }

    private void configurePowerButton() {
        Button powerButton = mLayout.findViewById(R.id.power);
        powerButton.setOnClickListener(view -> performGlobalAction(GLOBAL_ACTION_POWER_DIALOG));
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        println("onAccessibilityEvent");
        if(event == null) return;
        AccessibilityNodeInfo source = event.getSource();
        if(source != null) {
            println(source.toString());
//            UiDevice.getInstance().dumpWindowHierarchy(dumpXml)
//            AccessibilityNodeInfoDumper.
                    device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        }
    }

    @Override
    public void onInterrupt() {
        println("Interrupt");
    }
}
