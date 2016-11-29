package roscoe.carson.com.livecard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.net.URLEncoder;
import java.io.DataOutputStream;
import java.io.DataInputStream;

/**
 * Created by Carson on 11/28/2016.
 */
public class HTTPHelper {
    private static final String QUERY_URL = "https://api.mlab.com/api/1/databases/livecard/collections/cards?apiKey=LJMEN3-U1lzH2zo-2EKIqsc4_DDLlq_b";
    private static final String SIGNUP_URL = "https://api.mlab.com/api/1/databases/livecard/collections/signup?apiKey=LJMEN3-U1lzH2zo-2EKIqsc4_DDLlq_b";
    private Activity activity;

    public HTTPHelper() {}
    public HTTPHelper(Activity activity) {
        this.activity = activity;
    }

    public void getCards() {
        System.out.println("Executing ReceiveCardsTask().execute()");
        new ReceiveCardsTask().execute();
    }

    private ArrayList<Card> parseCards(InputStream in) {
        String jsonString = convertStreamToString(in);
        ArrayList<Card> result = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(jsonString);
            for(int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                String categoryID = obj.getString("DeckID");
                String question = obj.getString("Question");
                String answer = obj.getString("Answer");
                String attachment = obj.getString("Image");
                Card card = new Card(question, answer, categoryID, attachment);
                result.add(card);
            }
        } catch (Exception e) {
            System.out.println("Exception " + e.getMessage());
        }
        return result;
    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public void sendCardToServer(String school, String course, String question, String answer, String attachment) {
        new SendCardTask().execute(createJSONCard(school, course, question, answer, attachment));
    }

    String createJSONCard(String school, String course, String question, String answer, String attachment) {
        JSONObject json = new JSONObject();
        try {
            json.put("DeckID", school + " " + course);
            json.put("Question", question);
            json.put("Answer", answer);
            json.put("Image", attachment);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return json.toString();
    }

    void trySignIn(String username, String password) {
        new ReceiveLoginAttemptTask().execute(username, password);
    }

    class ReceiveCardsTask extends AsyncTask<Void, Void, ArrayList<Card>> {

        private Exception exception;

        protected ArrayList<Card> doInBackground(Void ... voids) {
            URL url;
            ArrayList<Card> cards = new ArrayList<>();
            try {
                url = new URL(QUERY_URL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    cards = parseCards(in);
                } finally {
                    urlConnection.disconnect();
                }
            } catch(Exception e) {
                System.out.println(e.toString());
                //Url was malformed
            }
            return cards;
        }

        protected void onPostExecute(ArrayList<Card> feed) {
            if (exception == null && feed.size() > 0) {
                SyncManager.instance.storeCardsFromServer(feed);
            } else {
                System.out.println(exception);
            }
        }
    }

    class SendCardTask extends AsyncTask<String, Void, Void> {

        private Exception exception;

        protected Void doInBackground(String ... string) {
            try {
                URL url;
                HttpURLConnection urlConn;
                DataOutputStream printout;
                DataInputStream input;
                url = new URL (QUERY_URL);
                urlConn = (HttpURLConnection)url.openConnection();
                urlConn.setDoInput (true);
                urlConn.setDoOutput(true);
                urlConn.setUseCaches (false);
                urlConn.setRequestProperty("Content-Type", "application/json");
                urlConn.setRequestProperty("Host", "android.schoolportal.gr");
                urlConn.connect();
                printout = new DataOutputStream(urlConn.getOutputStream ());
                printout.writeBytes(URLEncoder.encode(string[0], "UTF-8"));
                printout.flush();
                printout.close();
            } catch(Exception e) {
                System.out.println(e.toString());
            }
            return null;
        }

        protected void onPostExecute() {
        }
    }

    class ReceiveLoginAttemptTask extends AsyncTask<String, Void, Boolean> {
        private Exception exception;

        protected Boolean doInBackground(String ... strings) {
            URL url;
            try {
                url = new URL(SIGNUP_URL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    JSONArray array = new JSONArray(convertStreamToString(in));
                    for(int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        String user = obj.getString("User");
                        String pass = obj.getString("Pass");
                        if (strings[0].equals(user) && strings[1].equals(pass)) {
                            System.out.println("WOOOO");
                            return new Boolean(true);
                        }
                    }
                } finally {
                    urlConnection.disconnect();
                }
            } catch(Exception e) {
                System.out.println(e.toString());
                //Url was malformed
            }
            return new Boolean(false);
        }

        protected void onPostExecute(Boolean bool) {
            System.out.println("Login was " + bool.toString());
            Intent intent = new Intent();
            intent.putExtra("success", bool);
            activity.setResult(Activity.RESULT_OK, intent);
            activity.finish();
        }
    }

    class SendSignupTask extends AsyncTask<String, Void, Void> {
        private Exception exception;

        protected Void doInBackground(String ... string) {
            try {
                URL url;
                HttpURLConnection urlConn;
                DataOutputStream printout;
                DataInputStream input;
                url = new URL (SIGNUP_URL);
                urlConn = (HttpURLConnection)url.openConnection();
                urlConn.setDoInput(true);
                urlConn.setDoOutput(true);
                urlConn.setUseCaches(false);
                urlConn.setRequestProperty("Content-Type", "application/json");
                urlConn.setRequestProperty("Host", "android.schoolportal.gr");
                urlConn.connect();
                printout = new DataOutputStream(urlConn.getOutputStream ());
                String json = "{\"User\":\"" + string[0] + "\",";
                json += "\"Pass\":\"" + string[1] + "\"}";
                printout.writeBytes(URLEncoder.encode(json, "UTF-8"));
                printout.flush();
                printout.close();
            } catch(Exception e) {
                System.out.println(e.toString());
            }
            return null;
        }

        protected void onPostExecute() {
        }
    }
}
