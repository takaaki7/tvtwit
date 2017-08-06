package com.hubtele.android.util

import com.hubtele.android.BuildConfig
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricGradleTestRunner
import org.robolectric.annotation.Config
import java.util.*


/**
 * Created by nakama on 2016/02/03.
 */
@RunWith(RobolectricGradleTestRunner::class)
@Config(constants = BuildConfig::class, sdk = intArrayOf(21))
class PrefUtilTest {

    @After
    fun after() {
        PrefUtil.clear()
    }

    @Test
    fun clearで全て消える() {
        PrefUtil.saveInt("a", 2)
        PrefUtil.saveString("b", "bb")
        PrefUtil.saveBoolean("c", true)
        PrefUtil.saveDate("d", Date())
        PrefUtil.clear()
        assertEquals(-1, PrefUtil.loadInt("a"))
        assertNull(PrefUtil.loadString("b"))
        assertEquals(false, PrefUtil.loadBooleanDefFalse("c"))
        assertNull(PrefUtil.loadDate("d"))
    }

    @Test
    fun intをsaveしたらloadできる() {
        PrefUtil.saveInt("testi", 3)
        assertTrue(PrefUtil.loadInt("testi") == 3)
        assertEquals(3, PrefUtil.loadInt("testi"))
    }

    @Test
    fun stringをsaveしたらloadできる() {
        PrefUtil.saveString("tests", "test")
        assertEquals("test", PrefUtil.loadString("tests"))
    }

    @Test
    fun booleanをsaveしたらloadできる() {
        PrefUtil.saveBoolean("testb", true)
        assertEquals(true, PrefUtil.loadBooleanDefFalse("testb"))
    }

    @Test
    fun loadBooleanのdefault(){
        assertEquals(true,PrefUtil.loadBooleanDefTrue("nonexistskey"))
        assertEquals(false,PrefUtil.loadBooleanDefFalse("nonexistskey"))
    }

    @Test
    fun dateをsaveしたらloadできる(){
        var date=Date()
        PrefUtil.saveDate("testd",date)
        assertEquals(date,PrefUtil.loadDate("testd"))
    }

    @Test
    fun saveしてないloadDateはnullを返す(){
        assertNull(PrefUtil.loadDate("nonexistskey"))
    }

}