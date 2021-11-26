package com.bignerdranch.android.geoquiz;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //region Fields
    QuizViewModel quizViewModel;
    Button trueButton;
    Button falseButton;
    ImageButton prevButton;
    ImageButton nextButton;
    Button cheatButton;
    TextView questionTextView;
    TextView tokenTextView;

    final String TAG = "MainActivity";
    final String KEY_INDEX = "index";


    //endregion

    //region Lifecycle callbacks

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");
        outState.putInt(KEY_INDEX, quizViewModel.currentIndex);
    }

    @Override
    @SuppressLint("RestrictedApi")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        quizViewModel = new ViewModelProvider(this).get(QuizViewModel.class);

        int currentIndex = 0;
        if (savedInstanceState != null){
            currentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }

        // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
        ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() != Activity.RESULT_OK){
                        return;
                    }
                    if (result.getData() != null && result.getData().getBooleanExtra(CheatActivity.EXTRA_ANSWER_SHOWN, false)) {
                        // There are no request codes
                        quizViewModel.isCheater = true;
                        quizViewModel.set_cheatToken(quizViewModel.get_cheatToken() - 1);
                        tokenTextView.setText(String.valueOf(quizViewModel.get_cheatToken()));
                        quizViewModel.getCurrentQuestion().set_isCheated(true);
                        checkIfCheatingIsAllowed();
                    }
                });

        quizViewModel.currentIndex = currentIndex;
        Log.d(TAG, "onCreate(Bundle savedInstanceState) called");
        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        prevButton = findViewById(R.id.prev_button);
        nextButton = findViewById(R.id.next_button);
        cheatButton = findViewById(R.id.cheat_button);
        questionTextView = findViewById(R.id.question_text_view);
        tokenTextView = findViewById(R.id.token_text_view);

        questionTextView.setOnClickListener(view -> {
            quizViewModel.moveNext();
            displayQuestion();
        });

        trueButton.setOnClickListener(view -> {
            setQuestionAnswered(true);
            checkAnswer(true);
            checkIfAllAnswered();
        });

        falseButton.setOnClickListener(view -> {
            setQuestionAnswered(false);
            checkAnswer(false);
            checkIfAllAnswered();
        });

        prevButton.setOnClickListener(view -> {
            quizViewModel.movePrevious();
            displayQuestion();
        });

        nextButton.setOnClickListener(view -> {
            quizViewModel.moveNext();
            displayQuestion();
        });

        cheatButton.setOnClickListener(view -> {
            //start CheatActivity
            boolean answerIsTrue = quizViewModel.getCurrentQuestionAnswer();
            Intent intent = CheatActivity.newIntent(this, answerIsTrue);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                ActivityOptionsCompat options = ActivityOptionsCompat
                        .makeClipRevealAnimation(view, 0, 0, view.getWidth(), view.getHeight());
                someActivityResultLauncher.launch(intent, options);
            } else{
                someActivityResultLauncher.launch(intent);
            }
        });

        displayQuestion();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    //endregion

    //region Methods
    void displayQuestion(){
        Question question = quizViewModel.getCurrentQuestion();
        checkIfCheatingIsAllowed();
        tokenTextView.setText(String.valueOf(quizViewModel.get_cheatToken()));
        int questionTextResId = question.getTextResId();
        questionTextView.setText(questionTextResId);
        if (question.getAnswered()){
            trueButton.setEnabled(false);
            falseButton.setEnabled(false);
        }
        else{
            trueButton.setEnabled(true);
            falseButton.setEnabled(true);
        }
    }

    void checkIfCheatingIsAllowed(){
        cheatButton.setEnabled(quizViewModel.isCheatingAllowed());
    }

    void checkAnswer(Boolean userAnswer){
        Boolean correctAnswer = quizViewModel.getCurrentQuestion().getAnswer();

        int messageResId = R.string.incorrect_toast;
        if (quizViewModel.getCurrentQuestion().isCheated()){
            messageResId = R.string.judgment_toast;
        }
        else if (userAnswer == correctAnswer) {
            messageResId = R.string.correct_toast;
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }

    void setQuestionAnswered(boolean userAnswer){
        quizViewModel.getCurrentQuestion().setAnswered(true);
        quizViewModel.getCurrentQuestion().setUserAnswer(userAnswer);
       trueButton.setEnabled(false);
       falseButton.setEnabled(false);
    }

    void checkIfAllAnswered(){
        boolean areAllAnswered = true;
        for (Question question : quizViewModel.getQuestionBank()) {
            if (!question.getAnswered()) {
                areAllAnswered = false;
                break;
            }
        }
        if (areAllAnswered){
            Toast.makeText(this, "Your score is: " + (int)countPercentageScore() + "%",
                    Toast.LENGTH_SHORT).show();
        }
    }

    double countPercentageScore(){
        double correctCount = 0;
        for (Question question : quizViewModel.getQuestionBank()) {
            if (question.getUserAnswer() == question.getAnswer()) {
                correctCount++;
            }
        }
        double score = correctCount / quizViewModel.getQuestionBank().length;
        return score * 100;
    }
    //endregion
}