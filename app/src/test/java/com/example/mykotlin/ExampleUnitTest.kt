package com.example.mykotlin

import org.junit.Test

import org.junit.Assert.*
import java.text.SimpleDateFormat
import java.util.*

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

}
