package com.vikeshpatil.mediaplayer

import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.song_ticket.view.*
import java.lang.Exception
import java.util.zip.Inflater

class MainActivity : AppCompatActivity() {

    var songsList = ArrayList<Song>()
    var adapter: SongsAdapter? = null
    var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        LoadOnlineSongs()
        CheckPermission()


        var songProgressThread = SongProgressThread()
        songProgressThread.start()
    }

    fun LoadOnlineSongs(){
        songsList.add(Song("Memories", "Maroon 5", ""))
        songsList.add(Song("Imagination", "Inrique", ""))
        songsList.add(Song("Something Just Like This", "Chainsmoker", ""))
    }

    inner class SongsAdapter: BaseAdapter {

        var songsList = ArrayList<Song>()
        constructor(songsList: ArrayList<Song>){

            this.songsList = songsList
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            val inflater = layoutInflater.inflate(R.layout.song_ticket, null)
            val song = this.songsList[position]
            inflater.tvSongTitle.text = song.title
            inflater.tvArtistName.text = song.artist

            inflater.btnPlay.setOnClickListener(View.OnClickListener {
            //TODO: play song


                if(inflater.btnPlay.text.equals("Stop")){
                    mediaPlayer!!.stop()
                    inflater.btnPlay.text = "Play"
                }else{
                    try {
                        mediaPlayer = MediaPlayer()
                        mediaPlayer!!.setDataSource(song.songUrl)
                        mediaPlayer!!.prepare()
                        mediaPlayer!!.start()
                        inflater.btnPlay.text = "Stop"
                        sbSongProgress.max = mediaPlayer!!.duration
                    }catch (e: Exception){
                        println("Error in playing song" + e.message)
                    }
                }

            })

            return inflater
        }

        override fun getItem(position: Int): Any {
            return this.songsList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return songsList.size
        }
    }

    inner class SongProgressThread(): Thread(){

        override fun run() {
            while (true){
                try {
                    Thread.sleep(1000)
                }catch (ex: Exception){
                    println("Error in thread UI")
                }

                runOnUiThread{
                    if(mediaPlayer != null){
                        sbSongProgress.progress = mediaPlayer!!.currentPosition
                    }
                }
            }
        }
    }

    fun LoadOfflineSong(){

        val allSongsURI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Audio.Media.IS_MUSIC + "!=0"
        val cursor = contentResolver.query(allSongsURI, null, selection, null, null)

        if (cursor != null){
            if (cursor!!.moveToFirst()){

                do {

                    val songURL = cursor!!.getString(cursor!!.getColumnIndex(MediaStore.Audio.Media.DATA))
                    val songArtist= cursor!!.getString(cursor!!.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                    val songName= cursor!!.getString(cursor!!.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))
                    songsList.add(Song(songName, songArtist, songURL))
                }while (cursor!!.moveToNext())
            }
            cursor.close()

            adapter = SongsAdapter(songsList)
            lvSongsList.adapter = adapter
        }

    }

    val REQUEST_CODE = 123
    fun CheckPermission(){
        if(Build.VERSION.SDK_INT >= 23){
            if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE)
                return
            }
        }

        LoadOfflineSong()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            REQUEST_CODE -> if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                LoadOfflineSong()
            }
            else{
                Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_LONG).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
