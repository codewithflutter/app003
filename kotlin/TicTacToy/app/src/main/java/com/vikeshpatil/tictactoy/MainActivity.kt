package com.vikeshpatil.tictactoy

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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

        PlayGame(btnId, selectedBtn)
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
            AutoPlay()
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

    fun AutoPlay(){

        var emptyCells = ArrayList<Int>()

        for (cellId in 1..9){
            if (!(player1Moves.contains(cellId) || player2Moves.contains(cellId))){
                emptyCells.add(cellId)
            }
        }

//        val r = java.util.Random()
        val cellId = emptyCells.random()
//        val cellId = emptyCells[randomVal]

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
}
