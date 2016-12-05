package roscoe.carson.com.livecard.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import roscoe.carson.com.livecard.R;
import roscoe.carson.com.livecard.http.SignInManager;

public class SignInActivity extends AppCompatActivity {
    private boolean rememberMe = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        ((Switch)findViewById(R.id.rememberMe_signIn)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                rememberMe = isChecked;
            }
        });

        ((Button)findViewById(R.id.signInButton_signIn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSignInButtonClicked();
            }
        });
    }

    public void onSignInButtonClicked() {
        String username = ((EditText)findViewById(R.id.usernameEditText_signIn)).getText().toString();
        String password = ((EditText)findViewById(R.id.passwordEditText_signIn)).getText().toString();
        SignInManager.instance.trySignIn(this, username, password);
    }
}
