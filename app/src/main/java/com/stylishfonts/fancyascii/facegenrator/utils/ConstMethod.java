package com.stylishfonts.fancyascii.facegenrator.utils;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;

import com.stylishfonts.fancyascii.facegenrator.R;

public class ConstMethod {
    public static void CopyToClipBoard(Activity activity , String copyStr){
        ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
        clipboard.setText(copyStr);
        Toast.makeText(activity, "Copy Succesfully..", Toast.LENGTH_SHORT).show();
    }


}
