package com.sirekanyan.andersrobot

import com.sirekanyan.andersrobot.extensions.getClosest
import org.junit.Assert.assertEquals
import org.junit.Test

class MapTest {

    @Test
    fun getClosestTest0() {
        assertEquals(null, mapOf<Int, String>().getClosest(0))
    }

    @Test
    fun getClosestTest1() {
        assertEquals("zero", mapOf(0 to "zero").getClosest(0))
    }

    @Test
    fun getClosestTest2() {
        assertEquals("zero", mapOf(0 to "zero").getClosest(1))
    }

    @Test
    fun getClosestTest3() {
        assertEquals("two", mapOf(0 to "zero", 2 to "two").getClosest(1))
    }

    @Test
    fun getClosestTest4() {
        assertEquals("two", mapOf(2 to "two", 0 to "zero").getClosest(1))
    }

    @Test
    fun getClosestTest5() {
        assertEquals("zero", mapOf(0 to "zero", 3 to "three").getClosest(1))
    }

    @Test
    fun getClosestTest6() {
        assertEquals("three", mapOf(0 to "zero", 3 to "three").getClosest(2))
    }

}