package com.cittis.signsup.controller.firebase.login

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.VisibleForTesting
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.navigation.Navigation
import com.cittis.signsup.R
import com.cittis.signsup.actions.ActionsRequest
import com.cittis.signsup.actions.EndPoints
import com.cittis.signsup.actions.FetchDataListener
import com.cittis.signsup.connection.DataBase
import com.cittis.signsup.connection.GETAPIRequest
import com.cittis.signsup.connection.POSTAPIRequest
import com.cittis.signsup.connection.RequestQueueService
import com.cittis.signsup.controller.firebase.tracking.TrackerService
import com.cittis.signsup.controller.plugins.ConvertJSON
import com.cittis.signsup.model.CittisListSignal
import com.cittis.signsup.model.DataUser
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import org.json.JSONObject

class Login : Fragment() {

    // Main Variables
    private var fragment = this
    private lateinit var viewMain: View
    private lateinit var connection: DataBase
    // Make Bundle
    val bundle = Bundle()
    // [START declare_auth]
    private lateinit var auth: FirebaseAuth
    // [END declare_auth]

    // Google SignIn
    private lateinit var googleSignInClient: GoogleSignInClient


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
        // [START config_signin]
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        // [END config_signin]

        googleSignInClient = GoogleSignIn.getClient(viewMain.context, gso)


        // [START initialize_auth]
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        // [END initialize_auth]


        // Actions
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


