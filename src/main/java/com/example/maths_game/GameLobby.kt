package com.example.maths_game

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.PersistableBundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

class GameLobby : AppCompatActivity() {

    private val random: Random = Random()
    private val operatorArr = arrayOf("+", "-", "*", "/")
    private var leftVal: Double = 0.0
    private var rightVal: Double = 0.0
    private var leftFinalVal:Double =0.0
    private var rightFinalVal:Double =0.0
    private var answer:String =""
    private var correctAnswersCount:Int = 0
    private var wrongAnswersCount:Int = 0
    private var allQuesCount:Int = 0
    private var addExtraSeconds:Long = 10000
    private var millisInFuture:Long = 50000
    private var currentTime:Long =0
    private var btnClickedCount:Int = 0
    private lateinit var timerTwoCommand:CountDownTimer
    private lateinit var timerOneCommand:CountDownTimer




    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_lobby)

        val gameLobbyBackBtn = findViewById<Button>(R.id.gameLobbyBackBtn)
        val leftText = findViewById<TextView>(R.id.leftTextView)
        val rightText = findViewById<TextView>(R.id.rightTextView)
//        val leftValue = findViewById<TextView>(R.id.l)
//        val rightValue = findViewById<TextView>(R.id.r)
        val greater = findViewById<Button>(R.id.greater)
        val equal = findViewById<Button>(R.id.equal)
        val less = findViewById<Button>(R.id.less)
        val timerTextView = findViewById<TextView>(R.id.timer_view)
        val redToast = findViewById<LinearLayout>(R.id.red_toast)
        val greenToast = findViewById<LinearLayout>(R.id.green_toast)
        val redLayout =layoutInflater.inflate(R.layout.red_color_toast,redToast)
        val greenLayout =layoutInflater.inflate(R.layout.green_color_toast,greenToast)


        val correctToast:Toast = Toast(this).apply {
            duration =Toast.LENGTH_SHORT
            setGravity(Gravity.TOP,0,0)
            view = greenLayout
        }
        val wrongToast:Toast = Toast(this).apply   {
            duration =Toast.LENGTH_SHORT
            setGravity(Gravity.TOP,0,0)
            view = redLayout
        }

        val randomExpression = {
            leftText.text = expression("left") //generate random expression on the left side
            rightText.text = expression("right")//generate random expression on the right side

            leftFinalVal = ((leftVal * 1000.0).roundToInt() /1000.0)
            rightFinalVal = ((rightVal * 1000.0).roundToInt() /1000.0)

//            leftValue.text = leftFinalVal.toString()/
//            rightValue.text = rightFinalVal.toString()

            if(leftFinalVal == rightFinalVal){
                answer = "equal"
            }else if(leftFinalVal > rightFinalVal){
                answer = "greater"
            }else if(leftFinalVal < rightFinalVal){
                answer = "less"
            }
        }

        randomExpression()//calling random expression

        val timer  = countDownTimer(timerTextView,millisInFuture)
        timer.start()
        timerOneCommand = timer

        gameLobbyBackBtn.setOnClickListener{
            val backToHomePage = Intent(this,MainActivity:: class.java)
            startActivity(backToHomePage)
            setFinalResult()
        }

        greater.setOnClickListener{// checking the number greater or not

            allQuesCount++

            when(answer){
                "greater"->{
                    correctToast.show()
                    correctAnswersCount++
                }
                else->{
                    wrongToast.show()
                    wrongAnswersCount++
                }
            }
            Handler().postDelayed({
                    randomExpression()
                },3000)

        }
        equal.setOnClickListener{// checking the number equal or not


            allQuesCount++
            when(answer){
                "equal"->{
                    correctToast.show()
                    correctAnswersCount++
                }
                else->{
                    wrongToast.show()
                    wrongAnswersCount++
                }
            }
            Handler().postDelayed({
                randomExpression()
            },3000)
        }
        less.setOnClickListener{ // checking the number less or not


            allQuesCount++
            when(answer){
                "less"->{
                    correctToast.show()
                    correctAnswersCount++
                }
                else->{
                    wrongToast.show()
                    wrongAnswersCount++
                }
            }
            Handler().postDelayed({
                randomExpression()
            },3000)
        }


    }


    private fun addExtraTime(timerTextView:TextView){ // adding the extra time

        timerOneCommand.cancel()
        val timerTwo = countDownTimer(timerTextView, currentTime + addExtraSeconds)

        if (correctAnswersCount == 5){
            timerTwoCommand.cancel()
            correctAnswersCount = 0
        }

        timerTwo.start()
        timerTwoCommand = timerTwo

    }

    private fun countDownTimer(timerTextView : TextView,millisInFuture : Long) :CountDownTimer{ // count down timer function
        val timer = object: CountDownTimer(millisInFuture,1000){
            override fun onTick(time: Long) {
                val sDuration :String = String.format(Locale.ENGLISH,"%02d : %02d",TimeUnit.MILLISECONDS.toMinutes(time),TimeUnit.MILLISECONDS.toSeconds(time)- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)))
                currentTime = time
                timerTextView.text = sDuration.toString()
            }
            override fun onFinish() {
                setFinalResult()
            }
        }
        return timer
    }

    private fun setFinalResult(){
        val resultsPageIntent = Intent(this,Results::class.java)
        resultsPageIntent.putExtra("allQuesCount",allQuesCount.toString())
        resultsPageIntent.putExtra("correctAns",(allQuesCount - wrongAnswersCount).toString())
        startActivity(resultsPageIntent)
    }


    private fun randomNumbersArrGenerator(): MutableList<Int> {
        val ranSize = 2 + random.nextInt(4)
        val numberArr = mutableListOf<Int>()
        var newRanNum = 1 + random.nextInt(20)

        while (numberArr.size < ranSize) {
            if (newRanNum !in numberArr)
                numberArr.add(newRanNum)
            else
                newRanNum = 1 + random.nextInt(20)
        }
        return numberArr
    }

    private fun expression(flag: String): String {

        val numArray = randomNumbersArrGenerator()
        val lengthOfNumArr = numArray.size
        val operator1 = getOperator()
        val operator2 = getOperator()
        val operator3 = getOperator()
        val operator4 = getOperator()
        var expressionModel = ""


        when (lengthOfNumArr) {
            2 -> {
                expressionModel = "(${numArray[0]}" + operator1 + "${numArray[1]}" + ")"
                when(flag){
                    "left"-> leftVal = getCalculateExpressionOne(numArray[0], operator1, numArray[1])
                    "right"-> rightVal = getCalculateExpressionOne(numArray[0], operator1, numArray[1])
                }
            }
            3 -> {
                expressionModel =
                    "(" + "(${numArray[0]}" + operator1 + "${numArray[1]}" + ")" + operator2 + "${numArray[2]}" + ")"
                when(flag){
                    "left"-> leftVal = getCalculateExpressionTwo(numArray[0], operator1, numArray[1],operator2,numArray[2])
                    "right"-> rightVal = getCalculateExpressionTwo(numArray[0], operator1, numArray[1],operator2,numArray[2])
                }
            }
            4 -> {
                expressionModel =
                    "(" + "(${numArray[0]}" + operator1 + "${numArray[1]}" + ")" + operator2 + "${numArray[2]}" + ")" + operator3 + "${numArray[3]}" + ")"
                when(flag){
                    "left"-> leftVal = getCalculateExpressionThree(numArray[0], operator1, numArray[1],operator2,numArray[2],operator3,numArray[3])
                    "right"-> rightVal = getCalculateExpressionThree(numArray[0], operator1, numArray[1],operator2,numArray[2],operator3,numArray[3])
                }
            }
            5 -> {
                expressionModel =
                    "(" + "(${numArray[0]}" + operator1 + "${numArray[1]}" + ")" + operator2 + "${numArray[2]}" + ")" + operator3 + "${numArray[3]}" + ")" + operator4 + "${numArray[4]}" + ")"
                when(flag){
                    "left"-> leftVal = getCalculateExpressionFour(numArray[0], operator1, numArray[1],operator2,numArray[2],operator3,numArray[3],operator4,numArray[4])
                    "right"-> rightVal = getCalculateExpressionFour(numArray[0], operator1, numArray[1],operator2,numArray[2],operator3,numArray[3],operator4,numArray[4])
                }
            }
        }
        return expressionModel
    }
    private fun getOperator(): String {
        val generateRandomNum = 0 + random.nextInt(4)
        return operatorArr[generateRandomNum]
    }

    private fun getCalculateExpressionOne(num1: Int, op1: String, num2: Int): Double {

        var value: Double = 0.0
        val n1: Double = num1.toDouble()
        val n2: Double = num2.toDouble()

        when (op1) {
            "+" -> value = n1 + n2
            "-" -> value = n1 - n2
            "*" -> value = n1 * n2
            "/" -> value = n1 / n2
        }
        return value
    }

    private fun getCalculateExpressionTwo(
        num1: Int,
        op1: String,
        num2: Int,
        op2: String,
        num3: Int
    ): Double {

        var value: Double = 0.0
        val n3: Double = num3.toDouble()

        when (op2) {
            "+" -> value = getCalculateExpressionOne(num1, op1, num2) + n3
            "-" -> value = getCalculateExpressionOne(num1, op1, num2) - n3
            "*" -> value = getCalculateExpressionOne(num1, op1, num2) * n3
            "/" -> value = getCalculateExpressionOne(num1, op1, num2) / n3
        }

        return value
    }

    private fun getCalculateExpressionThree(
        num1: Int,
        op1: String,
        num2: Int,
        op2: String,
        num3: Int,
        op3: String,
        num4: Int
    ): Double {
        var value: Double = 0.0
        val n4: Double = num4.toDouble()

        when (op3) {
            "+" -> value = getCalculateExpressionTwo(num1, op1, num2, op2, num3) + n4
            "-" -> value = getCalculateExpressionTwo(num1, op1, num2, op2, num3) - n4
            "*" -> value = getCalculateExpressionTwo(num1, op1, num2, op2, num3) * n4
            "/" -> value = getCalculateExpressionTwo(num1, op1, num2, op2, num3) / n4
        }

        return value
    }

    private fun getCalculateExpressionFour(
        num1: Int,
        op1: String,
        num2: Int,
        op2: String,
        num3: Int,
        op3: String,
        num4: Int,
        op4: String,
        num5: Int
    ): Double {
        var value: Double = 0.0
        val n5: Double = num5.toDouble()

        when (op4) {
            "+" -> value = getCalculateExpressionThree(num1, op1, num2, op2, num3, op3, num4) + n5
            "-" -> value = getCalculateExpressionThree(num1, op1, num2, op2, num3, op3, num4) - n5
            "*" -> value = getCalculateExpressionThree(num1, op1, num2, op2, num3, op3, num4) * n5
            "/" -> value = getCalculateExpressionThree(num1, op1, num2, op2, num3, op3, num4) / n5
        }

        return value
    }
}