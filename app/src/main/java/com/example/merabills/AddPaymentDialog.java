package com.example.merabills;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.merabills.data.Payment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddPaymentDialog extends Dialog {
    private Context context;
    private List<Payment> existingPayments;
    private OnPaymentAddedListener listener;

    private Spinner paymentTypeSpinner;
    private EditText amountEditText, providerEditText, transactionRefEditText;
    private ConstraintLayout extraDetailsLayout;

    public interface OnPaymentAddedListener {
        void onPaymentAdded(Payment payment);
    }

    public AddPaymentDialog(Context context, List<Payment> existingPayments, OnPaymentAddedListener listener) {
        super(context);
        this.context = context;
        this.existingPayments = existingPayments;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_payment_dialog);
        if (getWindow() != null) {
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        paymentTypeSpinner = findViewById(R.id.payment_type_spinner);
        amountEditText = findViewById(R.id.amount_et);
        providerEditText = findViewById(R.id.provider_et);
        transactionRefEditText = findViewById(R.id.transaction_ref_et);
        extraDetailsLayout = findViewById(R.id.extra_details_layout);
        Button saveButton = findViewById(R.id.save_button);
        AppCompatTextView cancelButton = findViewById(R.id.cancel_button);

        updatePaymentTypeSpinner();

        paymentTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedType = (String) parent.getItemAtPosition(position);
                extraDetailsLayout.setVisibility(selectedType.equals("Cash") ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        saveButton.setOnClickListener(v -> {
            //Check Amount Values
            if (amountEditText.getText().toString().trim().isEmpty()) {
                showToast(context.getString(R.string.please_enter_amount));
                return;
            }

            //Provider and Transaction reference required only for Bank Transfer and Credit Card
            if (paymentTypeSpinner.getSelectedItem().toString().equals("Bank Transfer") ||
                    paymentTypeSpinner.getSelectedItem().toString().equals("Credit Card")) {
                //Provider
                if (transactionRefEditText.getText().toString().trim().isEmpty()) {
                    showToast(context.getString(R.string.please_enter_provider));
                    return;
                }

                //Transaction Reference
                if (amountEditText.getText().toString().trim().isEmpty()) {
                    showToast(context.getString(R.string.please_enter_transaction_reference));
                    return;
                }
            }

            String type = paymentTypeSpinner.getSelectedItem().toString();
            double amount = Double.parseDouble(amountEditText.getText().toString());
            String provider = providerEditText.getText().toString();
            String transactionRef = transactionRefEditText.getText().toString();

            Payment payment = new Payment(type, amount, provider, transactionRef);
            listener.onPaymentAdded(payment);
            dismiss();
        });

        cancelButton.setOnClickListener(v -> {
            dismiss();
        });
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    private void updatePaymentTypeSpinner() {
        List<String> availablePayments = new ArrayList<>(Arrays.asList("Cash", "Bank Transfer", "Credit Card"));

        for (Payment p : existingPayments) {
            availablePayments.remove(p.getType());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, availablePayments);
        paymentTypeSpinner.setAdapter(adapter);
    }
}
