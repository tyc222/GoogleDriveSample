package com.example.mstar_sample

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.mstar_sample.Activity.DriveActivity
import com.example.mstar_sample.Model.DriveResponse
import com.example.mstar_sample.Model.FileResponse
import com.example.mstar_sample.Network.IRetrofit
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_drive.*
import kotlinx.android.synthetic.main.drive_file_recycle_adapter.view.*
import java.io.File
import java.lang.Exception

class DriveFileRecycleAdapter(val response: ArrayList<FileResponse>, val context: Context, val clickListener: (FileResponse) -> Unit): RecyclerView.Adapter<DriveFileRecycleAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.drive_file_recycle_adapter, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return response.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(response[position], clickListener)

//
        holder.driveName.text = response.get(position).name
        Picasso.get().load(response.get(position).thumbnailLink).placeholder(R.mipmap.ic_launcher).into(holder.driveImage)
//        holder.driveContrainer.setOnClickListener {
//
//            if (response.get(position).mimeType == "video/x-msvideo") {
//            var uri: Uri = Uri.parse(response.get(position).webContentLink)
//            var intent = Intent(Intent.ACTION_VIEW, uri)
//            intent.setDataAndType(Uri.parse(response.get(position).webContentLink), "video/")
//            context.startActivity(intent)
//            }
//            else {
//
//            }
//        }
        holder.driveImage.setOnLongClickListener{
            Log.e("File Id", response.get(position).id)
            Log.e("Type", response.get(position).mimeType)
            Log.e("Name", response.get(position).name)
            true
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val driveImage: ImageView = itemView.driveImageView
        val driveName: TextView = itemView.driveTextView
//        val driveContrainer = itemView.driveContainer
        fun bind(filedata: FileResponse, clickListener: (FileResponse) -> Unit) {
//        itemView.driveTextView.text = filedata.name
//        Picasso.get().load(filedata.thumbnailLink).placeholder(R.mipmap.ic_launcher).into(itemView.driveImageView)
        itemView.driveContainer.setOnClickListener { clickListener(filedata) }
        }


    }


}