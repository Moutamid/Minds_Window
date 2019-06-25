package com.example.mycalender;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

public class Utils {
    public static boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }
    public  static void toast(Context context,String Message){
        Toast.makeText(context, Message, Toast.LENGTH_SHORT).show();
    }
}
