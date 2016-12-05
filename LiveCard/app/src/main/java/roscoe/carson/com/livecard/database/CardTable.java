package roscoe.carson.com.livecard.database;

/**
 * Created by Carson on 11/6/2016.
 */
public final class CardTable {
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    public static final String TABLE_NAME = "cardtable";
    public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    Columns.CARD_ID + TEXT_TYPE + COMMA_SEP +
                    Columns.CATEGORY_ID + TEXT_TYPE + COMMA_SEP +
                    Columns.QUESTION + TEXT_TYPE + COMMA_SEP +
                    Columns.ANSWER + TEXT_TYPE + COMMA_SEP +
                    Columns.ATTACHMENT + TEXT_TYPE + " )";

    public static final class Columns {
        public static final String CARD_ID = "cardID";
        public static final String CATEGORY_ID = "categoryID";
        public static final String QUESTION = "question";
        public static final String ANSWER = "answer";
        public static final String ATTACHMENT = "attachment";
    }
}
