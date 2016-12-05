package roscoe.carson.com.livecard.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.ByteArrayOutputStream;

import roscoe.carson.com.livecard.R;
import roscoe.carson.com.livecard.sync.SyncManager;

public class CreateCardActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    EditText schoolField;
    EditText courseField;
    EditText questionField;
    EditText answerField;
    String base64Attachment = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_card);

        schoolField = (EditText)findViewById(R.id.schoolEditText_createCard);
        courseField = (EditText)findViewById(R.id.courseEditText_createCard);
        questionField = (EditText)findViewById(R.id.questionEditText_createCard);
        answerField = (EditText)findViewById(R.id.answerEditText_createCard);
        Button create = (Button)findViewById(R.id.createCardButton_createCard);
        ImageButton attach = (ImageButton)findViewById(R.id.imageButton);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String school = schoolField.getText().toString();
                String course = courseField.getText().toString();
                String question = questionField.getText().toString();
                String answer = answerField.getText().toString();
                String attachment = base64Attachment;
                SyncManager.instance.createCard(school, course, question, answer, attachment);
                questionField.setText("");
                answerField.setText("");
            }
        });

        attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
    }

    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            System.out.println(imageBitmap.toString());
            ((ImageButton)findViewById(R.id.imageButton)).setImageBitmap(imageBitmap);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();
            base64Attachment = Base64.encodeToString(byteArray, Base64.DEFAULT);
        }
    }
}
