package com.vikeshpatil.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import java.lang.Exception

class MainActivity : AppCompatActivity(), SensorEventListener {
    var sensorManager:SensorManager? = null
    var sensor: Sensor? = null
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }


    var isRunning = false
    override fun onSensorChanged(event: SensorEvent?) {

        if (event!!.values[0] > 40 && isRunning == false){
            isRunning = true
            try {
                Toast.makeText(this, "Change in Light Observed", Toast.LENGTH_LONG).show()

//            Run the music
//                var mediaPlayer = MediaPlayer()
//                mediaPlayer.setDataSource("url to music")
//                mediaPlayer.prepare()
//                mediaPlayer.start()
            }catch (e: Exception){
                println("Error in playing music after change in light: " + e.message)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_LIGHT)
    }

    override fun onResume() {
        super.onResume()

        sensorManager!!.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()

        sensorManager!!.unregisterListener(this)
    }

}
