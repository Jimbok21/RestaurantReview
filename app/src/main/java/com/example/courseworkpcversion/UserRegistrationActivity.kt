package com.example.courseworkpcversion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.courseworkpcversion.firestore.FirestoreClass
import com.example.courseworkpcversion.models.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

//This class allows the user to create an account and logs them in
class UserRegistrationActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_registration)

    }

    fun openLogin(view: View) {
        //takes the user back to the main login page
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    fun createAccount(view: View) {
        //gets the data the user inputs and removes spaces
        val email = findViewById<TextView>(R.id.inputEmailText)
        val password = findViewById<TextView>(R.id.inputPasswordText)
        val username = findViewById<TextView>(R.id.inputUsernameText)
        val emailTxt: String = email.text.toString().trim { it <= ' ' }
        val passwordTxt: String = password.text.toString().trim { it <= ' ' }
        val usernameTxt: String = username.text.toString().trim { it <= ' ' }
        //creates the snackbars and assignes them colours
        val snackEmptyPassword =
            Snackbar.make(view, getString(R.string.missingPassword), Snackbar.LENGTH_LONG)
        snackEmptyPassword.view.setBackgroundColor(ContextCompat.getColor(this, R.color.ColourSnackbarError))
        val snackEmptyEmail =
            Snackbar.make(view, getString(R.string.missingEmail), Snackbar.LENGTH_LONG)
        snackEmptyEmail.view.setBackgroundColor(ContextCompat.getColor(this, R.color.ColourSnackbarError))
        val snackEmptyUsername =
            Snackbar.make(view, getString(R.string.missingUsername), Snackbar.LENGTH_LONG)
        snackEmptyUsername.view.setBackgroundColor(ContextCompat.getColor(this, R.color.ColourSnackbarError))
        val snackSuccessCreated =
            Snackbar.make(view, getString(R.string.registerSuccess), Snackbar.LENGTH_LONG)
        snackSuccessCreated.view.setBackgroundColor(ContextCompat.getColor(this, R.color.ColourSnackbarSuccess))

        when {
            //checks if the user has left any details blank and alerts them
            TextUtils.isEmpty(passwordTxt) -> {
                snackEmptyPassword.show()
            }

            TextUtils.isEmpty(emailTxt) -> {
                snackEmptyEmail.show()
            }

            TextUtils.isEmpty(usernameTxt) -> {
                snackEmptyUsername.show()
            }
            else -> {
                //This will create the user and sign them in
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailTxt, passwordTxt)
                    .addOnCompleteListener() { task ->
                        if (task.isSuccessful) {

                            val firebaseUser: FirebaseUser = task.result!!.user!!

                            val user = User(
                                firebaseUser.uid, usernameTxt, emailTxt)

                            val intent =
                                Intent(this@UserRegistrationActivity, HomePageActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            intent.putExtra("user_id", firebaseUser.uid)
                            intent.putExtra("email_id", emailTxt)
                            FirestoreClass().registerUser(this, user)
                            FirestoreClass().getUserDetails(this@UserRegistrationActivity)
                            snackSuccessCreated.show()
                            finish()
                        } else {
                            //create a snackbar with the specific firebase error
                            val snackError = Snackbar.make(
                                view,
                                task.exception!!.message.toString(),
                                Snackbar.LENGTH_LONG
                            )
                                snackError.view.setBackgroundColor(ContextCompat.getColor(this@UserRegistrationActivity, R.color.ColourSnackbarError))
                            snackError.show()
                        }
                    }
            }
        }
    }

    fun userRegisterSuccess(user: User) {
        Log.i("Username: ", user.username)
        Log.i("Email: ", user.email)
        startActivity(Intent(this@UserRegistrationActivity, HomePageActivity::class.java))
        finish()
    }
}