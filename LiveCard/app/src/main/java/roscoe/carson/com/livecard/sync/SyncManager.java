package roscoe.carson.com.livecard.sync;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import roscoe.carson.com.livecard.database.CardTable;
import roscoe.carson.com.livecard.database.DatabaseHelper;
import roscoe.carson.com.livecard.datamodels.Card;
import roscoe.carson.com.livecard.datamodels.Deck;
import roscoe.carson.com.livecard.http.HTTPHelper;

/**
 * Created by Carson on 11/5/2016.
 */
public final class SyncManager {
    private int cardID;
    public static SyncManager instance;
    DatabaseHelper databaseHelper;
    HTTPHelper httpHelper;

    private SyncManager() {
        cardID = 0;
    }

    static {
        instance = new SyncManager();
    }

    public void initiate(Context context) {
        databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        httpHelper = new HTTPHelper();
        httpHelper.getCards();
//        System.out.println("Initiate");
//        for(Card card : httpHelper.getCards()) {
//            System.out.println("GOGOGO");
//            addCardToDatabase(db, card.GetDeckID(), card.GetQuestion(), card.GetAnswer(), card.GetAttachment());
//        }

        db.close();
    }

    private void addCardToDatabase(SQLiteDatabase db, Card card){
        addCardToDatabase(db, card.GetDeckID(), card.GetQuestion(), card.GetAnswer(), card.GetAttachment());
    }

    private void addCardToDatabase(SQLiteDatabase db, String school, String course, String question, String answer, String attachment) {
        ContentValues values = new ContentValues();
        values.put(CardTable.Columns.CARD_ID, cardID++);
        values.put(CardTable.Columns.CATEGORY_ID, school + " " + course);
        values.put(CardTable.Columns.QUESTION, question);
        values.put(CardTable.Columns.ANSWER, answer);
        values.put(CardTable.Columns.ATTACHMENT, attachment);
        db.insert(CardTable.TABLE_NAME, null, values);
    }

    private void addCardToDatabase(SQLiteDatabase db, String deckID, String question, String answer, String attachment) {
        ContentValues values = new ContentValues();
        values.put(CardTable.Columns.CARD_ID, cardID++);
        values.put(CardTable.Columns.CATEGORY_ID, deckID);
        values.put(CardTable.Columns.QUESTION, question);
        values.put(CardTable.Columns.ANSWER, answer);
        values.put(CardTable.Columns.ATTACHMENT, attachment);
        db.insert(CardTable.TABLE_NAME, null, values);
    }

    public void createCard(String school, String course, String question, String answer, String attachment) {
        if (isEmpty(school) || isEmpty(course) || isEmpty(question) || isEmpty(answer)) {
            return;
        }

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        addCardToDatabase(db, school.toUpperCase(), course.toUpperCase(), question.toUpperCase(), answer.toUpperCase(), attachment);
        httpHelper.sendCardToServer(school.toUpperCase(), course.toUpperCase(), question.toUpperCase(), answer.toUpperCase(), attachment);
        db.close();
    }

    public ArrayList<Deck> getDecks() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        HashMap<String, Deck> decks = new HashMap<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + CardTable.TABLE_NAME, null);

        if(cursor.moveToFirst()){
            do{
                String cardID = cursor.getString(0);
                String categoryID = cursor.getString(1);
                String question = cursor.getString(2);
                String answer = cursor.getString(3);
                String attachment = cursor.getString(4);
                String[] categorySplit = categoryID.split(" ");
                System.out.println(categoryID);
                String school = categorySplit[0];
                String courseID = categorySplit[1];

                if (!decks.containsKey(categoryID)) {
                    decks.put(categoryID, new Deck(school, courseID));
                }

                decks.get(categoryID).AddCard(new Card(question, answer, categoryID, attachment));

            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        ArrayList<Deck> deckArraylist = new ArrayList<>();
        for(Deck deck : decks.values()) {
            deckArraylist.add(deck);
        }
        return deckArraylist;
    }

    private boolean isEmpty(String string) {
        return string == null || string.isEmpty();
    }

    public void storeCardsFromServer(ArrayList<Card> cards) {
        ArrayList<Deck> decks = getDecks();
        HashMap<String, Deck> deckMap = new HashMap<>();
        for(Deck deck : decks) {
            deckMap.put(deck.GetCategoryID(), deck);
        }

        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        for(Card card : cards) {
            if (deckMap.containsKey(card.GetDeckID())) {
                Deck deck = deckMap.get(card.GetDeckID());
                if (deck.HasCard(card)) {
                    continue;
                }
            }
            addCardToDatabase(db, card);
        }
        db.close();
    }
}
