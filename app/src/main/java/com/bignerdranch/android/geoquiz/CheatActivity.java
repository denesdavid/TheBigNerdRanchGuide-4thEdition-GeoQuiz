package com.bignerdranch.android.geoquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    final static String EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true";
    final static String EXTRA_ANSWER_SHOWN = "com.bignerdranch.android.geoquiz.answer_shown";
    final String KEY_CHEATED = "cheated";
    boolean isCheated = false;

    private boolean answerIsTrue = false;

    private TextView answerTextView;
    private TextView versionTextView;
    private Button showAnswerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);


        answerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
        answerTextView = findViewById(R.id.answer_text_view);
        versionTextView = findViewById(R.id.android_version_text_view);
        showAnswerButton = findViewById(R.id.show_answer_button);

        versionTextView.setText("API Level " + Build.VERSION.SDK_INT);

        if (savedInstanceState != null){
            if (savedInstanceState.getBoolean(KEY_CHEATED, false)){
                showCheat();
            }
        }

        showAnswerButton.setOnClickListener(view -> {
            showCheat();
        });
    }

    private void showCheat(){
        isCheated = true;
        int answerText;
        if (answerIsTrue){
            answerText = R.string.true_button;
        }
        else{
            answerText = R.string.false_button;
        }
        answerTextView.setText(answerText);
        setAnswerShownResult(true);
    }

    static Intent newIntent(Context context, Boolean answerIsTrue){
        return new Intent(context, CheatActivity.class)
                .putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("cheated", isCheated );
    }

    private void setAnswerShownResult(Boolean isAnswerShown){
        Intent intent = getIntent().putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(Activity.RESULT_OK, intent);
    }
}