package roscoe.carson.com.livecard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    static final int SIGNIN_REQUEST = 1;
    static final int JOIN_REQUEST = 2;
    ListView deckListView;
    Button signInButton;
    Button joinButton;
    Button createCardButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SyncManager.instance.initiate(getApplicationContext());
        deckListView = (ListView)findViewById(R.id.deckListView_main);

        signInButton = (Button)findViewById(R.id.signInButton_main);
        joinButton = (Button)findViewById(R.id.joinButton_main);
        createCardButton = (Button)findViewById(R.id.createButton_main);

        deckListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onPickDeck((String) deckListView.getItemAtPosition(position));
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onSignInButtonClicked();
            }
        });

        joinButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onJoinButtonClicked();
            }
        });

        createCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {onCreateCardButtonClicked();
            }
        });

        reloadSignedIn(SignInManager.instance.isSignedIn());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

        DeckManager.instance.reloadSyncDecks();
        String[] values = DeckManager.instance.getDeckIDs();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);
        deckListView.setAdapter(adapter);
    }

    public void reloadSignedIn(boolean isSignedIn) {
        if (isSignedIn) {
            signInButton.setVisibility(View.INVISIBLE);
            joinButton.setVisibility(View.INVISIBLE);
            createCardButton.setVisibility(View.VISIBLE);
        } else {
            signInButton.setVisibility(View.VISIBLE);
            joinButton.setVisibility(View.VISIBLE);
            createCardButton.setVisibility(View.INVISIBLE);
        }
    }

    public void onSignInButtonClicked() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivityForResult(intent, SIGNIN_REQUEST);
    }

    public void onJoinButtonClicked() {
        Intent intent = new Intent(this, JoinActivity.class);
        startActivityForResult(intent, JOIN_REQUEST);
    }

    public void onCreateCardButtonClicked() {
        Intent intent = new Intent(this, CreateCardActivity.class);
        startActivity(intent);
    }

    public void onPickDeck(String deck) {
        Intent intent = new Intent(this, ViewDeckActivity.class);
        intent.putExtra("deck", deck);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SIGNIN_REQUEST) {
            if (data != null) {
                Bundle extras = data.getExtras();
                if (extras.containsKey("success") && extras.getBoolean("success")) {
                    System.out.println("SignIn Request Received");
                    SignInManager.instance.signIn();
                    reloadSignedIn(SignInManager.instance.isSignedIn());
                }
            } else {
                System.out.println("SignIn Request Failed. Data is null");
            }
        } else if (requestCode == JOIN_REQUEST) {
            if (data != null) {
                Bundle extras = data.getExtras();
                if (extras.containsKey("username") && extras.containsKey("password")) {
                    System.out.println("Join Request Received");
                }
            } else {
                System.out.println("Join Request Failed. Data is null");
            }
        }
    }
 }
