package com.basics.bookhub.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.basics.bookhub.Database.BookEntity
import com.basics.bookhub.R
import com.squareup.picasso.Picasso

class FavouriteRecyclerAdapter(val context:Context,val bookList:List<BookEntity>):RecyclerView.Adapter<FavouriteRecyclerAdapter.FavouriteViewHolder>() {

    class FavouriteViewHolder(view: View):RecyclerView.ViewHolder(view){

        val txtBookName:TextView = view.findViewById(R.id.txtBookName)
        val txtAuthorName:TextView = view.findViewById(R.id.txtAuthorName)
        val txtPrice:TextView = view.findViewById(R.id.txtPrice)
        val txtRating:TextView = view.findViewById(R.id.txtRating)
        val imgBookIcon: ImageView = view.findViewById(R.id.imgBookIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val view  = LayoutInflater.from(parent.context).inflate(R.layout.recylcer_favourite_single_row,parent,false)

        return FavouriteViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val book = bookList[position]

        holder.txtBookName.text = book.name
        holder.txtAuthorName.text = book.author
        holder.txtPrice.text = book.price
        holder.txtRating.text = book.rating

        Picasso.get().load(book.image).error(R.drawable.default_book_cover).into(holder.imgBookIcon)
    }
}