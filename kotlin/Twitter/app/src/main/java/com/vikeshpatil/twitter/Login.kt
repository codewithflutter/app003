package com.vikeshpatil.twitter

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_login.*
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

class Login : AppCompatActivity() {


    private var mAuth: FirebaseAuth? = null
    private var database = FirebaseDatabase.getInstance()
    private var databaseRefrence = database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance();

        ivProfile.setOnClickListener(View.OnClickListener {
            CheckStoragePermission()
        })

    }

    fun LoginToFirebase(email:String, password:String){

        mAuth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){
                    task ->
                if (task.isSuccessful){
                    Toast.makeText(applicationContext, "Login Successful", Toast.LENGTH_LONG).show()

                    SaveImageInFirebase()

                }else{
                    Toast.makeText(applicationContext, "Login Failed", Toast.LENGTH_LONG).show()
                }
            }
    }

    fun SaveImageInFirebase(){
        var currentUser = mAuth!!.currentUser

        val storage = FirebaseStorage.getInstance()
        val storageReference = storage.getReferenceFromUrl("gs://twitter-836f9.appspot.com")
        val dateFormat = SimpleDateFormat("ddMMyyHHmmss")
        val imagePath = SplitEmail(currentUser!!.email.toString()) + "." + dateFormat.format(Date()) + ".jpg"
        val imageReference = storageReference.child("images/" + imagePath)

        ivProfile.isDrawingCacheEnabled = true
        ivProfile.buildDrawingCache()

        val drawable = ivProfile.drawable as BitmapDrawable
        val imageBitmap = drawable.bitmap
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, ByteArrayOutputStream())
        val data = ByteArrayOutputStream().toByteArray()
        val uploadTask = imageReference.putBytes(data)
        uploadTask.addOnFailureListener{
            Toast.makeText(applicationContext, "Image Upload Failed", Toast.LENGTH_LONG).show()

        }.addOnSuccessListener { taskSnapshot->

            var downloadURL = imageReference.downloadUrl.toString()
            println("Download Link is: " + downloadURL)
            databaseRefrence.child("Users").child(currentUser.uid).child("email").setValue(currentUser.email)
            databaseRefrence.child("Users").child(currentUser.uid).child("ProfileImage").setValue(downloadURL)

            LoadTweets()
        }
    }

    override fun onStart(){
        super.onStart()
        LoadTweets()
    }

    fun LoadTweets(){
        var currentUser = mAuth!!.currentUser

        if (currentUser != null){
            var intent = Intent(this, MainActivity::class.java)
            intent.putExtra("emai", currentUser.email)
            intent.putExtra("uid", currentUser.uid)

            startActivity(intent)
        }
    }


    fun SplitEmail(email:String): String{
        return email.split("@")[0]
    }


    fun LoginEvent(view: View){
        LoginToFirebase(inputEmail.text.toString(), inputPassword.text.toString())
    }

    val REQUESTCODE = 123
    fun CheckStoragePermission(){

        if(Build.VERSION.SDK_INT >= 23){
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), REQUESTCODE)
                return
            }
        }

        LoadImage()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        when(requestCode){
            REQUESTCODE -> {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    LoadImage()
                }else{
                    Toast.makeText(this, "Can not access storage", Toast.LENGTH_LONG).show()
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }


    val PICK_IMAGE_CODE = 321
    fun LoadImage(){

//        Select image to load
        var intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(intent, PICK_IMAGE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//      get address of selected image from device
        if (requestCode == PICK_IMAGE_CODE && resultCode == Activity.RESULT_OK && data != null){
            val selecteImage = data.data
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor =
                selecteImage?.let { contentResolver.query(it, filePathColumn, null, null, null) }
            cursor!!.moveToFirst()

            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            val picturePath = cursor.getString(columnIndex)
            cursor.close()

            ivProfile.setImageBitmap(BitmapFactory.decodeFile(picturePath))

        }
    }
}
