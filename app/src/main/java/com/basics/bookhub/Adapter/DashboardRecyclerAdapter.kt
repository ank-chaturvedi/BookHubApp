package com.basics.bookhub.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.basics.bookhub.Activity.DescriptionActivity
import com.basics.bookhub.R
import com.basics.bookhub.model.Book
import com.squareup.picasso.Picasso

class DashboardRecyclerAdapter(val context: Context,val itemList:ArrayList<Book>):RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>(),Filterable{

    var exampleItemList:ArrayList<Book>

    init {
        exampleItemList = ArrayList(itemList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_dashboard,parent,false)

        return DashboardViewHolder(
            view
        )
    }



    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val author = itemList[position].author
        val book = itemList[position].name
        val cost = itemList[position].price
        val rating = itemList[position].rating
        val image = itemList[position].image


        holder.textAuthor.text = author
        holder.textBookName.text = book
        holder.textRating.text = rating
        holder.textPrice.text = cost
        Picasso.get().load(image).error(R.drawable.default_book_cover).into(holder.imgBook)

        holder.llDashboard.setOnClickListener {
            val intent = Intent(context, DescriptionActivity::class.java)
            intent.putExtra("book_id",itemList[position].book_id)
            context.startActivity(intent)
        }

        holder.textBookName.setOnClickListener {
            Toast.makeText(context,"yes you clicked on $book",Toast.LENGTH_SHORT).show()

        }



    }

    override fun getItemCount(): Int {
        return itemList.size
    }


    class DashboardViewHolder(view:View):RecyclerView.ViewHolder(view){

        val textBookName:TextView = view.findViewById(R.id.txtBookName)
        val textAuthor:TextView = view.findViewById(R.id.txtAuthorName)
        val textRating:TextView = view.findViewById(R.id.txtRating)
        val textPrice:TextView = view.findViewById(R.id.txtPrice)
        val imgBook:ImageView = view.findViewById(R.id.imgBookIcon)
        val llDashboard: RelativeLayout = view.findViewById(R.id.llDashboard)
    }

    override fun getFilter(): Filter {
        TODO("Not yet implemented")
        return exampleFilter
    }

    private val exampleFilter:Filter = object:Filter(){
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = ArrayList<Book>()

            if(constraint == null || constraint.length==0) {
                filteredList.addAll(exampleItemList)
            }
            else{
                val pattern = constraint.toString().trim().toLowerCase()

                for(values in exampleItemList){
                    if(values.name.toLowerCase().contains(pattern))
                        filteredList.add(values)
                }
            }

            val results:FilterResults = FilterResults()
            results.values = filteredList

            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            itemList.clear()
            itemList.addAll(results!!.values as ArrayList<Book>)
            notifyDataSetChanged()
        }


    }


}