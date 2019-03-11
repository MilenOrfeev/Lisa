package com.example.milen.hope;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SeeLists extends AppCompatActivity {
    private static final String TAG = "SeeListsActivity";
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mItemsReference;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount account;
    private static final int RC_SIGN_IN = 4444;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_lists);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();


        Spinner listPicker = findViewById(R.id.spinner);
        listPicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SeeLists.this.fetchList(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.w(TAG, "User has not selected an item");
            }
        });

        FloatingActionButton addButton = findViewById(R.id.add_list_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addButtonCLick();
            }
        });

        FloatingActionButton removeButton = findViewById(R.id.remove_list_button);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeButtonCLick();
            }
        });
    }

    private void removeButtonCLick() {
    }

    private void addButtonCLick() {
        Intent intent = new Intent(this, CreateList.class);
        startActivity(intent);
    }

    @Override
    public void onStart(){
        super.onStart();

        account = GoogleSignIn.getLastSignedInAccount(SeeLists.this);
        if (account == null)
            signInUser();


        ValueEventListener listsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> dropdownOptions= new ArrayList<>();
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children){
                    dropdownOptions.add(child.getKey());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(SeeLists.this,
                        android.R.layout.simple_spinner_dropdown_item, dropdownOptions);
                Spinner listPicker = findViewById(R.id.spinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                listPicker.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "The following dabase error occured: ", databaseError.toException());
            }
        };

        // TODO: Change this.
        while (account == null) {
            // Simply wait
        }

        DatabaseReference mListsReference = mDatabaseReference.child("users").child(account.getId());
        mListsReference.addListenerForSingleValueEvent(listsListener);
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

    public void fetchList(final String listName) {

        ValueEventListener listItemsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                LinearLayout layout = (LinearLayout) SeeLists.this.findViewById(R.id.layout_items);
                if (layout == null)
                    return; // TODO : Create an error or something.
                if (layout.getChildCount() > 0) {
                    layout.removeAllViewsInLayout();
                }
                for (DataSnapshot child : children) {
                    CheckBox checkBox = new CheckBox(SeeLists.this);
                    checkBox.setText(child.getKey());
                    if (child.getValue().toString().equals("false"))
                        checkBox.setChecked(false);
                    else
                        checkBox.setChecked(true);
                    SeeLists.this.linkWithListener(checkBox);
                    layout.addView(checkBox);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "The following dabase error occured: "
                        , databaseError.toException());
            }
        };
        mItemsReference = mDatabaseReference.child("users").child(account.getId())
                .child(listName);
        mItemsReference.addValueEventListener(listItemsListener);
    }

    public void linkWithListener(CheckBox checkBox) {
        checkBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String changedItem = String.valueOf(buttonView.getText());
                mItemsReference.child(changedItem).setValue(isChecked);
            }
        });
    }
}
