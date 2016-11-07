package roscoe.carson.com.livecard;

/**
 * Created by Carson on 11/5/2016.
 */
public class Card {
    private String attachedImage;
    private String question;
    private String answer;
    private String deckID;

    public Card(String question, String answer, String deckID) {
        this.question = question;
        this.answer = answer;
        this.deckID = deckID;
        this.attachedImage = null;
    }

    public Card(String question, String answer, String deckID, String imageToAttach) {
        this.question = question;
        this.answer = answer;
        this.deckID = deckID;
        this.attachedImage = imageToAttach;
    }

    public void SetAttachment(String imageToAttach) {
        attachedImage = imageToAttach;
    }

    public String GetAttachment() {
        return attachedImage;
    }

    public String GetQuestion() {
        return question;
    }

    public String GetAnswer() {
        return answer;
    }

    public String GetDeckID() { return deckID; }
}
