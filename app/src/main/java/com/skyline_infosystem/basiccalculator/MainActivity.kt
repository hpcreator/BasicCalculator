package com.skyline_infosystem.basiccalculator

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.skyline_infosystem.basiccalculator.databinding.ActivityMainBinding
import com.skyline_infosystem.basiccalculator.utils.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var count = 0
    private var expression = ""
    private var text = ""
    private var result: Double? = 0.0
    private var back: Boolean = false

    //private var dbHelper: DBHelper? = null
    private var toggleMode: Int = 1
    private var angleMode = 1

    private var keepSplashOnScreen = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition { keepSplashOnScreen }
        addDelay(2000) {
            keepSplashOnScreen = false
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        handleButtonClicks()
    }

    private fun handleButtonClicks() {
        binding.apply {
            btnZero.setSafeClickListener { tvResult.setText(tvResult.getTrimmedText().plus("0")) }
            btnOne.setSafeClickListener { tvResult.setText(tvResult.getTrimmedText().plus("1")) }
            btnTwo.setSafeClickListener { tvResult.setText(tvResult.getTrimmedText().plus("2")) }
            btnThree.setSafeClickListener { tvResult.setText(tvResult.getTrimmedText().plus("3")) }
            btnFour.setSafeClickListener { tvResult.setText(tvResult.getTrimmedText().plus("4")) }
            btnFive.setSafeClickListener { tvResult.setText(tvResult.getTrimmedText().plus("5")) }
            btnSix.setSafeClickListener { tvResult.setText(tvResult.getTrimmedText().plus("6")) }
            btnSeven.setSafeClickListener { tvResult.setText(tvResult.getTrimmedText().plus("7")) }
            btnEight.setSafeClickListener { tvResult.setText(tvResult.getTrimmedText().plus("8")) }
            btnNine.setSafeClickListener { tvResult.setText(tvResult.getTrimmedText().plus("9")) }
            btnDot.setSafeClickListener {
                tvResult.setText(tvResult.getTrimmedText().plus("."))
                count++
            }

            btnClear.setSafeClickListener {
                tvExpression.setText("")
                tvResult.setText("")
                count = 0
                expression = ""
            }

            btnBack.setSafeClickListener {
                text = tvResult.text.toString()
                if (text.isNotEmpty()) {
                    if (text.endsWith(".")) {
                        count = 0
                    }
                    var newText = text.substring(0, text.length - 1)

                    if (text.endsWith(")")) {
                        val a = text.toCharArray()
                        var pos = a.size - 2
                        var counter = 1

                        for (i in a.size - 2 downTo 0) {
                            when {
                                a[i] == ')' -> counter++
                                a[i] == '(' -> counter--
                                a[i] == '.' -> count = 0
                            }
                            if (counter == 0) {
                                pos = i
                                break
                            }
                        }
                        newText = text.substring(0, pos)
                    }

                    when {
                        newText == "-" || newText.endsWith("sqrt") || newText.endsWith("log") ||
                                newText.endsWith("ln") || newText.endsWith("sin") || newText.endsWith("asin") ||
                                newText.endsWith("asind") || newText.endsWith("sinh") || newText.endsWith("cos") ||
                                newText.endsWith("acos") || newText.endsWith("acosd") || newText.endsWith("cosh") ||
                                newText.endsWith("tan") || newText.endsWith("atan") || newText.endsWith("atand") ||
                                newText.endsWith("tanh") || newText.endsWith("cbrt") -> {
                            newText = ""
                        }
                        newText.endsWith("^") || newText.endsWith("/") -> newText = newText.substring(0, newText.length - 1)
                        newText.endsWith("pi") || newText.endsWith("e^") -> newText = newText.substring(
                            0,
                            newText.length - 2
                        )
                    }
                    tvResult.setText(newText)
                }
            }

            btnPlus.setSafeClickListener { operationClicked("+") }

            btnMinus.setSafeClickListener { operationClicked("-") }

            btnDivide.setSafeClickListener { operationClicked("/") }

            btnMultiply.setSafeClickListener { operationClicked("*") }

            btnSqrt.setSafeClickListener {
                text = tvResult.text.toString()
                toggleMode = btnSwitch.tag as Int
                when (toggleMode) {
                    1 -> tvResult.setText(getString(R.string.valueSqrt, text))
                    2 -> tvResult.setText(getString(R.string.valueCbrt, text))
                    else -> tvResult.setText(getString(R.string.valueOnesUpon, text))
                }
            }

            btnSquare.setSafeClickListener {
                if (tvResult.length() != 0) {
                    text = tvResult.text.toString()
                    if (toggleMode == 2)
                        tvResult.setText(getString(R.string.valuePowerCube, text))
                    else
                        tvResult.setText(getString(R.string.valuePowerSquare, text))
                }
            }

            btnNegative.setSafeClickListener {
                val s = tvResult.text.toString()
                val arr = s.toCharArray()
                if (arr[0] == '-')
                    tvResult.setText(s.substring(1, s.length))
                else
                    tvResult.setText("-".plus(s))
            }

            btnEquals.setSafeClickListener {
                if (tvResult.length() != 0) {
                    text = tvResult.text.toString()
                    expression = tvExpression.text.toString() + text
                }
                tvExpression.setText("")
                if (expression.isEmpty()) expression = "0.0"
                try { //evaluate the expression
                    result = ExtendedDoubleEvaluator().evaluate(expression)
                    //insert expression and result in sqlite database if expression is valid and not 0.0
                    if (result.toString() == "6.123233995736766E-17") {
                        result = 0.0
                        tvResult.setText(result.toString())
                    } else if (result.toString() == "1.633123935319537E16") {
                        tvResult.setText(getString(R.string.infinity))
                    } else tvResult.setText(result.toString())
                    //if (expression != "0.0") dbHelper!!.insert("SCIENTIFIC", "$expression = $result")
                } catch (e: java.lang.Exception) {
                    tvResult.setText(getString(R.string.invalidExpression))
                    tvExpression.setText("")
                    expression = ""
                    e.printStackTrace()
                }
            }

            btnOpen.setSafeClickListener { tvExpression.setText(tvExpression.getTrimmedText().plus(getString(R.string.open))) }

            btnClose.setSafeClickListener { tvExpression.setText(tvExpression.getTrimmedText().plus(getString(R.string.close))) }

            btnSwitch.setSafeClickListener {
                toggleMode = if (btnSwitch.tag != null) btnSwitch.tag as Int else 1
                when (toggleMode) {
                    1 -> {
                        btnSwitch.tag = 2
                        toggleMode = btnSwitch.tag as Int
                        btnSquare.setText(R.string.cube)
                        btnPower.setText(R.string.tenPOV)
                        btnLog.setText(R.string.naturalLog)
                        btnSin.setText(R.string.sin_Inv)
                        btnCos.setText(R.string.cos_Inv)
                        btnTan.setText(R.string.tan_Inv)
                        btnSqrt.setText(R.string.cube_Root)
                        btnFact.setText(R.string.Mod)
                    }
                    2 -> {
                        btnSwitch.tag = 3
                        toggleMode = btnSwitch.tag as Int
                        btnSquare.setText(R.string.square)
                        btnPower.setText(R.string.e_POV)
                        btnLog.setText(R.string.log)
                        btnSin.setText(R.string.hyperbolicSine)
                        btnCos.setText(R.string.hyperbolicCosine)
                        btnTan.setText(R.string.hyperbolicTan)
                        btnSqrt.setText(R.string.inverse)
                        btnFact.setText(R.string.factorial)
                    }
                    3 -> {
                        btnSwitch.tag = 1
                        toggleMode = btnSwitch.tag as Int
                        btnSin.setText(R.string.sin)
                        btnCos.setText(R.string.cos)
                        btnTan.setText(R.string.tan)
                        btnSqrt.setText(R.string.sqrt)
                        btnPower.setText(R.string.xPOV)
                    }
                }
            }

            btnDegree.setSafeClickListener {
                if (angleMode == 1) {
                    btnDegree.tag = 2
                    angleMode = btnDegree.tag as Int
                    btnDegree.setText(R.string.mode2)
                } else {
                    btnDegree.tag = 1
                    angleMode = btnDegree.tag as Int
                    btnDegree.setText(R.string.mode1)
                }
            }

            btnLog.setSafeClickListener {
                if (tvResult.length() != 0) {
                    text = tvResult.text.toString()
                    if (toggleMode == 2)
                        tvResult.setText(getString(R.string.valueLn, text))
                    else
                        tvResult.setText(getString(R.string.valueLog, text))
                }
            }

            btnPower.setSafeClickListener {
                if (tvResult.length() != 0) {
                    text = tvResult.text.toString()
                    when (toggleMode) {
                        1 -> tvResult.setText(getString(R.string.n_power, text))
                        2 -> tvResult.setText(getString(R.string.ten_power, text))
                        else -> tvResult.setText(getString(R.string.e_power, text))
                    }
                }
            }

            btnSin.setSafeClickListener {
                if (tvResult.length() != 0) {
                    text = tvResult.text.toString()
                    if (angleMode == 1) {
                        val angle = Math.toRadians(ExtendedDoubleEvaluator().evaluate(text))
                        when (toggleMode) {
                            1 -> tvResult.setText(getString(R.string.valueSin, angle.toString()))
                            2 -> tvResult.setText(getString(R.string.valueASinD, text))
                            else -> tvResult.setText(getString(R.string.valueSinH, text))
                        }
                    } else {
                        when (toggleMode) {
                            1 -> tvResult.setText(getString(R.string.valueSin, text))
                            2 -> tvResult.setText(getString(R.string.valueASin, text))
                            else -> tvResult.setText(getString(R.string.valueSinH, text))
                        }
                    }
                }
            }

            btnCos.setSafeClickListener {
                if (tvResult.length() != 0) {
                    text = tvResult.text.toString()
                    if (angleMode == 1) {
                        val angle = Math.toRadians(ExtendedDoubleEvaluator().evaluate(text))
                        when (toggleMode) {
                            1 -> tvResult.setText(getString(R.string.valueCos, angle.toString()))
                            2 -> tvResult.setText(getString(R.string.valueACosD, text))
                            else -> tvResult.setText(getString(R.string.valueCosH, text))
                        }
                    } else {
                        when (toggleMode) {
                            1 -> tvResult.setText(getString(R.string.valueCos, text))
                            2 -> tvResult.setText(getString(R.string.valueACos, text))
                            else -> tvResult.setText(getString(R.string.valueCosH, text))
                        }
                    }
                }
            }

            btnTan.setSafeClickListener {
                if (tvResult.length() != 0) {
                    text = tvResult.text.toString()
                    if (angleMode == 1) {
                        val angle = Math.toRadians(ExtendedDoubleEvaluator().evaluate(text))
                        when (toggleMode) {
                            1 -> tvResult.setText(getString(R.string.valueTan, angle.toString()))
                            2 -> tvResult.setText(getString(R.string.valueATanD, text))
                            else -> tvResult.setText(getString(R.string.valueTanH, text))
                        }
                    } else {
                        when (toggleMode) {
                            1 -> tvResult.setText(getString(R.string.valueTan, text))
                            2 -> tvResult.setText(getString(R.string.valueATan, text))
                            else -> tvResult.setText(getString(R.string.valueTanH, text))
                        }
                    }
                }
            }

            btnPi.setSafeClickListener { tvResult.setText(tvResult.getTrimmedText().plus("pi")) }

            btnFact.setSafeClickListener {
                if (tvResult.length() != 0) {
                    text = tvResult.text.toString()
                    if (toggleMode == 2) {
                        tvExpression.setText("(".plus(text).plus(")%"))
                        tvResult.setText("")
                    } else {
                        var res = ""
                        try {
                            val cf = CalculateFactorial()
                            val arr = cf.factorial(
                                java.lang.String.valueOf(
                                    ExtendedDoubleEvaluator().evaluate(text)
                                ).toDouble().toInt()
                            )
                            val resSize = cf.getRes()
                            if (resSize > 20) {
                                for (i in resSize - 1 downTo resSize - 20) {
                                    if (i == resSize - 2) res += "."
                                    res += arr[i]
                                }
                                res += "E" + (resSize - 1)
                            } else {
                                for (i in resSize - 1 downTo 0) {
                                    res += arr[i]
                                }
                            }
                            tvResult.setText(res)
                        } catch (e: Exception) {
                            if (e.toString().contains("ArrayIndexOutOfBoundsException")) {
                                tvResult.setText(getString(R.string.tooBigResult))
                            } else tvResult.setText(getString(R.string.invalid))
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_history -> {
                startActivity(Intent(this, HistoryActivity::class.java))
            }
        }
        return true
    }

    private fun operationClicked(op: String) {
        if (binding.tvResult.length() != 0) {
            val text = binding.tvResult.getTrimmedText()

            binding.tvExpression.setText(binding.tvExpression.getTrimmedText().plus(text).plus(op))
            binding.tvResult.setText("")
            count = 0
        } else {
            val text = binding.tvExpression.getTrimmedText()
            if (text.isNotEmpty()) {
                val newText = text.substring(0, text.length - 1) + op
                binding.tvExpression.setText(newText)
            }
        }
    }

    private val onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (back) {
                finishAffinity()
            } else {
                back = true
                showToast("Press again for exit..")
                addDelay(2000) {
                    back = false
                }
            }
        }
    }
}