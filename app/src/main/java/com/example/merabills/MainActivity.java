package com.example.merabills;

import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.merabills.data.Payment;
import com.example.merabills.utils.FileUtils;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Payment> payments = new ArrayList<>();
    private ChipGroup chipGroup;
    private AppCompatTextView totalAmountTextView;
    private AppCompatTextView paymentsTv;
    private AppCompatButton addPaymentButton;
    private double totalAmount = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        chipGroup = findViewById(R.id.chipGroup);
        totalAmountTextView = findViewById(R.id.total_amount_tv);
        paymentsTv = findViewById(R.id.payments_tv);
        addPaymentButton = findViewById(R.id.add_payment_button);
        AppCompatImageView backAction = findViewById(R.id.back_action);
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            backAction.setImageTintList(ColorStateList.valueOf(Color.WHITE));
        } else {
            backAction.setImageTintList(ColorStateList.valueOf(Color.BLACK));
        }

        // Load Old payments
        payments = FileUtils.loadPayments(this);
        updateUI();

        addPaymentButton.setOnClickListener(v -> openAddPaymentDialog());
        backAction.setOnClickListener(v ->finish());
    }

    private void openAddPaymentDialog() {
        AddPaymentDialog dialog = new AddPaymentDialog(this, payments, newPayment -> {
            payments.add(newPayment);
            updateUI();
        });
        dialog.show();
    }

    private void updateUI() {
        chipGroup.removeAllViews();
        totalAmount = 0.0;

        for (Payment payment : payments) {
            totalAmount += payment.getAmount();
            Chip chip = new Chip(this);
            String typeAmount = payment.getType() + ": ₹" + payment.getAmount();
            chip.setText(typeAmount);
            chip.setCloseIconVisible(true);
            chip.setOnCloseIconClickListener(v -> {
                payments.remove(payment);
                updateUI();
            });
            chipGroup.addView(chip);
        }
        String totalAmountValue = "Total Amount= ₹" + totalAmount;
        totalAmountTextView.setText(totalAmountValue);

        // Save payments after update
        FileUtils.savePayments(this, payments);

        int chipCount = chipGroup.getChildCount();
        if (chipCount == 3) {
            addPaymentButton.setVisibility(View.GONE);
            paymentsTv.setText(getString(R.string.payments));
        } else if (chipCount == 0) {
            paymentsTv.setText(R.string.no_payments);
            addPaymentButton.setVisibility(View.VISIBLE);
        } else {
            paymentsTv.setText(getString(R.string.payments));
            addPaymentButton.setVisibility(View.VISIBLE);
        }
    }
}