    // [START onactivityresult]
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == ActionsRequest.RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                // [START_EXCLUDE]
                updateUI(null)
                // [END_EXCLUDE]
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id!!)
        // [START_EXCLUDE silent]
        showProgressDialog()
        // [END_EXCLUDE]

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Snackbar.make(
                        viewMain.findViewById<LinearLayout>(R.id.main_layout),
                        "Authentication Failed.",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }

                // [START_EXCLUDE]
                hideProgressDialog()
                // [END_EXCLUDE]
            }
    }
    // [END auth_with_google]


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
                Toast.makeText(
                    viewMain.context, R.string.auth_failed,
                    Toast.LENGTH_SHORT
                ).show()
            }
            hideProgressDialog()
            // [END_EXCLUDE]
        }
        // [END sign_in_with_email]
    }


    private fun setSignOut() {
        /*viewMain.findViewById<Button>(R.id.signOutButton).setOnClickListener {
            signOut()
        }*/
    }

    private fun signOut() {
        // TODO: SALIR
        auth.signOut()
        updateUI(null)
        // Google sign out
        googleSignInClient.signOut().addOnCompleteListener {
            updateUI(null)
        }
    }

    private fun setSendEmailVerification() {
        /*viewMain.findViewById<Button>(R.id.verifyEmailButton).setOnClickListener {
            sendEmailVerification()
        }*/
    }

    private fun sendEmailVerification() {
        // Disable button
        //viewMain.findViewById<Button>(R.id.verifyEmailButton).isEnabled = false

        // Send verification email
        // [START send_email_verification]
        val user = auth.currentUser
        user?.sendEmailVerification()?.addOnCompleteListener { task ->
            // [START_EXCLUDE]
            // Re-enable button
            // viewMain.findViewById<Button>(R.id.verifyEmailButton).isEnabled = true

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

    private lateinit var userMain: FirebaseUser

    // [END on_start_check_user]
    private fun updateUI(user: FirebaseUser?) {

        hideProgressDialog()
        if (user != null) {
            userMain = user
            /*viewMain.findViewById<TextView>(R.id.status).text = getString(
                R.string.emailpassword_status_fmt,
                user.email, user.isEmailVerified
            )*/
            //viewMain.findViewById<TextView>(R.id.detail).text = getString(R.string.firebase_status_fmt, user.uid)


            viewMain.findViewById<LinearLayout>(R.id.emailPasswordButtons).visibility = View.GONE
            //viewMain.findViewById<LinearLayout>(R.id.emailPasswordFields).visibility = View.GONE
            //viewMain.findViewById<LinearLayout>(R.id.signedInButtons).visibility = View.VISIBLE

            //viewMain.findViewById<Button>(R.id.verifyEmailButton).isEnabled = !user.isEmailVerified


            // Check is Login and Verify
            checkLogin()
        } else {
            //viewMain.findViewById<TextView>(R.id.status).setText(R.string.signed_out)
            //viewMain.findViewById<TextView>(R.id.detail).text = null

            viewMain.findViewById<LinearLayout>(R.id.emailPasswordButtons).visibility = View.VISIBLE
            //viewMain.findViewById<LinearLayout>(R.id.emailPasswordFields).visibility = View.VISIBLE
            //viewMain.findViewById<LinearLayout>(R.id.signedInButtons).visibility = View.GONE
        }
    }


    private fun checkLogin() {
        // Check Login - Email Verification
        var isLogin = userMain.isEmailVerified
        if (isLogin) {

            // Flag Data - Traking
            EndPoints.FireBaseID = userMain.uid


            var url = EndPoints.URL_CHECK_USER(EndPoints.FireBaseID)
            getApiCall(url, "checkUser")


        } else {
            Toast.makeText(
                viewMain.context, "Please Verify Email.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun startServiceTracking() {
        val intent = Intent(viewMain.context, TrackerService::class.java)
        // start your next activity
        viewMain.context.startService(intent)
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

    fun Boolean.toInt() = if (this) 1 else 0

    var option = ""
    /** GET API */
    private fun getApiCall(url: String, option: String) {
        this.option = option
        try {
            //Create Instance of GETAPIRequest and call it's
            //request() method
            val getapiRequest = GETAPIRequest()
            //Attaching only part of URL as base URL is given
            //in our GETAPIRequest(of course that need to be same for all case)
            getapiRequest.request(viewMain.context, fetchGetResultListener, url)
            // Toast.makeText(viewMain.context, "GET API called", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    //Implementing interfaces of FetchDataListener for GET api request
    private var fetchGetResultListener: FetchDataListener = object : FetchDataListener {
        override fun onFetchComplete(data: JSONObject) {
            //Fetch Complete. Now stop progress bar  or loader
            //you started in onFetchStart
            RequestQueueService.cancelProgressDialog()
            try {
                //Now check result sent by our GETAPIRequest class
                if (data.has("success")) {
                    val success = data.getInt("success")
                    if (success == 1) {
                        val response = data.getJSONObject("response")
                        if (response != null) {
                            //Display the result
                            var array = response.toString(4)
                            var tempValue = ConvertJSON(array)["data"]
                            // Init Process
                            initProcessGet(tempValue)
                        }
                    } else {
                        RequestQueueService.showAlert("Error! No data fetched", viewMain.context)
                    }
                }
            } catch (e: Exception) {
                RequestQueueService.showAlert("Something went wrong", viewMain.context)
                e.printStackTrace()
            }

        }

        override fun onFetchFailure(msg: String) {
            RequestQueueService.cancelProgressDialog()
            //Show if any error message is there called from GETAPIRequest class
            RequestQueueService.showAlert(msg, viewMain.context)
        }

        override fun onFetchStart() {
            //Start showing progressbar or any loader you have
            RequestQueueService.showProgressDialog(fragment, viewMain.context)
        }
    }

    var check = false

    private fun initProcessGet(values: Any) {
        // Data - User
        var dataUser =
            DataUser(userMain.email.toString(), EndPoints.FireBaseID, userMain.isEmailVerified.toInt())

        when (option) {
            "checkUser" -> {
                if (values == "1") {
                    //** Id Project **/
                    var url = EndPoints.URL_GET_COUNT_INVENTORY(EndPoints.FireBaseID)
                    getApiCall(url, "count")
                } else {
                    //** Id Project **/
                    var url = EndPoints.URL_POST_ADD_FIREBASE_USER(EndPoints.FireBaseID)
                    postApiCall(url, dataUser)
                }
            }
            "count" -> {

                EndPoints.FireBasePath = dataUser.firebase_path

                // Make Object Main
                var cittisDB: CittisListSignal = CittisListSignal(values as Int, dataUser, null)
                // Show Data
                Log.e("Data-Login", cittisDB.toString())
                // Set and Send Data Main
                bundle.putParcelable("CittisDB", cittisDB)
                // Start Tracking
                startServiceTracking()
                // Init Action
                Navigation.findNavController(viewMain).navigate(R.id.municipalities, bundle)
            }
            else -> {
                Log.e("Error", option)

            }
        }

    }

    /** Post Elements **/
    private fun postApiCall(url: String, data: DataUser) {
        try {
            //Create Instance of POSTAPIRequest and call it's
            //request() method
            val postApiRequest = POSTAPIRequest()
            //Attaching only part of URL as base URL is given
            //in our POSTAPIRequest(of course that need to be same for all case)

            var params = HashMap<String, String>()
            try {
                //Creating POST body in JSON format
                //to send in POST request
                params.put("data", data.getData())
            } catch (e: Exception) {
                e.printStackTrace()
            }
            postApiRequest.request(viewMain.context, fetchPostResultListener, params, url)
            Toast.makeText(viewMain.context, "POST API called", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    //Implementing interfaces of FetchDataListener for POST api request
    private var fetchPostResultListener: FetchDataListener = object : FetchDataListener {
        override fun onFetchComplete(data: JSONObject) {

            //Fetch Complete. Now stop progress bar  or loader
            //you started in onFetchStart
            RequestQueueService.cancelProgressDialog()
            try {
                //Now check result sent by our POSTAPIRequest class
                if (data.has("success")) {
                    val success = data.getInt("success")
                    if (success == 1) {
                        val response = data.getJSONObject("response")
                        if (response != null) {
                            //Display the result
                            //Or, You can do whatever you need to
                            //do with the JSONObject
                            Log.e("Data", response.toString(4))
                            //if(response.getString("data")=="Complete") { }
                        }
                    } else {
                        RequestQueueService.showAlert("Error! No data fetched", viewMain.context)
                    }
                }
            } catch (e: Exception) {
                RequestQueueService.showAlert("Something went wrong", viewMain.context)
                e.printStackTrace()
            }

        }

        override fun onFetchFailure(msg: String) {
            RequestQueueService.cancelProgressDialog()
            //Show if any error message is there called from POSTAPIRequest class
            RequestQueueService.showAlert(msg, viewMain.context)
        }

        override fun onFetchStart() {
            //Start showing progressbar or any loader you have
            RequestQueueService.showProgressDialog(fragment, viewMain.context)
        }
    }

}
