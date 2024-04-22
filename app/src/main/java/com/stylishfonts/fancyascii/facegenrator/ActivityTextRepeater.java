package com.stylishfonts.fancyascii.facegenrator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.stylishfonts.fancyascii.facegenrator.databinding.ActTextRepeaterBinding;
import com.stylishfonts.fancyascii.facegenrator.utils.ConstMethod;

import java.util.ArrayList;

public class ActivityTextRepeater extends AppCompatActivity {
    ActTextRepeaterBinding binding;

    String text = "";
    String limitNo;
    int count = 0;



    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isAcceptingText()) {
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(),
                    0
            );
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActTextRepeaterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);




        binding.toolbar.mToolBarThumb.setOnClickListener(v -> onBackPressed());
        binding.toolbar.mToolBarText.setText("Text Repeater");

        limitNo = binding.etTextRepeateNumber.getText().toString();
        binding.btnRepeat.setOnClickListener(v -> {
            hideSoftKeyboard(ActivityTextRepeater.this);
            if (!TextUtils.isEmpty(binding.etTextMsg.getText().toString()) && !TextUtils.isEmpty(binding.etTextRepeateNumber.getText().toString())) {
//                    int i1 = countChar(etTextRepeateNumber.getText().toString());
                binding.etTextRepeates.getText().clear();
                String input = binding.etTextMsg.getText().toString();
                int number = Integer.parseInt(binding.etTextRepeateNumber.getText().toString());
                ArrayList<String> data = new ArrayList<>();
                if (binding.switchNewLine.isChecked()) {
                    for (int i = 0; i < number; i++) {
                        data.add(input);
                        data.add("\n");
                    }
                } else {
                    for (int i = 0; i < number; i++) {
                        data.add(input);
                    }
                }
                StringBuilder builder = new StringBuilder();

                for (int i = 0; i < data.size(); i++) {

                    int finalI = i;
//                        new Handler(getMainLooper()).post(() -> {
                        builder.append(data.get(finalI));
//                        });


                }
                binding.etTextRepeates.append(builder);
            } else {
                Toast.makeText(ActivityTextRepeater.this, getString(R.string.txt_please_enter_your_message_and_counter), Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnCopy.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(binding.etTextRepeates.getText().toString())) {
                ConstMethod.CopyToClipBoard(ActivityTextRepeater.this, binding.etTextRepeates.getText().toString());
//                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
//                clipboard.setText(etTextRepeates.getText().toString());
//                Toast.makeText(this, "Copy Succesfully..", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ActivityTextRepeater.this, getString(R.string.txt_please_enter_your_message), Toast.LENGTH_SHORT).show();
            }
        });
        binding.btnClear.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(binding.etTextRepeates.getText().toString())) {
                binding.etTextRepeates.getText().clear();
            } else {
                Toast.makeText(ActivityTextRepeater.this, getString(R.string.txt_please_enter_your_message), Toast.LENGTH_SHORT).show();
            }
        });
        binding.btnShare.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(binding.etTextRepeates.getText().toString())) {
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, binding.etTextRepeates.getText().toString());
                try {
                    startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ActivityTextRepeater.this, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ActivityTextRepeater.this, getString(R.string.txt_please_enter_your_message), Toast.LENGTH_SHORT).show();
            }
        });

    }

}