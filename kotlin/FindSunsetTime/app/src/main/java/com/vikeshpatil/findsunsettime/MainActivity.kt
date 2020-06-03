package com.vikeshpatil.findsunsettime

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }

     fun GetSunsetTime(view: View){
        var city = InputCity.text.toString()
        var lat:Double = 19.04
         var long:Double = 73.02
//        val url = "\n" +
//                "https://api.sunrise-sunset.org/json?lat=" + lat + "&lng=" + long + "&date=today"

         val url = "https://api.sunrise-sunset.org/json?lat=36.7201600&lng=-4.4203400"

        asyncTask().execute(url)
    }

    inner class asyncTask: AsyncTask<String, String, String>() {



        override fun onPostExecute(result: String?) {

        }

        override fun doInBackground(vararg params: String?): String {

            try {
                val url = URL(params[0])
                val urlConnect = url.openConnection() as HttpsURLConnection
                urlConnect.connectTimeout = 7000

                var urlInString = ConvertStreamToString(urlConnect.inputStream)

                publishProgress(urlInString)
            }catch (e: Exception){
                println("Error in running in background " + e.message)
                e.stackTrace
            }

            return " "
        }

        override fun onProgressUpdate(vararg values: String?) {

            try {
                var json = JSONObject(values[0])
                val current_observation = json.getJSONObject("current_observation")
                val astronomy = current_observation.getJSONObject("astronomy")
//                val channel = results.getJSONObject("channel")
//                val astronomy = channel.getJSONObject("astronomy")
                var sunrise = astronomy.getString("sunrise")
                TVSunsetTime.text = "Sunrise time is + " + sunrise
            }catch (e: Exception){
                println("Progress update error + " + e.message)
            }
        }

        override fun onPreExecute() {

        }
    }

    fun ConvertStreamToString(inputStream:InputStream):String{

        val bufferReader = BufferedReader(InputStreamReader(inputStream))
        var line:String
        var allString:String = ""

        try {
            do {

                line = bufferReader.readLine()
                if (line!=null){
                    allString += line
                }
            }while (line != null)
            inputStream.close()
        }catch (e: Exception){
            println("Error in ConvertStreamToString "+ e.message)
        }

        return allString
    }


}
