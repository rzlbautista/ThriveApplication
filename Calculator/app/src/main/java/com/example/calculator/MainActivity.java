// MainActivity.java
package com.example.calculator;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText etFirstNumber, etSecondNumber;
    private TextView tvResult, textView;
    private Button btnAdd, btnDiff, btnProd, btnQuo;

    @Override

        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
        etFirstNumber = findViewById(R.id.etFirstNumber);
        etSecondNumber = findViewById(R.id.etSecondNumber);
        tvResult = findViewById(R.id.tvResult);
        textView = findViewById(R.id.textView);
        btnAdd = findViewById(R.id.btnAdd);
        btnDiff = findViewById(R.id.btnDiff);
        btnProd = findViewById(R.id.btnProd);
        btnQuo = findViewById(R.id.btnQuo);

        etFirstNumber.setHint("Enter first number");
        etSecondNumber.setHint("Enter second number");
        tvResult.setText("Result");
        textView.setText("Simple Calculator");
        tvResult.setTextSize(16);
        textView.setTextSize(24);
        btnAdd.setText("+");
        btnDiff.setText("-");
        btnProd.setText("*");
        btnQuo.setText("/");

        btnAdd.setOnClickListener(view -> calculate('+'));
        btnDiff.setOnClickListener(view -> calculate('-'));
        btnProd.setOnClickListener(view -> calculate('*'));
        btnQuo.setOnClickListener(view -> calculate('/'));
    }

    private void calculate(char operator) {
        String num1Str = etFirstNumber.getText().toString();
        String num2Str = etSecondNumber.getText().toString();

        if (num1Str.isEmpty() || num2Str.isEmpty()) {
            tvResult.setText("Please enter both numbers");
            tvResult.setTextColor(Color.BLACK);
            return;
        }

        double num1 = Double.parseDouble(num1Str);
        double num2 = Double.parseDouble(num2Str);
        double result = 0;
        String operation = "";

        switch (operator) {
            case '+':
                result = num1 + num2;
                operation = "SUM";
                break;
            case '-':
                result = num1 - num2;
                operation = "DIFFERENCE";
                break;
            case '*':
                result = num1 * num2;
                operation = "PRODUCT";
                break;
            case '/':
                if (num2 == 0) {
                    tvResult.setText("Cannot divide by zero");
                    tvResult.setTextColor(Color.BLACK);
                    return;
                }
                result = num1 / num2;
                operation = "QUOTIENT";
                break;
        }

        tvResult.setText(String.format("Total %s is: %.1f", operation, result));
        tvResult.setTextColor((result % 2 == 0) ? Color.BLUE : Color.RED);
    }
}
