package me.phum.pocketigl

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.text.InputFilter
import android.view.inputmethod.InputMethodManager
import com.google.firebase.FirebaseError
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_session.*
import me.phum.pocketigl.lobby.LobbyFragment

class SessionActivity : AppCompatActivity(), LobbyFragment.Delegate {
    override fun onStartSession(sessionCode: String) {
        startActivity(Intent(this, MapActivity::class.java))
    }

    private val firebaseAuth = FirebaseAuth.getInstance()
    private var lobbyFragment : LobbyFragment? = null
    val context = this;
    var currentSession = "";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_session)
        if (firebaseAuth.currentUser == null) {
            Snackbar.make(root, "Not signed in!", Snackbar.LENGTH_INDEFINITE).show()
        }

        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("pocketigl").child("sessions")

        joinButton.setOnClickListener {
            if(sessionCodeInput.text.toString().length < 4) {
                Snackbar.make(root, "Invalid session code!", Snackbar.LENGTH_SHORT).show()
            } else if (currentSession == sessionCodeInput.text.toString()) {
                Snackbar.make(root, "Already in this session!", Snackbar.LENGTH_LONG).show()
            } else {
                currentSession = sessionCodeInput.text.toString()

                ref.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onDataChange(snapshot: DataSnapshot?) {
                        val session = snapshot!!.child(currentSession)
                        if(session.exists()) {
                            addUser(FirebaseAuth.getInstance().currentUser!!, currentSession, "player")
                            Snackbar.make(root, "Joined session", Snackbar.LENGTH_SHORT).show()
                            joinSession(currentSession)
                        } else {
                            Snackbar.make(root, "Session doesn't exist!", Snackbar.LENGTH_INDEFINITE).show()
                        }
                    }
                })
                hideKeyboard()
            }
        }

        sessionCodeInput.filters = arrayOf(InputFilter.AllCaps(), InputFilter.LengthFilter(4))

        //Uncomment the section below to navigate to the MapActivity using the 'Create New Section' button
/*        val button1 = findViewById<Button>(R.id.createButton)

        button1.setOnClickListener(object: View.OnClickListener {
            override fun onClick(view: View): Unit {
                // Handler code here.
                val intent = Intent(context, MapActivity::class.java);
                startActivity(intent);
            }
        })*/

        createButton.setOnClickListener {
            val session = Session()
            ref.child(session.sessionCode).setValue(session)
            Snackbar.make(root, "Session code: " + session.sessionCode, Snackbar.LENGTH_LONG).show()
            joinSession(session.sessionCode)
            sessionCodeInput.setText(session.sessionCode)
            addUser(FirebaseAuth.getInstance().currentUser!!, currentSession, "admin")
        }
    }

    fun addUser(user: FirebaseUser, sessionId: String, role: String) {
        val sessionsRef = FirebaseDatabase.getInstance().getReference("pocketigl").child("sessions").child(sessionId).child("users")
        val sessionUpdate = HashMap<String, String>()
        sessionUpdate.put(user.uid, role)
        sessionsRef.updateChildren(sessionUpdate as Map<String, String>)

        val usersRef = FirebaseDatabase.getInstance().getReference("pocketigl").child("users")
        val usersUpdate = HashMap<String, String>()
        usersUpdate.put(user.uid, user.displayName.toString())
        usersRef.updateChildren(usersUpdate as Map<String, String>)
    }

    fun joinSession(sessionId: String) {
        currentSession = sessionId
        val oldLobbyFragment = lobbyFragment
        lobbyFragment = LobbyFragment.newInstance(sessionId)
        supportFragmentManager.beginTransaction().apply {
            oldLobbyFragment?.let {
                remove(it)
            }
            add(R.id.lobby, lobbyFragment)
            commit()
        }
    }

    fun hideKeyboard() {
        try {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        } catch (e: Exception) {
        }
    }
}