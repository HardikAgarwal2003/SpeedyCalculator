package com.hardik.speedycalculator;

import android.os.Bundle;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.hardik.speedycalculator.databinding.ActivityMainBinding;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class MainActivity extends AppCompatActivity {

    private static ActivityMainBinding binding;
    boolean lastNumeric = false;
    boolean stateError = false;
    boolean lastDot = false;
    private static Expression expression;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void onAllclearClick(View view) {

        binding.dataTv.setText("");
        binding.resultTv.setText("");
        stateError = false;
        lastDot = false;
        lastNumeric = false;
        binding.resultTv.setVisibility(View.GONE);

    }

    public void onEqualClick(View view) {

//    TODO: 04-11-2023 Check this before running
/*
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform haptic feedback on the button click
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            }
        });
*/

        onEqual();
        // TODO: 02-11-2023 This may also cause error at runtime... video time- 39:17
        // .substring(1) is used inplace of drop(1) function of Kotlin to remove '=' from string...
        binding.dataTv.setText(binding.resultTv.getText().toString().substring(1));

    }

    public void onDigitClick(View view) {
        if (stateError) {
//            Button myButton = (Button) findViewById(R.id.btn_0);
//            binding.dataTv.setText(myButton.getText());

            // TODO: 02-11-2023 May Cause Error video timing - 36:07 minutes"
//            Button btn = (Button) view;
            binding.dataTv.setText(((Button) view).getText().toString());
            stateError = false;
        } else {
//            binding.dataTv.append(view.getContext().getText(0).toString());
            binding.dataTv.append(((Button) view).getText());
        }
        lastNumeric = true;
        onEqual();
    }

    public void onOperatorClick(View view) {

        if (!stateError && lastNumeric) {
            binding.dataTv.append(((Button) view).getText());
            lastNumeric = false;
            lastDot = false;
            onEqual();
        }
    }

    public void onBackClick(View view) {
        // TODO: 02-11-2023 May give error at runtime... Video Time - 41:49 minutes
        // .substring(0, (binding.resultTv.getText().toString()).length() - 1)) is used in place of dropLast(1) function of Kotlin...
        String dataText = binding.dataTv.getText().toString().trim();
        if (dataText.isEmpty()) {
            binding.dataTv.setText("");
        }else{
            binding.dataTv.setText(dataText.substring(0, dataText.length() - 1));

            //        binding.dataTv.setText(binding.resultTv.getText().toString().substring(0, (binding.resultTv.getText().toString()).length() - 1));

            try {
                String str = binding.dataTv.getText().toString();

                char lastChar = str.charAt(str.length() - 1);
                if (Character.isDigit(lastChar)) {
                    onEqual();
                }

            } catch (Exception e) {
                binding.resultTv.setText("");
                binding.resultTv.setVisibility(View.GONE);
                Log.e("lastChar error", e.getMessage().toString());
            }
        }
    }

    public void onClearClick(View view) {

        binding.dataTv.setText("");
        lastNumeric = false;

    }

    public void onEqual() {
        if (lastNumeric && !stateError) {
            String txt = binding.dataTv.getText().toString();
            expression = new ExpressionBuilder(txt).build();
            try {
                double result = expression.evaluate();
                binding.resultTv.setVisibility(View.VISIBLE);
                binding.resultTv.setText("=" + result);
            } catch (ArithmeticException e) {
                Log.e("evaluate error", e.getMessage().toString());
                binding.resultTv.setText("Error");
                stateError = true;
                lastNumeric = false;
            }
        }
    }
}
