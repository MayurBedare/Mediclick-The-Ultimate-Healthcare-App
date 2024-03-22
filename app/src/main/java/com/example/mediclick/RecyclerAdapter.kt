package com.example.mediclick

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class RecyclerAdapter(private val context: Context, private val items: List<PostHistory>) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val username: TextView = itemView.findViewById(R.id.name)
        val postBody: TextView = itemView.findViewById(R.id.chatContent)
        val postedDate: TextView = itemView.findViewById(R.id.date)
        val image: ImageView = itemView.findViewById(R.id.imageview_chat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.post_history_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.username.text = item.uid
//        holder.postBody.text = item.postBody
//        holder.postedDate.text = item.postedDate.toString()

        // Load user image using Picasso
        database.child(item.uid).get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                Picasso.get().load(snapshot.child("profileImageUrl").value.toString()).into(holder.image)
            } else {
                Toast.makeText(context, "User doesn't exist.", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to retrieve user information.", Toast.LENGTH_SHORT).show()
        }

        // Check if the current user is the author of the post
        if (FirebaseAuth.getInstance().uid == item.uid) {
            holder.postBody.setTextColor(ContextCompat.getColor(context, R.color.white))
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}
