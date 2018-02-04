package com.insomvic.frenchquiz;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    int number_of_questions = 7;
    int question_number = 0;
    int correct_counter = 0;
    String correct_answer;
    boolean answered = false;
    // The correct answer is the last string in the array in question
    // The first string in array is question type
    String answers[][] = {{"radio", "maison", "arbre", "livre", "eau", "maison"},
                          {"radio", "chat", "lumière", "ordinateur", "voiture", "voiture"},
                          {"check", "chien", "chat", "cheval", "chatte", "chat, chatte"},
                          {"check", "vache", "chienne", "chien", "chèvre", "chienne, chien"},
                          {"radio", "thé", "pomme", "café", "orange", "café"},
                          {"type", "orange"},
                          {"type", "pomme"},
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Start with first question
        initQuestion();
    }

    private void initQuestion() {
        // Get and set header image
        ImageView image = findViewById(R.id.image_header);
        String image_name = "img_question_" + (question_number +  1); // Add 1 because resource images start at 1
        int image_id = getResources().getIdentifier(image_name, "drawable", getPackageName());
        image.setImageResource(image_id);
        // Determine question type
        switch (answers[question_number][0]) {
            case "radio":
                initRadioQuestion();
                break;
            case "check":
                initCheckboxQuestion();
                break;
            case "type":
                initTypeQuestion();
                break;
        }
    }

    private void initRadioQuestion() {
        // Make radio layout visible
        LinearLayout display = findViewById(R.id.radio_question);
        display.setVisibility(View.VISIBLE);
        // Reset appearance (colors, checked buttons, etc)
        Button buttonSubmit = findViewById(R.id.radio_submit);
        buttonSubmit.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        // Get radio id's and set text
        correct_answer = answers[question_number][5];
        for (int i = 1; i <= 4; i++){
            String id_name = "answer_radio_" + i;
            String id_text = answers[question_number][i];
            RadioButton radioButton = findViewById(getResources().getIdentifier(id_name, "id", getPackageName()));
            radioButton.setText(id_text);
        }
    }

    private void initCheckboxQuestion() {
        // Make checkbox layout visible
        LinearLayout display = findViewById(R.id.checkbox_question);
        display.setVisibility(View.VISIBLE);
        // Reset appearance (colors, checked buttons, etc)
        Button buttonSubmit = findViewById(R.id.checkbox_submit);
        buttonSubmit.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        // Get checkbox id's and set text
        correct_answer = answers[question_number][5];
        for (int i = 1; i <= 4; i++){
            String id_name = "answer_checkbox_" + i;
            String id_text = answers[question_number][i];
            CheckBox checkBox = findViewById(getResources().getIdentifier(id_name, "id", getPackageName()));
            checkBox.setText(id_text);
        }
    }

    private void initTypeQuestion() {
        // Make type layout visible
        LinearLayout display = findViewById(R.id.type_question);
        display.setVisibility(View.VISIBLE);
        // Reset appearance (colors, checked buttons, etc)
        Button buttonSubmit = findViewById(R.id.type_submit);
        buttonSubmit.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        // Get checkbox id's and set text
        correct_answer = answers[question_number][1];
    }

    public void submitRadioAnswer(View view) {
        if (!answered) {
            answered = true;
            String selected_answer = "";
            // Get text from selected answer
            RadioGroup radioGroup = findViewById(R.id.radio_question);
            int selectedId = radioGroup.getCheckedRadioButtonId();
            RadioButton radioButton = findViewById(selectedId);
            // Make sure a selected id is found. If not, return blank
            if (radioButton != null) {
                selected_answer = (String) radioButton.getText();
            }
            // Check if correct
            Button buttonSubmit = findViewById(R.id.radio_submit);
            checkAnswer(buttonSubmit, selected_answer, false);
        }
    }

    public void submitCheckboxAnswer(View view) {
        if (!answered) {
            answered = true;
            // Check each checkbox
            boolean first_answer = true;
            StringBuilder selected_answer = new StringBuilder("");
            for (int i = 1; i <= 4; i++) {
                String id_name = "answer_checkbox_" + i;
                CheckBox checkBox = findViewById(getResources().getIdentifier(id_name, "id", getPackageName()));
                if (checkBox.isChecked()) {
                    // Concatenate answer strings to separate via commas
                    if (first_answer) {
                        first_answer = false;
                        selected_answer.append(checkBox.getText());
                    } else {
                        selected_answer.append(", ");
                        selected_answer.append(checkBox.getText());
                    }
                }
            }
            // Check if correct
            Button buttonSubmit = findViewById(R.id.checkbox_submit);
            checkAnswer(buttonSubmit, String.valueOf(selected_answer), false);
        }
    }

    public void submitTypeAnswer(View view) {
        if (!answered) {
            answered = true;
            // Get text from selected answer
            EditText editText = findViewById(R.id.answer_type);
            String selected_answer = editText.getText().toString();
            // Check if correct
            Button buttonSubmit = findViewById(R.id.type_submit);
            checkAnswer(buttonSubmit, selected_answer, true);
        }
    }

    /**
     * Verifies if the selected answer(s) matches the correct answer
     *
     * @param buttonSubmit    set correct submit button id
     * @param selected_answer the selected answer provided by the user
     */
    private void checkAnswer(Button buttonSubmit, String selected_answer, boolean editable_text) {
        if (selected_answer.equalsIgnoreCase(correct_answer)) {
            // Correct
            answered = true;
            Toast.makeText(getApplicationContext(), R.string.correct_answer, Toast.LENGTH_SHORT).show();
            correct_counter++;
            // Change button color
            buttonSubmit.setBackgroundColor(getResources().getColor(R.color.colorCorrectAnswer));
            // Queue next question
            prepareNextQuestion();
        } else if (selected_answer.equals("")) {
            // User forget to select an answer input
            answered = false;
            Toast.makeText(getApplicationContext(), getString(R.string.forgot_answer), Toast.LENGTH_SHORT).show();
        // Used for EditText questions
        } else if (editable_text){
            // This is a special case to search the answer to see if it 'contains' the word of the correct answer
            if (selected_answer.toLowerCase().contains(correct_answer.toLowerCase())) {
                // Kind of Correct
                answered = true;
                Toast.makeText(getApplicationContext(), R.string.kind_of_correct_answer, Toast.LENGTH_SHORT).show();
                correct_counter++;
                // Change button color
                buttonSubmit.setBackgroundColor(getResources().getColor(R.color.colorCorrectAnswer));
                // Queue next question
                prepareNextQuestion();
            } else {
                // Incorrect
                answered = true;
                Toast.makeText(getApplicationContext(), getString(R.string.incorrect_answer) + " " + correct_answer, Toast.LENGTH_SHORT).show();
                // Change button color
                buttonSubmit.setBackgroundColor(getResources().getColor(R.color.colorIncorrectAnswer));
                // Queue next question
                prepareNextQuestion();
            }
        } else {
            // Incorrect
            answered = true;
            Toast.makeText(getApplicationContext(), getString(R.string.incorrect_answer) + " " + correct_answer, Toast.LENGTH_SHORT).show();
            // Change button color
            buttonSubmit.setBackgroundColor(getResources().getColor(R.color.colorIncorrectAnswer));
            // Queue next question
            prepareNextQuestion();
        }
    }

    private void prepareNextQuestion() {
        // Wait 2.5 seconds for the next question to show on UI
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Hide foreground layout
                hideAnswerLayouts();
                // Set next question
                question_number++;
                if (question_number < number_of_questions) {
                    initQuestion();
                    answered = false;
                } else {
                    // No more questions exist, so show results
                    runOnUiThread(new Runnable() {
                        public void run() {
                            showResults();
                        }
                    });
                }
            }
        }, 2500);
    }

    private void showResults() {
        // Show results in centered box
        TextView textView = findViewById(R.id.results_message);
        String message;
        // Different message based on how many questions you got correct
        if (correct_counter == number_of_questions) {
            message = getString(R.string.perfect);
        } else if (correct_counter > (number_of_questions / 2)) {
            message = getString(R.string.great_job);
        } else {
            message = getString(R.string.okay_job);
        }
        message += " " + correct_counter + " " + getString(R.string.out_of) + " " + number_of_questions + " " + getString(R.string.correct);
        // Toast the results
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        message += "\n" + getString(R.string.try_again);
        textView.setText(message);
        // Make dialog box visible
        LinearLayout displayBox = findViewById(R.id.results);
        displayBox.setVisibility(View.VISIBLE);
    }

    public void hideAnswerLayouts() {
        // Hide all answer type layers
        findViewById(R.id.radio_question).setVisibility(View.INVISIBLE);
        findViewById(R.id.checkbox_question).setVisibility(View.INVISIBLE);
        findViewById(R.id.type_question).setVisibility(View.INVISIBLE);
        // Toggle checkboxes to off
        for (int i = 1; i <= 4; i++) {
            String id_name = "answer_checkbox_" + i;
            CheckBox checkBox = findViewById(getResources().getIdentifier(id_name, "id", getPackageName()));
            if(checkBox.isChecked()) {
                checkBox.toggle();
            }
        }
        // Toggle radio buttons to off
        RadioGroup radioGroup = findViewById(R.id.radio_question);
        radioGroup.clearCheck();
        // Edit text field will now be blank
        EditText editText = findViewById(R.id.answer_type);
        editText.setText("");
    }

    public void restartGame(View view) {
        // Make dialog box invisible
        LinearLayout displayBox = findViewById(R.id.results);
        displayBox.setVisibility(View.INVISIBLE);
        // Set initial values
        answered = false;
        question_number = 0;
        correct_counter = 0;
        initQuestion();
    }

}