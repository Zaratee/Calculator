package com.example.myapplication

import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.HorizontalScrollView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    companion object {
        var calculatorInput = "0"
        var calculatorInputCompleteFormula = ""
        var resultString = ""
        var numberstArray = ArrayList<Int>()
        var operationArray = ""
        var resultArrayIndex = 0
        var hasOperation = false

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val index = 0
        setContentView(R.layout.activity_main)
    }

    fun calculatorInput(view: View){
        hsvFormula.postDelayed(Runnable { hsvFormula.fullScroll(HorizontalScrollView.FOCUS_RIGHT) }, 100L)
        hsvResult.postDelayed(Runnable { hsvResult.fullScroll(HorizontalScrollView.FOCUS_RIGHT) }, 100L)

        val button: Button = view as Button
        val buttonText: String = button.getText().toString()
        if (buttonText == "CLR") {
            clrDisplay()
            return
        }

        if (buttonText?.toIntOrNull() !== null){
            if (calculatorInput.equals("")  && buttonText.equals("0") ) {
                calculatorInput = calculatorInput + buttonText
                tvDisplayResult.setText(calculatorInput)
            }else if(calculatorInput.equals("0")  && !buttonText.equals("0")){
                calculatorInput = ""
                calculatorInput = calculatorInput + buttonText
                tvDisplayResult.setText(calculatorInput)
            }else if(!calculatorInput.equals("0")  && !buttonText.equals("0")){
                calculatorInput = calculatorInput + buttonText
                tvDisplayResult.setText(calculatorInput)
            }else if(!calculatorInput.equals("0")  && buttonText.equals("0")){
                calculatorInput = calculatorInput + buttonText
                tvDisplayResult.setText(calculatorInput)
            }
        }else  {
            if (buttonText == "="){
                createFormulaToCalculate(buttonText)
            }
            else{
                hasOperation = true;
                createFormulaToCalculate(buttonText)
            }
        }
    }

    fun createFormulaToCalculate(buttonText : String){
        if (calculatorInput == ""){
            Toast.makeText(this, "Error: You should enter a number first", Toast.LENGTH_SHORT).show()
            return;
        }
        if (!hasOperation){
            Toast.makeText(this, "Error: You should enter a operation first", Toast.LENGTH_SHORT).show()
            return;
        }
        val numbersMap = mapOf("X" to ::multiply, "+" to ::add, "-" to ::substract, "=" to ::equal,"/" to ::division)
        numbersMap[buttonText]?.invoke()
    }
    fun multiply(){
        calculatorInputCompleteFormula = calculatorInputCompleteFormula + calculatorInput + "X"
        setCalculatorDefault()
    }
    fun substract(){
        calculatorInputCompleteFormula = calculatorInputCompleteFormula + calculatorInput + "-"
        setCalculatorDefault()
    }
    fun equal(){
        calculatorInputCompleteFormula = calculatorInputCompleteFormula + calculatorInput
        tvDisplayFormula.setText(calculatorInputCompleteFormula)
        operationResult(calculatorInputCompleteFormula)
        lyAllButtons.setVisibility(View.GONE)
        lyNextOperation.setVisibility(View.VISIBLE)
    }
    fun add(){
        calculatorInputCompleteFormula = calculatorInputCompleteFormula + calculatorInput + "+"
        setCalculatorDefault()

    }
    fun division(){
        calculatorInputCompleteFormula = calculatorInputCompleteFormula + calculatorInput + "/"
        setCalculatorDefault()

    }
    fun setCalculatorDefault(){
        tvDisplayFormula.setText(calculatorInputCompleteFormula)
        calculatorInput = ""
    }
    fun clrDisplay (){
        calculatorInput = "0"
        calculatorInputCompleteFormula = ""
        tvDisplayResult.setText(calculatorInput)
        tvDisplayFormula.setText("")
        resultString = ""
        numberstArray = ArrayList<Int>()
        operationArray = ""
        resultArrayIndex = 0
        hasOperation = false
    }

    fun operationResult( formula : String){
        var formmula = formula + "."
        formmula.forEachIndexed { i,char -> spliceFormula(i,char) }

        calculateResult()
    }
    fun spliceFormula(index: Int, char : Char){

        if (char.isDigit()){
            resultString = resultString + char
        }else{
            numberstArray.add(resultString.toInt())
            operationArray = operationArray + char
            resultString = ""
            resultArrayIndex =+ 1
        }
    }

    fun calculateResult(){
        operationArray.dropLast(operationArray.length)
        if (numberstArray.size <= 2){
            if (operationArray[0] == '/' && numberstArray[1] == 0){
                tvDisplayResult.setText("Infinity")
                return
            }else{
                tvDisplayResult.setText(""+operatorFromChar(operationArray[0]).invoke(numberstArray[0], numberstArray[1]))
            }
        }else{
            var result = 0
            numberstArray.forEachIndexed{i,number ->
                if (i == 0){
                    result = number
                }else{
                    if (operationArray[i-1] == '/' && number == 0){
                        tvDisplayResult.setText("Infinity")
                        return
                    }else{
                        result = operatorFromChar(operationArray[i-1]).invoke(result, number)
                    }
                }

            }
            tvDisplayResult.setText(""+result)
        }
    }

    fun operatorFromChar(charOperator: Char):(Int, Int)->Int {
        return when(charOperator)
        {
            '+'->{a,b->a+b}
            '-'->{a,b->a-b}
            '/'->{a,b->a/b}
            'X'->{a,b->a*b}
            else -> throw Exception("That's not a supported operator")
        }
    }

    fun nextOperation(view: View){
        clrDisplay()
        lyAllButtons.setVisibility(View.VISIBLE)
        lyNextOperation.setVisibility(View.GONE)
    }



}