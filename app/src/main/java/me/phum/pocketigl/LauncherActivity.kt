package me.phum.pocketigl

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_launcher.*
import me.phum.pocketigl.components.HotspotButton


class LauncherActivity : AppCompatActivity() {
    companion object {
        const val REQUEST_SIGN_IN = 1
        const val TAG = "LAUNCHER_ACTIVITY"
    }
    val firebaseAuth = FirebaseAuth.getInstance()
    private lateinit var gsiClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build()
        gsiClient = GoogleSignIn.getClient(this, gso)
        startActivityForResult(gsiClient.signInIntent, REQUEST_SIGN_IN)
        hotspot_button.availableActions = HotspotButton.HOTSPOTS_PLANT_SITE
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.currentUser?.let {
            updateUI(it)
        }
    }

    private fun updateUI(user: FirebaseUser) {
        Snackbar.make(root, "Signed in successfully", Snackbar.LENGTH_INDEFINITE).show()
        //startActivity(Intent(this, SessionActivity::class.java))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(task : Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account)
        } catch (e: ApiException) {
            Snackbar.make(findViewById(R.id.root), "signInResult:failed code=" + e.statusCode, Snackbar.LENGTH_INDEFINITE).show()
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success")
                        val user = firebaseAuth.getCurrentUser()?.let {
                            updateUI(it)
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                        Snackbar.make(findViewById(R.id.root), "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                    }
                })
    }
}