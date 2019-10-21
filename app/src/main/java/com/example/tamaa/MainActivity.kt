package com.example.tamaa

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController

import android.view.Menu
import android.view.MenuItem
import android.view.View


import android.app.AlertDialog
import android.widget.EditText
import android.text.InputType
import android.widget.ImageView
import android.widget.LinearLayout

import android.content.pm.PackageManager
import android.os.Build.*
import android.Manifest
import android.content.Intent

import android.app.Activity
import android.widget.Toast
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.media.Image
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Calendar

import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.Exclude
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var image: ImageView
    //private lateinit var database: DatabaseReference
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val user = auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Float Button
        val fab: View = findViewById(R.id.fab)
        fab.setOnClickListener { view ->

            //Alert Dialog
            val context = this.applicationContext
            val builder = AlertDialog.Builder(this@MainActivity).create()
            val layout = LinearLayout(context)
            layout.orientation = LinearLayout.VERTICAL

            val name = EditText(context)
            name.setHint("Titre de L'annonce")
            name.inputType = InputType.TYPE_CLASS_TEXT
            name.setTypeface(Typeface.DEFAULT_BOLD)
            layout.addView(name)

            // Upload image
            image = ImageView(context)
            image.setImageResource(R.drawable.tuna)
            image.setOnClickListener { v ->
                if(uploadImage2()) {
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    startActivityForResult(intent, IMAGE_PICK_CODE)
                }
                //startActivityForResult(intent, IMAGE_PICK_CODE)
                //if (uploadImage2()) {
                //    pickImageFromGallery2(image)
                //}
            }
            image.adjustViewBounds = true
            image.setMaxHeight(1000)
            layout.addView(image)

            val price = EditText(context)
            price.setHint("Prix")
            price.inputType = InputType.TYPE_CLASS_NUMBER
            layout.addView(price)

            val place = EditText(context)
            place.setHint("Lieu")
            place.inputType = InputType.TYPE_CLASS_TEXT
            layout.addView(place)


            val time = EditText(context)
            time.setHint("Heure")
            time.setOnFocusChangeListener(View.OnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    //handle your situation here
                    // TODO Auto-generated method stub
                    val mcurrentTime = Calendar.getInstance()
                    val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
                    val minute = mcurrentTime.get(Calendar.MINUTE)
                    val mTimePicker: TimePickerDialog
                    mTimePicker = TimePickerDialog(this@MainActivity,
                        TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                            time.setText(
                                "$selectedHour:$selectedMinute"
                            )
                        }, hour, minute, true
                    )
                    mTimePicker.setTitle("Select Time")
                    mTimePicker.show()
                }
            })
            layout.addView(time)

            builder.setView(layout)
            builder.setButton(
                AlertDialog.BUTTON_POSITIVE, "Poster"
            ) { dialog, which ->
                postAnnouncement(
                    name.text.toString(),
                    image.drawable,
                    Integer.parseInt(price.text.toString()),
                    place.text.toString(),
                    time.text.toString()
                )}
            builder.setButton(
                AlertDialog.BUTTON_NEGATIVE, "Annuler"
            ) { dialog, which -> dialog.dismiss()}
            builder.show()
        }

    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    private fun postAnnouncement(name: String, image: Drawable, price: Int, place: String, time: String ) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (user != null) {

            data class Post(
                var name: String? = "",
                //var image: Drawable? = null,
                var price: Int? = 0,
                var place: String? = "",
                var time: String? = ""
            ) {

                //@Exclude
                fun toMap(): Map<String, Any?> {
                    return mapOf(
                        "name" to name,
                        //"image" to image,
                        "price" to price,
                        "place" to place,
                        "time" to time
                    )
                }
            }
            System.out.println(user.email)

            val post = Post(name, price, place, time)
            db.collection("Posts")
                .add(post.toMap())
                .addOnSuccessListener { documentReference ->
                    System.out.println("Success!")
                }
                .addOnFailureListener { e ->
                    System.out.println(e)
                }
        }
        else {
            System.out.println("Not Authenticated")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml
        return when (item.itemId) {
            R.id.profile -> {
                val myIntent = Intent(this@MainActivity, LoginActivity::class.java)
                //myIntent.putExtra("key", value) //Optional parameters
                this@MainActivity.startActivity(myIntent)
                return true
            }
            R.id.settings -> {
                val myIntent = Intent(this@MainActivity, SettingsActivity::class.java)
                //myIntent.putExtra("key", value) //Optional parameters
                this@MainActivity.startActivity(myIntent)
                return true
            }
            else -> super.onOptionsItemSelected(item)

        }
    }

    private fun uploadImage() {
        if (VERSION.SDK_INT >= VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED){
                //permission denied
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                //show popup to request runtime permission
                requestPermissions(permissions, PERMISSION_CODE);
            }
            else{
                //permission already granted
                pickImageFromGallery();
            }
        }
        else{
            //system OS is < Marshmallow
            pickImageFromGallery();
        }
    }

    private fun uploadImage2(): Boolean {
        if (VERSION.SDK_INT >= VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED){
                //permission denied
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                //show popup to request runtime permission
                requestPermissions(permissions, PERMISSION_CODE);
            }
            else{
                //permission already granted
                return true
            }
        }
        else{
            //system OS is < Marshmallow
            return true
        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            image.setImageURI(data?.data)

        }
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000;
        //Permission code
        private val PERMISSION_CODE = 1001;
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size >0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    pickImageFromGallery()
                }
                else{
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}