package com.basics.bookhub.Fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.basics.bookhub.Adapter.DashboardRecyclerAdapter
import com.basics.bookhub.R
import com.basics.bookhub.model.Book
import com.basics.bookhub.util.ConectionManager
import org.json.JSONException
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap


class DashBoardFragment : Fragment() {

    lateinit var recyclerDashboard: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar


    lateinit var recyclerAdapter: DashboardRecyclerAdapter
    var bookInfoList = arrayListOf<Book>()

    var ratingComparator = Comparator<Book>{book1,book2 ->
        if(book1.rating.compareTo(book2.rating,true)==0){
            book1.name.compareTo(book2.name,true)
        } else{
            book1.rating.compareTo(book2.rating,true)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dash_board, container, false)

            setHasOptionsMenu(true)
        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)

        progressBar.visibility = View.VISIBLE

        recyclerDashboard = view.findViewById(R.id.recylerView)

        layoutManager = LinearLayoutManager(activity)









        if (ConectionManager().InternetConnection(activity as Context)) {
            val queue = Volley.newRequestQueue(activity as Context)


            val url = "http://13.235.250.119/v1/book/fetch_books/"

            val jsonObjectRequest =
                object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {

                    try {
                        progressLayout.visibility = View.GONE
                        val success = it.getBoolean("success")

                        if (success) {
                            val data = it.getJSONArray("data")

                            for (i in 0 until data.length()) {
                                val bookJsonObject = data.getJSONObject(i)
                                val bookObject = Book(
                                    bookJsonObject.getString("book_id").toString().trim(),
                                    bookJsonObject.getString("name").toString().trim(),
                                    bookJsonObject.getString("author").toString().trim(),
                                    bookJsonObject.getString("rating").toString().trim(),
                                    bookJsonObject.getString("price").toString().trim(),
                                    bookJsonObject.getString("image")
                                )
                                bookInfoList.add(bookObject)


                                recyclerAdapter =
                                    DashboardRecyclerAdapter(
                                        activity as Context,
                                        bookInfoList
                                    )

                                recyclerDashboard.layoutManager = layoutManager

                                recyclerDashboard.adapter = recyclerAdapter


                            }
                        } else {
                            Toast.makeText(
                                activity as Context,
                                "error is occured",
                                Toast.LENGTH_SHORT
                            ).show()


                        }

                    } catch (e: JSONException) {
                        Toast.makeText(
                            activity as Context,
                            "Some error accured",
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                }, Response.ErrorListener {


                    if (activity != null) {
                        Toast.makeText(
                            activity as Context,
                            "volley error accord",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }

                }) {

                    override fun getHeaders(): MutableMap<String, String> {

                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "ec66f7766ff4a4"
                        return headers
                    }

                }

            queue.add(jsonObjectRequest)
        } else {
            val alertDialog = AlertDialog.Builder(activity as Context)
            alertDialog.setTitle("Network Connection")
            alertDialog.setMessage("Internet is not connected")

            alertDialog.setPositiveButton("open settings") { text, listener ->

                val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                activity?.finish()
            }

            alertDialog.setNegativeButton("exit") { text, listene ->

                ActivityCompat.finishAffinity(activity as Activity)


            }
            alertDialog.create()
            alertDialog.show()

        }


        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.menu_dashboard,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item?.itemId

        if(id == R.id.actionSort){
            Collections.sort(bookInfoList,ratingComparator)
            bookInfoList.reverse()
        }

        recyclerAdapter.notifyDataSetChanged()
        return super.onOptionsItemSelected(item)
    }

}
