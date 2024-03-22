package com.example.mediclick

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class AboutViewModel : ViewModel() {
    private lateinit var database: DatabaseReference

    val liveFirstName: MutableLiveData<String> by lazy {
        MutableLiveData<String>().also {
            fetchData()
        }
    }

    val liveProfilePicture: MutableLiveData<String> by lazy {
        MutableLiveData<String>().also {
            fetchData()
        }
    }

    private fun fetchData() {
        val uid = FirebaseAuth.getInstance().uid
        database = FirebaseDatabase.getInstance().getReference("users").child(uid!!)

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val firstname = snapshot.child("firstname").value.toString()
                    val profileImageUrl = snapshot.child("profileImageUrl").value.toString()

                    liveFirstName.value = firstname
                    liveProfilePicture.value = profileImageUrl
                } else {
                    Log.e("AboutViewModel", "User data does not exist")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("AboutViewModel", "Database error: ${error.message}")
            }
        })
    }
}
