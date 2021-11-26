package com.bignerdranch.android.geoquiz;

import androidx.annotation.StringRes;

public class Question {

    //region Fields

    @StringRes
    private int _textResId;
    private boolean _answer;
    private boolean _userAnswer;
    private boolean _answered;
    private boolean _isCheated;

    //endregion

    //region GetterSetters

    public int getTextResId(){
        return _textResId;
    }

    public void setTextResId(int value){
        _textResId = value;
    }

    public boolean getAnswer(){
        return _answer;
    }

    public void setAnswered(boolean value){
        _answered = value;
    }

    public boolean getAnswered(){
        return _answered;
    }

    public void setUserAnswer(boolean value){
        _userAnswer = value;
    }

    public boolean getUserAnswer(){
        return _userAnswer;
    }

    public void setAnswer(boolean value){
        _answer = value;
    }

    public boolean isCheated() {
        return _isCheated;
    }

    public void set_isCheated(boolean value) {
        _isCheated = value;
    }

    //endregion

    //region Constructor

    public Question (@StringRes int textResId, boolean answer) {
        _textResId = textResId;
        _answer = answer;
        _answered = false;
    }

    //endregion
}