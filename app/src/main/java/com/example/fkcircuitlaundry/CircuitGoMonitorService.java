package com.example.fkcircuitlaundry;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

public class CircuitGoMonitorService extends AccessibilityService {

    private static final String TAG = "CircuitGoMonitorService";
    private boolean wifiDisabled = false;
    private boolean shownPanel = false;


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d(TAG, "onAccessibilityEvent: " + event);
        if(event.getPackageName() != null && event.getPackageName().equals("co.uk.circuit.go.app")){
            android.util.Log.d("APP", "Circuit Go App event captured!");

            AccessibilityNodeInfo rootNode = getRootInActiveWindow();
            if (rootNode != null) {
                List<AccessibilityNodeInfo> foundNodes = rootNode.findAccessibilityNodeInfosByText("machine");

                if(!foundNodes.isEmpty() && !shownPanel){
                    disableWifi();
                    shownPanel = true;
                }
                rootNode.recycle();
            }
        }else if(event.getPackageName() != null && !event.getPackageName().equals("com.android.systemui")) {
            shownPanel = false;
        }
    }

    @Override
    public void onInterrupt() {

    }

    private void disableWifi() {
        Intent panelIntent = new Intent(android.provider.Settings.Panel.ACTION_INTERNET_CONNECTIVITY);
        panelIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(panelIntent);
        wifiDisabled = true;
    }
}
