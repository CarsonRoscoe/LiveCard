package roscoe.carson.com.livecard;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class JoinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        Button joinButton = (Button)findViewById(R.id.joinButton_join);

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onJoinButtonClicked();
            }
        });
    }

    public void onJoinButtonClicked() {
        String username = ((EditText)findViewById(R.id.usernameEditText_join)).getText().toString();
        String password = ((EditText)findViewById(R.id.passwordEditText_join)).getText().toString();
        Intent intent = new Intent();
        intent.putExtra("username", username);
        intent.putExtra("password", password);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
