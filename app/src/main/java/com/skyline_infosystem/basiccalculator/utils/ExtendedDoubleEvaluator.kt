package com.skyline_infosystem.basiccalculator.utils

import com.fathzer.soft.javaluator.DoubleEvaluator
import com.fathzer.soft.javaluator.Function
import com.fathzer.soft.javaluator.Parameters
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.atan
import kotlin.math.sqrt

class ExtendedDoubleEvaluator : DoubleEvaluator(PARAMS) {

    public override fun evaluate(
        function: Function,
        arguments: Iterator<Double>?,
        evaluationContext: Any?
    ): Double? {
        return if (function === SQRT) {
            // Implements the new function
            sqrt(arguments!!.next())
        } else if (function === CBRT) {
            Math.cbrt(arguments!!.next())
        } else if (function === ASIND) {
            Math.toDegrees(asin(arguments!!.next()))
        } else if (function === ACOSD) {
            Math.toDegrees(acos(arguments!!.next()))
        } else if (function === ATAND) {
            Math.toDegrees(atan(arguments!!.next()))
        } else {
            // If it's another function, pass it to DoubleEvaluator
            super.evaluate(function, arguments, evaluationContext)
        }
    }

    companion object {
        /** Defines the new function (square root). */
        private val SQRT = Function("sqrt", 1)
        private val CBRT = Function("cbrt", 1)
        private val ASIND = Function("asind", 1)
        private val ACOSD = Function("acosd", 1)
        private val ATAND = Function("atand", 1)
        private val PARAMS: Parameters = getDefaultParameters()

        init {
            // Gets the default DoubleEvaluator's parameters
            // add the new sqrt function to these parameters
            PARAMS.add(SQRT)
            PARAMS.add(CBRT)
            PARAMS.add(ASIND)
            PARAMS.add(ACOSD)
            PARAMS.add(ATAND)
        }
    }
}
