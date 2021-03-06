package roscoe.carson.com.livecard.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import roscoe.carson.com.livecard.R;
import roscoe.carson.com.livecard.datamodels.Card;
import roscoe.carson.com.livecard.datamodels.Deck;
import roscoe.carson.com.livecard.datamodels.DeckManager;

public class ViewDeckActivity extends AppCompatActivity {
    TextView questionTextView;
    TextView answerTextView;
    ImageView attachmentImage;
    int currentCardIndex;
    Deck deck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_deck);

        Bundle extras = getIntent().getExtras();
        String deckName = extras.getString("deck");
        deck = DeckManager.instance.getDeck(deckName);

        Button leftButton = (Button)findViewById(R.id.leftButton_viewDeck);
        Button rightButton = (Button)findViewById(R.id.rightButton_viewDeck);
        Button happyButton = (Button)findViewById(R.id.happyButton_viewDeck);
        Button neutralButton = (Button)findViewById(R.id.neutralButton_viewDeck);
        Button sadButton = (Button)findViewById(R.id.sadButton_viewDeck);
        FrameLayout cardFrame = (FrameLayout)findViewById(R.id.cardFrameLayout_viewDeck);
        questionTextView = (TextView)findViewById(R.id.questionTextView_viewDeck);
        answerTextView = (TextView)findViewById(R.id.answerTextView_viewDeck);
        attachmentImage = ((ImageView)findViewById(R.id.attachmentImage_viewDeck));
        attachmentImage.setVisibility(View.INVISIBLE);

        ArrayList<Card> cards = deck.GetCards();
        if (cards.size() > 0) {
            changeCard(0);
            leftButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onLeftButtonClicked();
                }
            });
            rightButton.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    onRightButtonClicked();
                }
            });
            happyButton.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    onHappyButtonClicked();
                }
            });
            neutralButton.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    onNeutralButtonClicked();
                }
            });
            sadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSadButtonClicked();
                }
            });
            cardFrame.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onFlipCard();
                    return false;
                }
            });

        } else {
            questionTextView.setText("No cards in deck");
            answerTextView.setText("");
            leftButton.setEnabled(false);
            rightButton.setEnabled(false);
            happyButton.setEnabled(false);
            neutralButton.setEnabled(false);
            sadButton.setEnabled(false);
        }
    }

    private void changeCard(int index) {
        currentCardIndex = index;
        Card card = deck.GetCards().get(index);
        questionTextView.setText(card.GetQuestion());
        answerTextView.setText("");
        attachmentImage.setVisibility(View.INVISIBLE);
    }

    private void onRightButtonClicked() {
        int size = deck.GetCards().size();
        if (currentCardIndex + 1 < size) {
            changeCard(++currentCardIndex);
        }
    }

    private void onLeftButtonClicked() {
        if (currentCardIndex - 1 >= 0) {
            changeCard(--currentCardIndex);
        }
    }

    private void onHappyButtonClicked() {
        System.out.println("onHappyButtonClicked()");
    }

    private void onNeutralButtonClicked() {
        System.out.println("onNeutralButtonClicked()");
    }

    private void onSadButtonClicked() {
        System.out.println("onSadButtonClicked()");
    }

    private void onFlipCard() {
        Card currentCard = deck.GetCards().get(currentCardIndex);
        answerTextView.setText(currentCard.GetAnswer());
        String attachment = currentCard.GetAttachment();
        if (attachment != "") {
            attachmentImage.setVisibility(View.VISIBLE);
            byte[] decodedString = Base64.decode(attachment, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            attachmentImage.setImageBitmap(decodedByte);
        }
    }
}
