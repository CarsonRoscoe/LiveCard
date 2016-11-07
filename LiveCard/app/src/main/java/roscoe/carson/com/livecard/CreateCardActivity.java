package roscoe.carson.com.livecard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateCardActivity extends AppCompatActivity {
    EditText schoolField;
    EditText courseField;
    EditText questionField;
    EditText answerField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_card);

        schoolField = (EditText)findViewById(R.id.schoolEditText_createCard);
        courseField = (EditText)findViewById(R.id.courseEditText_createCard);
        questionField = (EditText)findViewById(R.id.questionEditText_createCard);
        answerField = (EditText)findViewById(R.id.answerEditText_createCard);
        Button create = (Button)findViewById(R.id.createCardButton_createCard);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String school = schoolField.getText().toString();
                String course = courseField.getText().toString();
                String question = questionField.getText().toString();
                String answer = answerField.getText().toString();
                String attachment = "";
                SyncManager.instance.createCard(school, course, question, answer, attachment);
                questionField.setText("");
                answerField.setText("");
            }
        });
    }
}
