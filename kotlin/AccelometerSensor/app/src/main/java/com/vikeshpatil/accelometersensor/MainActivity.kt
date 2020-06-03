package com.vikeshpatil.accelometersensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast

class MainActivity : AppCompatActivity(), SensorEventListener {
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        println("On Accuracy Changed")
    }


    var xOld = 0.0
    var yOld = 0.0
    var zOld = 0.0
    var threashold = 3000
    var oldTime:Long = 0

    override fun onSensorChanged(event: SensorEvent?) {

        var x = event!!.values[0]
        var y = event!!.values[1]
        var z = event!!.values[2]
        var currentTime = System.currentTimeMillis()

        if((currentTime - oldTime) > 100){

            var timeDiff = currentTime - oldTime
            oldTime = currentTime
            var speed = Math.abs(x + y + z - xOld - yOld - zOld) / timeDiff * 10000

            if (speed > threashold){
                var vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

                if(Build.VERSION.SDK_INT >= 26){
                    vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
                    Toast.makeText(applicationContext, "Shakking", Toast.LENGTH_LONG).show()
                }else{
                    vibrator.vibrate(500)
                }

            }

        }
    }

    var sensorManager: SensorManager? = null
    var sensor: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
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
