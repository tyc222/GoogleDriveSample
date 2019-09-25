package com.example.mstar_sample.Activity

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.TextView
import com.example.mstar_sample.Constant
import com.example.mstar_sample.DriveFileRecycleAdapter
import com.example.mstar_sample.Model.DriveResponse
import com.example.mstar_sample.Model.FileResponse
import com.example.mstar_sample.Network.IRetrofit
import com.example.mstar_sample.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_drive.*
import java.lang.Exception

class DriveActivity : AppCompatActivity() {

    private val disposable: Disposable? = null
    private lateinit var driveAdapter: DriveFileRecycleAdapter
    var parentId:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drive)

        try {
            requestDriveFiles("", "*", clouldRecyclerView)
        } catch (e: Exception) {
            Log.e("Error", e.message)
        }
    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }

    override fun onBackPressed() {
        if (parentId != null){
            requestDriveFiles(
                "",
                "files(kind, id, name, mimeType, description, createdTime, webContentLink, webViewLink, thumbnailLink, shared, originalFilename, parents)",
                clouldRecyclerView
                )
        } else {
            finish()
        }
    }

    private fun requestDriveFiles(
        name_mimeType_sharedWithMe_parent: String, fields: String, cloudView: RecyclerView) {
        cloudView.recycledViewPool.clear()
        cloudView.layoutManager = LinearLayoutManager(this)

        var requestDriveFile = IRetrofit.createDrive().requestDriveFiles(
            "Bearer " + Constant.ACCESS_TOKEN,
            "",
            name_mimeType_sharedWithMe_parent,
            fields
        )

        requestDriveFile.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ response: DriveResponse? ->

                try {
                    parentId = response!!.files.get(0).parents.get(0)
                    Log.e("parents", response!!.files.get(0).parents.get(0))
                } catch (exception:Exception) {
                    Log.e("parents", exception.message)
                    parentId = null
                }

                driveAdapter = DriveFileRecycleAdapter(
                    response!!.files, this, { response: FileResponse -> fileDataclicked(response, cloudView) }
                )
                cloudView.adapter = driveAdapter
            }, { error ->
                Log.e("Drive Error", error.message)
            })
    }

    private fun fileDataclicked(fileResponse: FileResponse, cloudView: RecyclerView) {
        if (fileResponse.mimeType == "video/x-msvideo") {
            var uri: Uri = Uri.parse(fileResponse.webContentLink)
            var intent = Intent(Intent.ACTION_VIEW, uri)
            intent.setDataAndType(Uri.parse(fileResponse.webContentLink), "video/")
            this.startActivity(intent)
        } else if (fileResponse.mimeType == "application/vnd.google-apps.folder") {
            var currentFileId = fileResponse.id
            requestDriveFiles(
                "'$currentFileId' in parents",
                "files(kind, id, name, mimeType, description, createdTime, webContentLink, webViewLink, thumbnailLink, shared, originalFilename, parents)",
                cloudView
            )
        } else {
            val snackbar = Snackbar.make(driveLayout, "File Type Not Support", Snackbar.LENGTH_SHORT)
            var view = snackbar.view
            var snackbarTextView:TextView = view.findViewById(android.support.design.R.id.snackbar_text)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                snackbarTextView.autoSizeMaxTextSize
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                snackbarTextView.textAlignment = View.TEXT_ALIGNMENT_CENTER
            }
            snackbar.show()
        }
    }
}



