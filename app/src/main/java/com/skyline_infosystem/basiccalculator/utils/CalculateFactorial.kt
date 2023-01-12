package com.skyline_infosystem.basiccalculator.utils
class CalculateFactorial internal constructor(){
    private val max = 1000

    private var resSize: Int = 0
    private val res = IntArray(max)

    init {
        resSize=1
    }

    fun getRes(): Int {
        return resSize
    }

    fun factorial(n: Int): IntArray {
        // Initialize result
        res[0] = 1

        // Apply simple factorial formula n! = 1 * 2 * 3 * 4...*n
        for (x in 2..n)
            resSize = multiply(x, resSize)

        return res
    }
    private fun multiply(x: Int, r: Int): Int {
        var _r = r
        var carry = 0  // Initialize carry

        // One by one multiply n with individual digits of res[]
        for (i in 0 until _r) {
            val prod = res[i] * x + carry
            res[i] = prod % 10  // Store last digit of 'prod' in res[]
            carry = prod / 10    // Put rest in carry
        }

        // Put carry in res and increase result size
        while (carry != 0) {
            res[_r] = carry % 10
            carry /= 10
            _r++
        }
        return _r
    }
}