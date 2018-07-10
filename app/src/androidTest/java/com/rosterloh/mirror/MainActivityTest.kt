package com.rosterloh.mirror

import com.rosterloh.mirror.MainActivity
import org.junit.Rule

class MainActivityTest {

    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule(MainActivity::class.java)
}