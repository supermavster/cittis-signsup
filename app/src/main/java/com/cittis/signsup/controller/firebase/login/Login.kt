package com.cittis.signsup.controller.firebase.login

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.support.annotation.VisibleForTesting
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.navigation.Navigation
import com.cittis.signsup.R
import com.cittis.signsup.connection.DataBase
import com.cittis.signsup.model.CittisListSignal
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Login : Fragment() {

    // Main Variables
    private lateinit var viewMain: View
    private lateinit var connection: DataBase
    // Make Bundle
    val bundle = Bundle()
    // [START declare_auth]
    private lateinit var auth: FirebaseAuth
    // [END declare_auth]

    companion object {
        private const val TAG = "EmailPassword"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Init View
        viewMain = inflater.inflate(R.layout.fragment_login, container, false)

        // Init Process
        initProcess()

        // Return Data
        return viewMain
    }

    private fun initProcess() {
        // [START initialize_auth]
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        // [END initialize_auth]

        // Actions
        setCreateAccount()
        setSignIn()
        setSignOut()
        setSendEmailVerification()

    }

    // [START on_start_check_user]
    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun setCreateAccount() {
        viewMain.findViewById<Button>(R.id.emailCreateAccountButton).setOnClickListener {
            var email = viewMain.findViewById<EditText>(R.id.fieldEmail).text.toString()
            var password = viewMain.findViewById<EditText>(R.id.fieldPassword).text.toString()
            createAccount(email, password)
        }
    }

    private fun createAccount(email: String, password: String) {
        Log.d(TAG, "createAccount:$email")
        if (!validateForm()) {
            return
        }

        showProgressDialog()

        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            // .addOnCompleteListener(viewMain.context) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "createUserWithEmail:success")
                val user = auth.currentUser
                updateUI(user)
            } else {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                Toast.makeText(
                    viewMain.context, "Authentication failed.",
                    Toast.LENGTH_SHORT
                ).show()
                updateUI(null)
            }

            // [START_EXCLUDE]
            hideProgressDialog()
            // [END_EXCLUDE]
        }
        // [END create_user_with_email]
    }

    private fun setSignIn() {

        viewMain.findViewById<Button>(R.id.emailSignInButton).setOnClickListener {
            var email = viewMain.findViewById<EditText>(R.id.fieldEmail).text.toString()
            var password = viewMain.findViewById<EditText>(R.id.fieldPassword).text.toString()
            signIn(email, password)

        }

    }

    private fun signIn(email: String, password: String) {
        Log.d(TAG, "signIn:$email")
        if (!validateForm()) {
            return
        }

        showProgressDialog()

        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            // .addOnCompleteListener(viewMain.context) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "signInWithEmail:success")
                val user = auth.currentUser
                updateUI(user)

            } else {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "signInWithEmail:failure", task.exception)
                Toast.makeText(
                    viewMain.context, "Authentication failed.",
                    Toast.LENGTH_SHORT
                ).show()
                updateUI(null)
            }

            // [START_EXCLUDE]
            if (!task.isSuccessful) {
                viewMain.findViewById<TextView>(R.id.status).setText(R.string.auth_failed)
            }
            hideProgressDialog()
            // [END_EXCLUDE]
        }
        // [END sign_in_with_email]
    }

    private fun checkLogin(user: FirebaseUser?) {
        // Check Login - Email Verification
        var isLogin = user!!.isEmailVerified
        if (isLogin) {


            // Start Main Object
            var isLoginInt = 1
            // Make Object Main
            var cittisDB: CittisListSignal = CittisListSignal(isLoginInt)
            // Show Data
            Log.e("Data-Login", cittisDB.toString())
            // Set and Send Data Main
            bundle.putParcelable("CittiDB", cittisDB)
            // Init Action
            Navigation.findNavController(viewMain).navigate(R.id.municipalities, bundle)
        } else {
            Toast.makeText(
                viewMain.context, "Please Verify Email.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setSignOut() {
        viewMain.findViewById<Button>(R.id.signOutButton).setOnClickListener {
            signOut()
        }
    }

    private fun signOut() {
        auth.signOut()
        updateUI(null)
    }

    private fun setSendEmailVerification() {
        viewMain.findViewById<Button>(R.id.verifyEmailButton).setOnClickListener {
            sendEmailVerification()
        }
    }

    private fun sendEmailVerification() {
        // Disable button
        viewMain.findViewById<Button>(R.id.verifyEmailButton).isEnabled = false

        // Send verification email
        // [START send_email_verification]
        val user = auth.currentUser
        user?.sendEmailVerification()?.addOnCompleteListener { task ->
            // [START_EXCLUDE]
            // Re-enable button
            viewMain.findViewById<Button>(R.id.verifyEmailButton).isEnabled = true

            if (task.isSuccessful) {
                Toast.makeText(
                    viewMain.context,
                    "Verification email sent to ${user.email} ",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Log.e(TAG, "sendEmailVerification", task.exception)
                Toast.makeText(
                    viewMain.context,
                    "Failed to send verification email.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            // [END_EXCLUDE]
        }
        // [END send_email_verification]
    }

    // [END on_start_check_user]
    private fun updateUI(user: FirebaseUser?) {

        hideProgressDialog()
        if (user != null) {
            viewMain.findViewById<TextView>(R.id.status).text = getString(
                R.string.emailpassword_status_fmt,
                user.email, user.isEmailVerified
            )
            viewMain.findViewById<TextView>(R.id.detail).text = getString(R.string.firebase_status_fmt, user.uid)


            viewMain.findViewById<LinearLayout>(R.id.emailPasswordButtons).visibility = View.GONE
            viewMain.findViewById<LinearLayout>(R.id.emailPasswordFields).visibility = View.GONE
            viewMain.findViewById<LinearLayout>(R.id.signedInButtons).visibility = View.VISIBLE

            viewMain.findViewById<Button>(R.id.verifyEmailButton).isEnabled = !user.isEmailVerified


            // Check is Login and Verify
            checkLogin(user)
        } else {
            viewMain.findViewById<TextView>(R.id.status).setText(R.string.signed_out)
            viewMain.findViewById<TextView>(R.id.detail).text = null

            viewMain.findViewById<LinearLayout>(R.id.emailPasswordButtons).visibility = View.VISIBLE
            viewMain.findViewById<LinearLayout>(R.id.emailPasswordFields).visibility = View.VISIBLE
            viewMain.findViewById<LinearLayout>(R.id.signedInButtons).visibility = View.GONE
        }
    }

    @VisibleForTesting
    val progressDialog by lazy {
        ProgressDialog(viewMain.context)
    }

    fun showProgressDialog() {
        progressDialog.setMessage(getString(R.string.loading))
        progressDialog.isIndeterminate = true
        progressDialog.show()
    }

    fun hideProgressDialog() {
        if (progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }

    fun hideKeyboard(view: View) {
        val imm = viewMain.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onStop() {
        super.onStop()
        hideProgressDialog()
    }

    private fun validateForm(): Boolean {
        var valid = true

        val email = viewMain.findViewById<EditText>(R.id.fieldEmail).text.toString()
        if (TextUtils.isEmpty(email)) {
            viewMain.findViewById<EditText>(R.id.fieldEmail).error = "Required."
            valid = false
        } else {
            viewMain.findViewById<EditText>(R.id.fieldEmail).error = null
        }

        val password = viewMain.findViewById<EditText>(R.id.fieldPassword).text.toString()
        if (TextUtils.isEmpty(password)) {
            viewMain.findViewById<EditText>(R.id.fieldPassword).error = "Required."
            valid = false
        } else {
            viewMain.findViewById<EditText>(R.id.fieldPassword).error = null
        }

        return valid
    }


}
