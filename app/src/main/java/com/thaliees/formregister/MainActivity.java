package com.thaliees.formregister;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnFocusChangeListener {
    private TextInputLayout email, password, repeatPassword;
    private EditText tEmail, tPassword, tRepeatPassword;
    private TextView note;
    private SlideButton button;
    private float movementInitialX, halfToMove;
    private Integer widthButton;
    private Boolean bEmail = false, bPassword = false, bRepeatPassword = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TextInputLayout
        email = findViewById(R.id.layoutEmail);
        password = findViewById(R.id.layoutPassword);
        repeatPassword = findViewById(R.id.layoutRepeatPassword);
        // EditText
        tEmail = findViewById(R.id.email);
        tPassword = findViewById(R.id.password);
        tRepeatPassword = findViewById(R.id.repeatPassword);
        // TextView
        note = findViewById(R.id.notePassword);
        // Button
        button = findViewById(R.id.slide);

        tEmail.setOnFocusChangeListener(this);
        tPassword.setOnFocusChangeListener(this);
        tRepeatPassword.setOnFocusChangeListener(this);
        button.setOnTouchListener(onTouchListener);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        note.setVisibility(View.GONE);
        if (v.getId() == R.id.email && hasFocus) {
            bEmail = true;
            if (bPassword) validatePassword();
            if (bRepeatPassword) validatePasswords();
        }
        else if (v.getId() == R.id.password && hasFocus) {
            note.setVisibility(View.VISIBLE);
            bPassword = true;
            if (bEmail) validateEmail();
            if (bRepeatPassword) validatePasswords();
        }
        else {
            bRepeatPassword = true;
            if (bEmail) validateEmail();
            if (bPassword) validatePassword();
        }
    }

    private void validateEmail(){
        String e = tEmail.getText().toString().trim();
        if (!Patterns.EMAIL_ADDRESS.matcher(e).matches())
            email.setError(getResources().getText(R.string.msg_error_email));
        else email.setErrorEnabled(false);
    }

    private void validatePassword(){
        String p = tPassword.getText().toString().trim();
        if (p.length() < 8) // Implement your validation here
            password.setError(getResources().getText(R.string.msg_error_password));
        else password.setErrorEnabled(false);
    }

    private void validatePasswords(){
        String password1 = tPassword.getText().toString().trim();
        String password2 = tRepeatPassword.getText().toString().trim();
        if (!password2.equals(password1))
            repeatPassword.setError(getResources().getText(R.string.msg_error_repeat));
        else repeatPassword.setErrorEnabled(false);
    }

    private boolean validateData(){
        boolean correct = true;
        email.setErrorEnabled(false);
        password.setErrorEnabled(false);
        repeatPassword.setErrorEnabled(false);
        String e = tEmail.getText().toString().trim();
        String p = tPassword.getText().toString().trim();
        String r = tRepeatPassword.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(e).matches()) {
            email.setError(getResources().getText(R.string.msg_error_email));
            correct = false;
        }

        if (p.length() == 0 || p.length() < 8) { // Implement your validation here
            password.setError(getResources().getText(R.string.msg_error_password));
            correct = false;
        }
        else if (!r.equals(p)) {
            repeatPassword.setError(getResources().getText(R.string.msg_error_repeat));
            correct = false;
        }
        return correct;
    }

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN: return true;

                case MotionEvent.ACTION_MOVE:
                    // Validate that ImageView is not expanded and avoid moving it
                    if (!button.expanded) {
                        // Initialize parameters to evaluate
                        if (movementInitialX == 0) {
                            movementInitialX = button.toSlider.getX();              // Movement initial (when user move for first once the ImageView)
                            widthButton = button.getWidth();                        // Get width of our SlideButton
                            button.initWidthToSlider = button.toSlider.getWidth();  // Get width of our ImageView (Now, we saved in your property)
                            halfToMove = (float) button.toSlider.getWidth() / 2;    // Calculate half the width of our ImageView
                        }
                        // Move the button
                        if (event.getX() > (movementInitialX + halfToMove) && (event.getX() + halfToMove) < widthButton)
                            button.toSlider.setX(event.getX() - halfToMove);
                        // Move to the end and avoid overflowing the limit
                        if ((event.getX() + halfToMove) > widthButton && (button.toSlider.getX() + halfToMove) < widthButton)
                            button.toSlider.setX(widthButton - button.initWidthToSlider);
                        // Move to the start and avoid overflowing the limit
                        if (event.getX() < halfToMove)
                            button.toSlider.setX(0);
                    }

                    return true;

                case MotionEvent.ACTION_UP:
                    // What animation to do?
                    // If the position of the movement exceeds 75% of the width of the SlideButton, expand
                    if ((button.toSlider.getX() + button.initWidthToSlider) > (widthButton * 0.75)) {
                        button.expandButton();
                        transactionRegister();
                    }
                    else button.moveButtonBack();
            }
            return false;
        }
    };

    private void transactionRegister(){
        if (validateData()) {
            // Send data
        }
        else button.collapseButton();
    }
}
