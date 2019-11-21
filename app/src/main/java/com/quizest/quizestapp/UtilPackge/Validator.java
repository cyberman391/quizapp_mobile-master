package com.quizest.quizestapp.UtilPackge;

import android.app.Activity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.quizest.quizestapp.R;


public class Validator {


    /*global field validator for this app*/
    public static final String EMAIL_VERIFICATION = "^([\\w-\\.]+){1,64}@([\\w&&[^_]]+){2,255}.[a-z]{2,}$";


    /*this function validates a array of edit text */
    public static boolean validateInputField(EditText[] array,
                                             Activity context) {
        int count = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i].getText().toString().length() == 0) {
                Toast.makeText(context, array[i].getTag().toString() + " is empty!", Toast.LENGTH_SHORT).show();
                break;
            } else {
                if ((array[i].getTag().toString().equals("Email"))) {
                    if (array[i].getText().toString().trim().matches(EMAIL_VERIFICATION)) {
                        count++;
                    } else {
                        Toast.makeText(context, array[i].getTag().toString() + " is invalid", Toast.LENGTH_SHORT).show();
                        break;
                    }
                } else {
                    count++;
                }
            }
        }


        return array.length == count;
    }


}