package com.example.thirdproject

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class IntroActivity :AppCompatActivity(){

    val SPLASH_TIME_OUT: Long = 2000//3초간 보여주고 엑티비티 이동

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)



        Handler().postDelayed({
            //어떤 엑티비티로 넘어갈지 결정

            startActivity(Intent(this, MainActivity::class.java ))
            finish()
        }, SPLASH_TIME_OUT)


    }


}