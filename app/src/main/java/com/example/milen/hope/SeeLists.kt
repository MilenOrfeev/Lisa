package com.example.milen.hope

import android.content.Intent
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.Spinner

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import java.util.ArrayList

class SeeLists : AppCompatActivity() {
    private var mDatabaseReference: DatabaseReference? = null
    private var mItemsReference: DatabaseReference? = null
    internal var mGoogleSignInClient: GoogleSignInClient? = null
    internal var account: GoogleSignInAccount? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_see_lists)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        mDatabaseReference = FirebaseDatabase.getInstance().reference


        val listPicker = findViewById<Spinner>(R.id.spinner)
        listPicker.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                this@SeeLists.fetchList(parent.getItemAtPosition(position).toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                Log.w(TAG, "User has not selected an item")
            }
        }

        val addButton = findViewById<FloatingActionButton>(R.id.add_list_button)
        addButton.setOnClickListener { addButtonCLick() }

        val removeButton = findViewById<FloatingActionButton>(R.id.remove_list_button)
        removeButton.setOnClickListener { removeButtonCLick() }
    }

    private fun removeButtonCLick() {}

    private fun addButtonCLick() {
        val intent = Intent(this, CreateList::class.java)
        startActivity(intent)
    }

    public override fun onStart() {
        super.onStart()

        account = GoogleSignIn.getLastSignedInAccount(this@SeeLists)
        if (account == null)
            signInUser()


        val listsListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dropdownOptions = ArrayList<String>()
                val children = dataSnapshot.children
                for (child in children) {
                    dropdownOptions.add(child.key!!)
                }

                val adapter = ArrayAdapter(this@SeeLists,
                        android.R.layout.simple_spinner_dropdown_item, dropdownOptions)
                val listPicker = findViewById<Spinner>(R.id.spinner)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                listPicker.adapter = adapter

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "The following dabase error occured: ", databaseError.toException())
            }
        }

        // TODO: Change this.
        while (account == null) {
            // Simply wait
        }

        val mListsReference = mDatabaseReference!!.child("users").child(account!!.id!!)
        mListsReference.addListenerForSingleValueEvent(listsListener)
    }


    fun signInUser() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                account = task.getResult<ApiException>(ApiException::class.java)

                // Signed in successfully, show authenticated UI.
            } catch (e: ApiException) {
                // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.
                Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            }

        }
    }

    fun fetchList(listName: String) {

        val listItemsListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val children = dataSnapshot.children
                val layout = this@SeeLists.findViewById<View>(R.id.layout_items) as LinearLayout
// TODO : Create an error or something.
                if (layout.childCount > 0) {
                    layout.removeAllViewsInLayout()
                }
                for (child in children) {
                    val checkBox = CheckBox(this@SeeLists)
                    checkBox.text = child.key
                    checkBox.isChecked = child.value!!.toString() != "false"
                    this@SeeLists.linkWithListener(checkBox)
                    layout.addView(checkBox)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "The following dabase error occured: ", databaseError.toException())
            }
        }
        mItemsReference = mDatabaseReference!!.child("users").child(account!!.id!!)
                .child(listName)
        mItemsReference!!.addValueEventListener(listItemsListener)
    }

    fun linkWithListener(checkBox: CheckBox) {
        checkBox.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {

            override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
                val changedItem = buttonView.text.toString()
                mItemsReference!!.child(changedItem).setValue(isChecked)
            }
        })
    }

    companion object {
        private val TAG = "SeeListsActivity"
        private val RC_SIGN_IN = 4444
    }
}
