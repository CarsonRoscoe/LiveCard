package roscoe.carson.com.livecard.http;

import android.app.Activity;

/**
 * Created by Carson on 11/6/2016.
 */
public class SignInManager {
    public static SignInManager instance;
    private boolean isSignedIn;

    private SignInManager() {
        isSignedIn = false;
    }

    static {
        instance = new SignInManager();
    }

    public void trySignIn(Activity activity, String username, String password) {
        new HTTPHelper(activity).trySignIn(username, password);
    }

    public void signIn() {
        isSignedIn = true;
    }

    public void signOut() {
        isSignedIn = false;
    }

    public boolean isSignedIn() {
        return isSignedIn;
    }
}
