package com.example.milen.hope

import android.content.Intent
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import java.util.LinkedList

class CreateList : AppCompatActivity() {
    internal var mGoogleSignInClient: GoogleSignInClient? = null
    internal var account: GoogleSignInAccount? = null
    private var mDatabase: DatabaseReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_list)


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { plusButttonClick() }

        account = GoogleSignIn.getLastSignedInAccount(this@CreateList)
        if (account == null)
            signInUser()
    }

    fun plusButttonClick() {
        val layout = findViewById<View>(R.id.items_layout) as LinearLayout
        val lparams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        val editText = EditText(applicationContext)
        editText.layoutParams = lparams
        editText.hint = "To do Item"
        layout.addView(editText)
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

    fun onCreateClick(view: View) {
        val listNameBox = findViewById<EditText>(R.id.list_name_box)
        val listName = listNameBox.text.toString()

        val toDoItems = LinkedList<ToDoItem>()
        val layout = findViewById<LinearLayout>(R.id.items_layout)
        for (index in 0 until layout.childCount) {
            val textView = layout.getChildAt(index)
            if (textView is EditText && textView.getId() != R.id.list_name_box) {
                val itemText = textView.text.toString()
                toDoItems.add(ToDoItem(itemText))
            }
        }

        val userID = account!!.id

        mDatabase = FirebaseDatabase.getInstance().reference
        mDatabase!!.child("users").child(userID!!).child(listName).push()

        for (item in toDoItems) {
            mDatabase!!.child("users").child(userID).child(listName).child(item.description)
                    .setValue(item.isCompleted)
        }
        finish()
    }

    companion object {

        private val TAG = CreateList::class.java.name
        private val RC_SIGN_IN = 4444
    }
}
