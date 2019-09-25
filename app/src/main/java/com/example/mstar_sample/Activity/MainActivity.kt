package com.example.mstar_sample.Activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.mstar_sample.Constant
import com.example.mstar_sample.Model.TokenResponse
import com.example.mstar_sample.Network.IRetrofit
import com.example.mstar_sample.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.api.services.drive.DriveScopes
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.RuntimeException


class MainActivity : AppCompatActivity(), View.OnClickListener {


    private lateinit var googleSignInClient: GoogleSignInClient

    private val iRetrofit by lazy {
        IRetrofit.createDrive()
    }

    var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        logIn.setOnClickListener(this)
        logOut.setOnClickListener(this)
        refreshToken.setOnClickListener(this)
        accessToken.setOnClickListener(this)
        amarylloCloud.setOnClickListener(this)


       val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
           .requestProfile()
           .requestEmail()
           .requestId()
           .requestScopes(Scope(DriveScopes.DRIVE_METADATA),Scope(DriveScopes.DRIVE_READONLY))
           .requestServerAuthCode("996013012533-k07i8nnee7kr9h45iuo8igg0g2792v45.apps.googleusercontent.com", true)
           .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)


    }

    private fun signIn () {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent,
            RC_SIGN_IN
        )
    }

    private fun signOut () {
        googleSignInClient.signOut().addOnCompleteListener(this) {

        }
    }

    private fun refreshToken () {

        try {
            var requestToken = iRetrofit.requestRefreshToken(
                Constant.CLIENT_ID,
                Constant.CLIENT_SECRET,
                Constant.AUTH_CODE,
            "authorization_code")

        requestToken.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe ( {response: TokenResponse? ->
                Constant.REFRESH_TOKEN = response?.refreshToken.toString()
                Constant.ACCESS_TOKEN = response?.accessToken.toString()
                Log.e(TAG + " R Token", response?.refreshToken.toString())
            }, { error ->
                Log.e(TAG, error.message)
            } ) }

        catch (e: RuntimeException) {
            Log.e("Runtime Exception", e.message)
        }

//        requestToken.enqueue(object : retrofit2.Callback<TokenResponse> {
//            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
//                Log.e(TAG, t.message)
//            }
//
//            override fun onResponse(
//                call: Call<TokenResponse>, response: retrofit2.Response<TokenResponse>) {
//
//                Log.e(TAG + " Response", response.toString())
//                Constant.ACCESS_TOKEN = response.body()?.accessToken.toString()
//                Log.e(TAG + " A Token", response.body()?.accessToken.toString())
//                Constant.REFRESH_TOKEN = response.body()?.refreshToken.toString()
//                Log.e(TAG + " R Token", response.body()?.refreshToken.toString())
//            }
//        })
    }

    private fun accessToken() {

        try {
        var requestToken = IRetrofit.createDrive().requestAccessToken(
            Constant.CLIENT_ID,
            Constant.CLIENT_SECRET,
            Constant.REFRESH_TOKEN,
            "refresh_token"
                )

        requestToken.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe( {
                response: TokenResponse? ->
                Constant.ACCESS_TOKEN = response?.accessToken.toString()
                Log.e("New A Token", Constant.ACCESS_TOKEN)
            }, {error ->
                Log.e("error", error.message)
            })
        }
        catch (e: RuntimeException) {
            Log.e("Runtime Exception", e.message)
        }
    }

    private fun amarylloCloud () {
        intent = Intent(this, DriveActivity::class.java)
        startActivity(intent)
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                Constant.AUTH_CODE = account.serverAuthCode.toString()
                Log.e(TAG, account.displayName + " " + account.email + " " +account.id)
                Log.e(TAG, account.grantedScopes.toString())
                Log.e(TAG, account.serverAuthCode)
                Log.e(
                    TAG + "Constant",
                    Constant.AUTH_CODE
                )


            } catch (e: ApiException) {
                Log.e(TAG, "Sign in failed", e)
            }

        }
    }

    override fun onClick(v: View) {
        val i = v.id
        when (i) {
            R.id.logIn -> signIn()
            R.id.logOut -> signOut()
            R.id.refreshToken -> refreshToken()
            R.id.accessToken -> accessToken()
            R.id.amarylloCloud -> amarylloCloud()
        }
    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }
    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }
}
