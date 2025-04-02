package com.example.calculatrice;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initNumberButtons();
    }

    ExpressionParser parser = new ExpressionParser();

    private void initNumberButtons() {
        int[] ids = {
                R.id.button_number_0,
                R.id.button_number_1,
                R.id.button_number_2,
                R.id.button_number_3,
                R.id.button_number_4,
                R.id.button_number_5,
                R.id.button_number_6,
                R.id.button_number_7,
                R.id.button_number_8,
                R.id.button_number_9,
                R.id.button_number_float,
                R.id.button_operator_addition,
                R.id.button_operator_substraction,
                R.id.button_operator_division,
                R.id.button_operator_multiplication,
        };

        for (int id : ids) {
            Button btn = findViewById(id);
            btn.setOnClickListener(this::onButtonClick);
        }
    }

    public void cleanText(View view) {
        TextView resultView = findViewById(R.id.textResult);
        resultView.setText("");
    }
    public void onButtonClick (View view) {
        Button button = (Button) view;
        String value = button.getText().toString();

        TextView resultView = findViewById(R.id.textResult);
        String currentText = resultView.getText().toString();
        resultView.setText(currentText + value);
    }

    public void getResult (View view) {
        TextView resultView = findViewById(R.id.textResult);
        String currentText = resultView.getText().toString();

        //Afin de gérer les priorités, j'ai décidé d'utiliser l'algo de Shunting Yard, qui semblait être le plus adapté à la tâche (ne gère pas les parenthèses)
        //Le code utilisé pour cela ne provient donc pas entièrement de moi (partiellement de sources internet, partiellement IA)
        List<String> tokens = parser.tokenize(currentText);
        List<String> postfix = parser.infixToPostfix(tokens);
        try {
            double result = parser.evaluatePostfix(postfix);

            if (result == (long) result) {
                resultView.setText(String.valueOf((long) result));
            } else {
                resultView.setText(String.valueOf(result));
            }

        } catch (ArithmeticException e) {
            resultView.setText("Erreur : division par zéro");

        } catch (IllegalArgumentException e) {
            resultView.setText("Erreur : expression invalide");

        } catch (Exception e) {
            resultView.setText("Erreur inconnue");
        }
    }

}