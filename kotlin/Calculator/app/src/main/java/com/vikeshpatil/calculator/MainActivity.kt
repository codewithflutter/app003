package com.vikeshpatil.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun NumberClickEvent(view: View){

        if(isNewOperation){
            eTInput.setText("")
        }
        isNewOperation = false
        val selectedButton = view as Button
        var btnValue = eTInput.text.toString()

        when(selectedButton.id){
            number0.id -> {
                btnValue += "0"
            }

            number1.id -> {
                btnValue += "1"
            }
            number2.id -> {
                btnValue += "2"
            }
            number3.id -> {
                btnValue += "3"
            }
            number4.id -> {
                btnValue += "4"
            }
            number5.id -> {
                btnValue += "5"
            }
            number6.id -> {
                btnValue += "6"
            }
            number7.id -> {
                btnValue += "7"
            }
            number8.id -> {
                btnValue += "8"
            }
            number9.id -> {
                btnValue += "9"
            }
            numberDot.id -> {
                if (!btnValue.contains('.'))
                        btnValue +="."
            }
            btnPlusMinus.id-> {
                if (btnValue[0] != '-'){
                    btnValue = "-" + btnValue
                }else{
                    btnValue = btnValue.substring(1, btnValue.length)
                }
            }
        }
        eTInput.setText(btnValue)
    }


    var operation:String = ""
    var oldNumber = ""
    var isNewOperation = true

    fun arithOperation(view: View) {

        val selectedButton = view as Button

        when (selectedButton.id) {
            btnDiv.id -> {
                operation = "/"
            }
            btnMul.id -> {
                operation = "*"
            }
            btnSub.id -> {
                operation = "-"
            }
            btnAdd.id -> {
                operation = "+"
            }
        }
        oldNumber = eTInput.text.toString()
        isNewOperation = true
    }

    fun equalClick(view: View){

        val newNumber = eTInput.text.toString()
        var result:Double? = null
        when(operation){
            "*"-> {
                result = oldNumber.toDouble() * newNumber.toDouble()
            }
            "/"-> {
                result = oldNumber.toDouble() / newNumber.toDouble()

            }
            "-"-> {
                result = oldNumber.toDouble() - newNumber.toDouble()

            }
            "+"-> {
                result = oldNumber.toDouble() + newNumber.toDouble()

            }
        }
        eTInput.setText(result.toString())
        isNewOperation = true
    }

    fun AllClearClick(view: View){

        val selectedbtn = view as Button

        if (selectedbtn.id == btnClear.id){
                operation = ""
                oldNumber = ""
                eTInput.setText("")
                isNewOperation = true
        }
    }

    fun percentClick(view: View){
        val result = eTInput.text.toString().toDouble() / 100

        eTInput.setText(result.toString())
        isNewOperation = true
    }
}
