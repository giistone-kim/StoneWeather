package com.example.thirdproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.activity_account_setting.*

class AccountSettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_setting)


        setupListener()

    }

    private fun setupListener() {
        account_setting_back.setOnClickListener { onBackPressed() }
        account_setting_logout.setOnClickListener { signoutAccount() }
        account_setting_delete.setOnClickListener { showDeleteDialog() }
    }

    private fun signoutAccount() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                Toast.makeText(this, "로그아웃 하셨습니다", Toast.LENGTH_SHORT).show()
                moveToMainActivity()
            }
    }

    private fun deleteAccount() {

        AuthUI.getInstance()
            .delete(this)
            .addOnCompleteListener {
                Toast.makeText(this, "정상적으로 탈퇴 됐습니다.", Toast.LENGTH_SHORT).show()
                moveToMainActivity()

            }

    }

    private fun showDeleteDialog() {
        AccountDeleteDialog().apply {
            addAccountDeleteDialogInterface(object : AccountDeleteDialog.AccountDeleteDialogInterface {
                override fun delete() {
                    deleteAccount()
                }

                override fun cancelDelete() {
                }
            })
        }.show(supportFragmentManager, "")

        //변수를 이 작업 이외에도 쓸경우에는 아래코드가 좋고 이번에만 쓸거면 위에가 좋음
        //변수를 만드는것 자체가 유지관리 할 것이 늘어나게됨

//        val accountDeleteDialog = AccountDeleteDialog()
//        accountDeleteDialog.addAccountDeleteDialogInterface(object : AccountDeleteDialog.AccountDeleteDialogInterface {
//
//            override fun delete() {
//
//            }
//
//            override fun cancelDelete() {
//
//            }
//        })
//        accountDeleteDialog.show(supportFragmentManager,"")

    }

    private fun moveToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }

}
