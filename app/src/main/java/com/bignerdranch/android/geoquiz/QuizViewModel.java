package com.bignerdranch.android.geoquiz;

import android.util.Log;

import androidx.lifecycle.ViewModel;

public class QuizViewModel extends ViewModel {

    private final String TAG = "QuizViewModel";
    int currentIndex = 0;
    private int _cheatToken = 3;
    boolean isCheater = false;


    private Question[] questionBank = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, false),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, true),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true)
    };

    public QuizViewModel(){
        Log.d(TAG, "ViewModel instance created");
    }

    //perfect place to clean-up, for example un-observing a datasource
    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "ViewModel instance about to be destroyed");
    }

    public boolean isCheatingAllowed(){
        if (_cheatToken > 0){
            return true;
        }
        return false;
    }

    int get_cheatToken(){return _cheatToken;}

    void set_cheatToken(int value){
        if (value < 0){
            _cheatToken = 0;
        } else {
            _cheatToken = value;
        }
    }

    Question[] getQuestionBank(){
        return questionBank;
    }

    Question getCurrentQuestion(){
        return questionBank[currentIndex];
    }

    Boolean getCurrentQuestionAnswer(){
        return questionBank[currentIndex].getAnswer();
    }

    int currentQuestionText(){
        return questionBank[currentIndex].getTextResId();
    }

    void moveNext(){
        currentIndex = (currentIndex + 1) % questionBank.length;
    }

    void movePrevious(){
        if (currentIndex > 0){
            currentIndex--;
        }
        else{
            currentIndex = 5;
        }
    }

}
