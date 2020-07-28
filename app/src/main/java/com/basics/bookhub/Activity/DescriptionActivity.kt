package com.basics.bookhub.Activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.basics.bookhub.Database.BookDatabase
import com.basics.bookhub.Database.BookEntity
import com.basics.bookhub.R
import com.basics.bookhub.util.ConectionManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_descriptive.*
import org.json.JSONException
import org.json.JSONObject

class DescriptionActivity : AppCompatActivity() {

    lateinit var txtBookName: TextView
    lateinit var txtAuthorName: TextView
    lateinit var txtPrice: TextView
    lateinit var txtRating: TextView
    lateinit var imgBookImage: ImageView

    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar

    lateinit var btnAddFavourite: Button

    var bookId: String = "100"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_descriptive)

        txtBookName = findViewById(R.id.txtBookName)
        txtAuthorName = findViewById(R.id.txtAuthorName)
        txtPrice = findViewById(R.id.txtPrice)
        txtRating = findViewById(R.id.txtRating)
        imgBookImage = findViewById(R.id.imgBookIcon)
        btnAddFavourite = findViewById(R.id.btnAddFavourite)

        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        progressLayout = findViewById(R.id.progressLayout)
        progressLayout.visibility = View.VISIBLE


        if (intent != null) {
            bookId = intent.getStringExtra("book_id")
        } else {
            Toast.makeText(
                this@DescriptionActivity,
                "some unwanted error happened!!",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }


        if (bookId == "100") {
            finish()
            Toast.makeText(this@DescriptionActivity, "some error accured", Toast.LENGTH_SHORT)
                .show()

        }

        val queue = Volley.newRequestQueue(this@DescriptionActivity)

        val url = "http://13.235.250.119/v1/book/get_book/"

        val jsonParams = JSONObject()
        jsonParams.put("book_id", bookId)

        if (ConectionManager().InternetConnection(this@DescriptionActivity)) {


            val jsonRequest =
                object : JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {

                    try {
                        val success = it.getBoolean("success")
                        if (success) {
                            val jsonObject = it.getJSONObject("book_data")
                            progressLayout.visibility = View.GONE
                            val bookImageUrl = jsonObject.getString("image")
                            Picasso.get().load(bookImageUrl).error(R.drawable.default_book_cover)
                                .into(imgBookImage)

                            txtBookName.text = jsonObject.getString("name").toString().trim()
                            txtAuthorName.text = jsonObject.getString("author").toString().trim()
                            txtRating.text = jsonObject.getString("rating").toString().trim()
                            txtPrice.text = jsonObject.getString("price").toString().trim()
                            txtBookDesc.text = jsonObject.getString("description").toString().trim()


                            val bookEntity = BookEntity(
                                bookId?.toInt() as Int,
                                    txtBookName.text.toString(),
                                txtAuthorName.text.toString(),
                                txtRating.text.toString(),
                                txtPrice.text.toString(),
                                txtBookDesc.text.toString(),
                                bookImageUrl
                            )


                            val checkFav = DBAsyncTask(
                                applicationContext,
                                bookEntity,
                                1
                            ).execute()
                            val isFav = checkFav.get()

                            if (isFav) {
                                btnAddFavourite.text = "Remove from Favourites"
                            } else {
                                btnAddFavourite.text = "Add to Favourites"
                            }

                            btnAddFavourite.setOnClickListener {
                                if (!DBAsyncTask(
                                        applicationContext,
                                        bookEntity,
                                        1
                                    ).execute()
                                        .get()
                                ) {

                                    val async =
                                        DBAsyncTask(
                                            applicationContext,
                                            bookEntity,
                                            2
                                        ).execute()
                                    val result = async.get()

                                    if (result) {
                                        Toast.makeText(
                                            this@DescriptionActivity,
                                            "Book added to favourite",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        btnAddFavourite.text = "Remove from Favourites"
                                    } else {
                                        Toast.makeText(
                                            this@DescriptionActivity,
                                            "some error occurred",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                } else {
                                    val async =
                                        DBAsyncTask(
                                            applicationContext,
                                            bookEntity,
                                            3
                                        ).execute()
                                    val result = async.get()

                                    if (result) {
                                        Toast.makeText(
                                            this@DescriptionActivity,
                                            "Book removed from favourite",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        btnAddFavourite.text = "Add to Favourite"


                                    } else {
                                        Toast.makeText(
                                            this@DescriptionActivity,
                                            "Some error occurred",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                    }
                                }
                            }

                        } else {
                            Toast.makeText(
                                this@DescriptionActivity,
                                "some error accured",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    } catch (e: JSONException) {
                        Toast.makeText(
                            this@DescriptionActivity,
                            "some error accured",
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                }, Response.ErrorListener {
                    Toast.makeText(
                        this@DescriptionActivity,
                        "some error accured",
                        Toast.LENGTH_SHORT
                    ).show()

                }) {


                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "ec66f7766ff4a4"
                        return headers
                    }
                }


            queue.add(jsonRequest)
        } else {
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle("Network Connection")
            alertDialog.setMessage("Internet is not connected")

            alertDialog.setPositiveButton("open settings") { text, listener ->

                val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                finish()
            }

            alertDialog.setNegativeButton("exit") { text, listene ->

                ActivityCompat.finishAffinity(this@DescriptionActivity)


            }
            alertDialog.create()
            alertDialog.show()
        }


    }


    class DBAsyncTask(val context: Context, val bookEntity: BookEntity, val mode: Int) :
        AsyncTask<Void, Void, Boolean>() {

        /*
            Mode1 -> check db if the book is favourite or not
            Mode2 -> Save the book into DB as favourite
            Mode3 -> Remove the favourite book
         */


        val db = Room.databaseBuilder(context, BookDatabase::class.java, "book-db").build()

        override fun doInBackground(vararg params: Void?): Boolean {

            when (mode) {

                1 -> {

                    val book: BookEntity? = db.bookDao().getBookById(bookEntity.book_id.toString())
                    db.close()
                    return book != null

                }

                2 -> {

                    db.bookDao().insert(bookEntity)
                    db.close()
                    return true

                }

                3 -> {

                    db.bookDao().delete(bookEntity)
                    db.close()
                    return true
                }


            }




            return false
        }


    }
}
