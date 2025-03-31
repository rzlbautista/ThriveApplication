package rizelle.bautista.semestralgradecomputation;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;

    public class MainActivity extends AppCompatActivity {

        private EditText inputPrelim, inputMidterm, inputFinal;
        private TextView resultGrade, resultEquivalent, resultRemarks;
        private Button btnCompute, btnNewEntry;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            inputPrelim = findViewById(R.id.inputPrelim);
            inputMidterm = findViewById(R.id.inputMidterm);
            inputFinal = findViewById(R.id.inputFinal);
            resultGrade = findViewById(R.id.resultGrade);
            resultEquivalent = findViewById(R.id.resultEquivalent);
            resultRemarks = findViewById(R.id.resultRemarks);
            btnCompute = findViewById(R.id.btnCompute);
            btnNewEntry = findViewById(R.id.btnNewEntry);

            btnCompute.setOnClickListener(v -> showComputeDialog());
            btnNewEntry.setOnClickListener(v -> showNewEntryDialog());
        }

        private void showComputeDialog() {
            new AlertDialog.Builder(this)
                    .setTitle("WARNING MESSAGE")
                    .setMessage("All Entries Correct?")
                    .setPositiveButton("YES", (dialog, which) -> computeGrade())
                    .setNegativeButton("NO", null)
                    .show();
        }

        private void computeGrade() {
            String prelimStr = inputPrelim.getText().toString();
            String midtermStr = inputMidterm.getText().toString();
            String finalStr = inputFinal.getText().toString();

            if (prelimStr.isEmpty() || midtermStr.isEmpty() || finalStr.isEmpty()) {
                resultGrade.setText("Please enter all grades!");
                return;
            }

            double prelim = Double.parseDouble(prelimStr);
            double midterm = Double.parseDouble(midtermStr);
            double finalExam = Double.parseDouble(finalStr);

            double semGrade = (prelim * 0.25) + (midterm * 0.25) + (finalExam * 0.50);
            double pointEquivalent = getPointEquivalent(semGrade);

            resultGrade.setText(String.format("Semestral Grade: %.2f", semGrade));
            resultEquivalent.setText(String.format("Point Equivalent: %.2f", pointEquivalent));

            if (semGrade >= 75) {
                resultRemarks.setText("PASSED");
                resultRemarks.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
            } else {
                resultRemarks.setText("FAILED");
                resultRemarks.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            }
        }

        private double getPointEquivalent(double grade) {
            if (grade == 100) return 1.00;
            else if (grade >= 95) return 1.50;
            else if (grade >= 90) return 2.00;
            else if (grade >= 85) return 2.50;
            else if (grade >= 80) return 3.00;
            else if (grade >= 75) return 3.50;
            else return 5.00;
        }

        private void showNewEntryDialog() {
            new AlertDialog.Builder(this)
                    .setTitle("WARNING MESSAGE")
                    .setMessage("Are you sure?")
                    .setPositiveButton("YES", (dialog, which) -> clearFields())
                    .setNegativeButton("NO", null)
                    .show();
        }

        private void clearFields() {
            inputPrelim.setText("");
            inputMidterm.setText("");
            inputFinal.setText("");
            resultGrade.setText("Semestral Grade: ");
            resultEquivalent.setText("Point Equivalent: ");
            resultRemarks.setText("Remarks: ");
        }
    }

