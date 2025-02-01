package com.example.merabills.utils;

import android.content.Context;

import com.example.merabills.data.Payment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    private static final String FILE_NAME = "LastPayment.txt";

    public static void savePayments(Context context, List<Payment> payments) {
        Gson gson = new Gson();
        String json = gson.toJson(payments);
        try (FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
             OutputStreamWriter osw = new OutputStreamWriter(fos)) {
            osw.write(json);
        } catch (IOException e) {
            //Exception
        }
    }

    public static List<Payment> loadPayments(Context context) {
        List<Payment> payments = new ArrayList<>();
        try (FileInputStream fis = context.openFileInput(FILE_NAME);
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader br = new BufferedReader(isr)) {
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                json.append(line);
            }
            Type listType = new TypeToken<List<Payment>>() {
            }.getType();
            payments = new Gson().fromJson(json.toString(), listType);
        } catch (IOException e) {
            //Exception
        }
        return payments;
    }
}
