package com.vikeshpatil.tictactoy

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import com.google.firebase.analytics.FirebaseAnalytics
import kotlin.random.Random
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception


class MainActivity : AppCompatActivity() {

    private var mFirebaseAnalytics:FirebaseAnalytics? = null

    private var database = FirebaseDatabase.getInstance()
    private var databaseRef = database.reference

    var currentUserEmail:String?  = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

        var bundle: Bundle? = intent.extras
        currentUserEmail = bundle!!.getString("email")

        IncomingRequest()
    }


    fun btnClick(view: View){
        val selectedBtn = view as Button
        var btnId = 0

        print(selectedBtn.id)
        when(selectedBtn.id){

            R.id.btn1 -> btnId = 1
            R.id.btn2 -> btnId = 2
            R.id.btn3 -> btnId = 3
            R.id.btn4 -> btnId = 4
            R.id.btn5 -> btnId = 5
            R.id.btn6 -> btnId = 6
            R.id.btn7 -> btnId = 7
            R.id.btn8 -> btnId = 8
            R.id.btn9 -> btnId = 9


        }

//        Toast.makeText(this, "Id: " + btnId, Toast.LENGTH_SHORT).show()

            databaseRef.child("Player Online").child(sessionId!!).child(btnId.toString()).setValue( currentUserEmail)
    }


    var player1Moves = ArrayList<Int>()
    var player2Moves = ArrayList<Int>()
    var playerTurn = 1

    fun PlayGame(cellId: Int, selectedBtn: Button){

        if(playerTurn == 1){
            selectedBtn.text = "X"
            selectedBtn.setBackgroundColor(Color.GREEN)
            player1Moves.add(cellId)
            playerTurn = 2

        }else{
            selectedBtn.text = "O"
            selectedBtn.setBackgroundColor(Color.CYAN)
            player2Moves.add(cellId)
            playerTurn = 1
        }


        selectedBtn.isEnabled = false
        CheckWinner()
    }

    fun CheckWinner(){
        var winner = -1

//        row 1
        if(player1Moves.contains(1) && player1Moves.contains(2) && player1Moves.contains(3)) {
            winner = 1
        }
        if(player2Moves.contains(1) && player2Moves.contains(2) && player2Moves.contains(3)){
            winner = 2
        }

//        Row 2
        if(player1Moves.contains(4) && player1Moves.contains(5) && player1Moves.contains(6)) {
            winner = 1
        }
        if(player2Moves.contains(4) && player2Moves.contains(5) && player2Moves.contains(6)) {
            winner = 2
        }

//        Row3
        if(player1Moves.contains(7) && player1Moves.contains(8) && player1Moves.contains(9)){
            winner = 1
        }
        if(player2Moves.contains(7) && player2Moves.contains(8) && player2Moves.contains(9)){
            winner = 2
        }

//        diagonal 1
        if(player1Moves.contains(1) && player1Moves.contains(5) && player1Moves.contains(9)) {
            winner = 1
        }
        if(player2Moves.contains(1) && player2Moves.contains(5) && player2Moves.contains(9)){
            winner = 2
        }

//      Diagonal 2
        if(player1Moves.contains(3) && player1Moves.contains(5) && player1Moves.contains(7)){
            winner = 1
        }
        if(player2Moves.contains(3) && player2Moves.contains(5) && player2Moves.contains(7)){
            winner = 2
        }

        if (winner != -1){
            if(winner == 1){
                Toast.makeText(this, "Player 1 win the game.", Toast.LENGTH_LONG).show()
                tvWin.text = "Player 1 win the game"
            }else{
                Toast.makeText(this, "Player 2 win the game", Toast.LENGTH_LONG).show()
                tvWin.text = "Player 2 win the game"

            }
        }

    }

    fun AutoPlay(cellId: Int){

        var selectedBtn:Button?
        when(cellId){

            1 -> selectedBtn = btn1
            2 -> selectedBtn = btn2
            3 -> selectedBtn = btn3
            4 -> selectedBtn = btn4
            5 -> selectedBtn = btn5
            6 -> selectedBtn = btn6
            7 -> selectedBtn = btn7
            8 -> selectedBtn = btn8
            9 -> selectedBtn = btn9

            else -> {
                selectedBtn = btn1
            }
        }

        PlayGame(cellId, selectedBtn)
    }

    fun RequestEvent(view: View){
            var email = inputRequestEmail.text.toString()
        databaseRef.child("Users").child(SplitEmail(email)).child("Request").push().setValue(currentUserEmail)

        PlayerOnline(SplitEmail(currentUserEmail!!) + SplitEmail(email))
        playerSymbol = "X"
    }

    fun AcceptEvent(view: View){
        var email = inputRequestEmail.text.toString()
        databaseRef.child("Users").child(SplitEmail(email)).child("Request").push().setValue(currentUserEmail)

        PlayerOnline(SplitEmail(currentUserEmail!!) + SplitEmail(email))
        playerSymbol = "O"
    }

    var sessionId:String? = null
    var playerSymbol:String? = null

    fun PlayerOnline(sessionId: String){
        this.sessionId = sessionId
        databaseRef.child("Player Online").removeValue()
        databaseRef.child("Player Online").child(sessionId!!)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    try {
                        val tableData = dataSnapshot!!.value as HashMap<String, Any>
                        if (tableData != null){

                            player1Moves.clear()
                            player2Moves.clear()

                            var value: String
                            for (key in tableData.keys){
                                value = tableData[key] as String

                                if (value != currentUserEmail){
                                    playerTurn = if (playerSymbol === "X") 1 else 2
                                }else{
                                    playerTurn = if (playerSymbol === "X") 2 else 1
                                }

                                AutoPlay(key.toInt())
                            }
                        }
                    }catch (e: Exception){
                        println("Error in Incoming Request: " + e.message)
                    }
                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })
    }

    var number = 0
    fun IncomingRequest(){
        databaseRef.child("Users").child(SplitEmail(currentUserEmail!!)).child("Request")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    try {
                        val tableData = dataSnapshot!!.value as HashMap<String, Any>
                        if (tableData != null){

                            var value: String
                            for (key in tableData.keys){
                                value = tableData[key] as String
                                inputRequestEmail.setText(value)

                                val notifyMe = Notification()
                                notifyMe.Notify(applicationContext, value + " wants to play tic tac toy", number)
                                number++
                                databaseRef.child("Users").child(SplitEmail(currentUserEmail!!)).child("Request").setValue(true)

                                break
                            }
                        }
                    }catch (e: Exception){
                        println("Error in Incoming Request: " + e.message)
                    }
                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })
    }

    fun SplitEmail(str: String): String{
        return str.split("@")[0]
    }

}
