package roscoe.carson.com.livecard;

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
