package roscoe.carson.com.livecard;

import java.util.ArrayList;

/**
 * Created by Carson on 11/5/2016.
 */
public class Deck {
    public static Deck Empty = new Deck();

    private ArrayList<Card> cards;
    private String school;
    private String courseID;

    private Deck() {
        cards = new ArrayList<>();
        school = "";
        courseID = "";
    }

    public Deck(String school, String courseID) {
        cards = new ArrayList<>();
        this.school = school;
        this.courseID = courseID;
    }

    public ArrayList<Card> GetCards() {
        return cards;
    }

    public void AddCard(Card card) {
        cards.add(card);
    }

    public void RemoveCard(Card card) {
        cards.remove(card);
    }

    public String GetSchool() {
        return school;
    }

    public String GetCourse() {
        return courseID;
    }

    public String GetCategoryID() { return school + " " + courseID; }
}
