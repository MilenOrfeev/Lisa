package com.example.milen.hope;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.LinkedList;
import java.util.List;

public class CreateList extends AppCompatActivity {

    private static final String TAG = CreateList.class.getName();
    private static final int RC_SIGN_IN = 4444;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount account;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_list);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                plusButttonClick();
            }
        });

        account = GoogleSignIn.getLastSignedInAccount(CreateList.this);
        if (account == null)
            signInUser();
    }

    public void plusButttonClick() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.items_layout);
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        EditText editText = new EditText(getApplicationContext());
        editText.setLayoutParams(lparams);
        editText.setHint("To do Item");
        layout.addView(editText);
    }

    public void signInUser() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                account = task.getResult(ApiException.class);

                // Signed in successfully, show authenticated UI.
            } catch (ApiException e) {
                // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.
                Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            }

        }
    }

    public void onCreateClick(View view) {
        EditText listNameBox = findViewById(R.id.list_name_box);
        String listName = listNameBox.getText().toString();

        List<ToDoItem> toDoItems = new LinkedList<>();
        LinearLayout layout = findViewById(R.id.items_layout);
        for (int index = 0; index < layout.getChildCount(); index++) {
            View textView = layout.getChildAt(index);
            if ((textView instanceof EditText) && (textView.getId() != R.id.list_name_box)) {
                String itemText = ((EditText) textView).getText().toString();
                toDoItems.add(new ToDoItem(itemText));
            }
        }

        String userID = account.getId();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(userID).child(listName).push();

        for (ToDoItem item: toDoItems) {
            mDatabase.child("users").child(userID).child(listName).child(item.getDescription())
                    .setValue(item.isCompleted());
        }
        finish();
    }
}
