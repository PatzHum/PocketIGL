package me.phum.pocketigl.lobby


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_session.*
import kotlinx.android.synthetic.main.fragment_lobby.*
import me.phum.pocketigl.MapActivity

import me.phum.pocketigl.R
import me.phum.pocketigl.Session
import me.phum.pocketigl.SessionActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_SESSION_CODE = "arg_session_code"

/**
 * A simple [Fragment] subclass.
 * Use the [LobbyFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class LobbyFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var sessionCode: String
    private var delegate : Delegate? = null
    var userList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments!!.let {
            sessionCode = it.getString(ARG_SESSION_CODE)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lobby, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (activity is Delegate) {
            delegate = activity as Delegate
        }
        session_id.text = sessionCode

        startButton.setOnClickListener {
            delegate?.onStartSession(sessionCode)
        }
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("pocketigl").child("sessions")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(snapshot: DataSnapshot?) {
                val users = snapshot!!.child(sessionCode).child("users").children
                users.forEach {
                    userList.add(it.value.toString())
                }
            }
        })
    }

    interface Delegate {
        fun onStartSession(sessionCode: String)
    }

    companion object {
        @JvmStatic
        fun newInstance(sessionCode: String) =
                LobbyFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_SESSION_CODE, sessionCode)
                    }
                }
    }
}
