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
import android.graphics.Insets;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.*;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import com.example.android.globalactionbarservice.uiautomator.AccessibilityNodeInfoDumper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

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
        if (event == null) return;
        AccessibilityNodeInfo source = event.getSource();
        if (source != null) {
            WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
            int height = getScreenHeight(wm);
            int width = getScreenWidth(wm);

            OutputStream out = new ByteArrayOutputStream();
            try {
                // TODO output file to filesystem:
                //File outputFile = new File("dumpNode-" + System.currentTimeMillis());
                //AccessibilityNodeInfoDumper.dumpNode(source, outputFile, width, height);
                AccessibilityNodeInfoDumper.dumpNode(source, out, width, height);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            String str = out.toString();
            println(str);
        }
    }

    public static int getScreenWidth(@NonNull WindowManager wm) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowMetrics windowMetrics = wm.getCurrentWindowMetrics();
            Insets insets = windowMetrics.getWindowInsets()
                    .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());
            return windowMetrics.getBounds().width() - insets.left - insets.right;
        } else {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(displayMetrics);
            return displayMetrics.widthPixels;
        }
    }

    public static int getScreenHeight(@NonNull WindowManager wm) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowMetrics windowMetrics = wm.getCurrentWindowMetrics();
            Insets insets = windowMetrics.getWindowInsets()
                    .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());
            return windowMetrics.getBounds().height() - insets.top - insets.bottom;
        } else {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(displayMetrics);
            return displayMetrics.heightPixels;
        }
    }

    @Override
    public void onInterrupt() {
        println("Interrupt");
    }
}
