package com.vaultshare.play;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class KeyboardUtil {
    public static boolean isKeyboardVisible(View v) {
        try {
            Rect r = new Rect();
            // R will be populated with the coordinates of your view that area still visible.
            v.getWindowVisibleDisplayFrame(r);

            int heightDiff = v.getRootView().getHeight() - (r.bottom - r.top);
            return heightDiff > 100; // if more than 100 pixels, its probably a keyboard
        } catch (Exception e) {
            return false;
        }
    }

    public static void showKeyboard(Activity activity, View v) {
        if (isKeyboardVisible(v)) {
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager) App.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() == null) {
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
        } else {
            View view = activity.getCurrentFocus();
            inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
        v.requestFocus();
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view == null) {
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);

    }
}
