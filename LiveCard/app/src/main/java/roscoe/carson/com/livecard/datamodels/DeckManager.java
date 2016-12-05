package roscoe.carson.com.livecard.datamodels;

import java.util.ArrayList;

import roscoe.carson.com.livecard.sync.SyncManager;


/**
 * Created by Carson on 11/5/2016.
 */
public final class DeckManager {
    public static DeckManager instance;
    private ArrayList<Deck> decks;

    private DeckManager() {
        reloadSyncDecks();
    }

    static {
        instance = new DeckManager();
    }

    public ArrayList<Deck> getDecks() {
        return decks;
    }

    public Deck getDeck(String categoryID) {
        for (Deck deck: decks) {
            if (deck.GetCategoryID().equals(categoryID))
                return deck;
        }
        System.out.println("No deck of name " + categoryID + " found, returning empty deck.");
        return Deck.Empty;
    }

    public String[] getDeckIDs() {
        String[] result = new String[decks.size()];
        for(int i = 0; i < decks.size(); i++) {
            result[i] = decks.get(i).GetCategoryID();
        }
        return result;
    }

    public void reloadSyncDecks() {
        decks = SyncManager.instance.getDecks();
    }
}
