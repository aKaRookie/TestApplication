package com.example.mykotlin


import org.junit.Test

import org.junit.Assert.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.logging.SimpleFormatter
import kotlin.collections.ArrayList

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun test() {
        val x = 10
        var a: Int? = 0
        if (x > 0) {
            println("$x sss")
        }
    }

    @Test
    fun testNull() {
        var name: String? = "fitkit"
        val len = if (name != null) name.length else -1
        println("name的长度: $len")
        println(name?.length?.toString())
        name = null
        if (name != null && name.length > 0) {
            println("name: $name")
        } else {
            println("空字符串！")
        }
    }

    @Test
    fun testLet() {
        val myArr: Array<String?> = arrayOf("fkit", "fkjava", null, "crazyit")
        myArr.forEach { item -> item?.let { println("array: $item") } }

        for (item in myArr) {
            item?.let { println(item) }
        }
    }

    @Test
    fun testElvis() {
        var name: String? = "fkit"
        val b = name?.length ?: -1
        println("boolean: $b")
    }

    @Test
    fun testNPE() {
        val name: String? = "fkit"
        println(name!!.length)

        for (c in name.toCharArray()) {
            c.let {
                println("char: $c")
                println("char: $c")
            }
        }
    }

    @Test
    fun testArray() {
        val array = "fkjava.org"
        println(array[2])
        for (c in array) {
            print(c)
        }
        println()

        var str = "fkjava.org"
        println(str.length)

        val txt = """
            |天上白玉京
            |十二楼五城
            |现任福我顶
            |结发受长生""".trimMargin()
        println(txt)
    }

    @Test
    fun testTime() {
        var t1 = 1573974000000
        var t2 = System.currentTimeMillis()
        println("time long: t1=$t1,t2=$t2")
        var time = SimpleDateFormat("HH:mm:ss").format(Date(t2))
        println("time: $time")
    }

    class A(var x: Int, private val y: Int) {
        val isEqual: Boolean
            get() = x == y
    }

    @Test
    fun testCmt1() {
        val a = A(1, 1)
        println("is equal: ${a.isEqual}")

    }

    @Test
    fun calculateTime() {

        var duration: Long = 1574419517_000
        var time = ""
        val hours = 0
        val minute = (duration / 60000).toLong()
        val seconds = (duration % 60000).toLong()
        val second = Math.floor(seconds.toDouble() / 1000).toLong()
        println("时间: hours:" + hours)

        if (minute < 10) {
            time += "0"
        }
        time += "$minute:"
        if (second < 10) {
            time += "0"
        }
        time += second
        val format = SimpleDateFormat("HH:mm:ss")
        val t1 = System.currentTimeMillis()
        time = format.format(duration)
        println("时间: time:$time, 耗时:${System.currentTimeMillis() - t1}")
    }

    @Test
    fun testMath() {
        val num = math("add", 3, 2)
        println("num: $num")
    }


    private fun math(type: String, n1: Int, n2: Int): Int {
        fun add(n1: Int, n2: Int): Int = n1 + n2
        fun sub(n1: Int, n2: Int): Int = n1 - n2
        val myfun: (Int, Int) -> Int
        myfun = when (type) {
            "add" -> {
                ::add
            }
            else -> {
                ::sub
            }
        }

        fun out(type: String, func: (Int, Int) -> Int) {
            val n: Int = func(1, 2)
            println("type:$type, out: $n")
        }
        out(type, myfun)


        return myfun(n1, n2)
    }

    private val lambdaList: ArrayList<(Int) -> Int> = ArrayList()
    private fun collectFn(fn: (Int) -> Int) {
        lambdaList.add(fn)
    }

    @Test
    fun testLambda1() {
        collectFn { it * it }
        collectFn { it * it * it }
        println("size: ${lambdaList.size}")
        for (i in lambdaList.indices) {
            println("for: ${lambdaList[i](i + 10)}")
        }
    }

    inline fun map(data: Array<Int>, fn: (Int) -> Int): Array<Int> {
        val result = Array<Int>(data.size) { 0 }
        for (i in data.indices) {
            result[i] = fn(data[i])
        }
        return result
    }

    @Test
    fun testInline() {
        val arr = arrayOf(20, 4, 40, 100, 39)
        val mapResult = map(arr) { it + 3 }
        println(mapResult.contentToString())
    }

    inline fun noInline(fn1: (Int) -> Int, noinline fn2: (String) -> String) {
        println(fn1(20))
        println(fn2("hahah"))
    }

    @Test
    fun testNoInline() {
        noInline({ it * it }, { "say $it" })
    }

    inline fun f(crossinline body: () -> Unit) {
        val f = Runnable { body() }
        Thread(f).start()

    }

    @Test
    fun testCrossLine() {
        f { println("cross inline") }
    }

    class ApplePack(weight: Double) {
        var weight: Double = weight
        override fun toString(): String {
            return "ApplePack(weight=$weight)"
        }
    }

    class Apple(weight: Double) {
        var weight: Double = weight
        override fun toString(): String {
            return "Apple(weight=$weight)"
        }

        infix fun add(other: Apple): ApplePack {
            return ApplePack(weight + other.weight)
        }

        infix fun sub(other: Apple): Apple {
            weight -= other.weight
            return this
        }
    }

    @Test
    fun testInfix() {
        var origin = Apple(3.4)
        val ap = origin add (Apple(2.4))
        println(ap)
        origin sub Apple(1.4)
        println(origin)
    }


    class User(name: String, pass: String, age: Int) {
        var name = name
        var pass = pass
        var age = age
        operator fun component1(): String {
            return this.name
        }

        operator fun component2(): String {
            return this.pass
        }

        operator fun component3(): Int {
            return this.age
        }
    }

    @Test
    fun testComponent() {
        val user = User("lao wang", "123321", 23)
        val (name, pass: String) = user
        println(name)
        println(pass)
        //将User对象解构给3个变量
        //利用user对象的component1(),component2(),component3()方法
        user.name = "老罗"
        var (name2,pass2,age2) = user
        println(name2)
        println(pass2)
        println(age2)

        var(_,pass3,age3) =user

        println(pass3)
        println(age3)

    }
}
