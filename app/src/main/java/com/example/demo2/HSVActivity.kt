package com.example.demo2

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.red
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.hsv.*
import kotlin.math.roundToInt

class HSVActivity : AppCompatActivity(){
    lateinit var colorSquare: View
    lateinit var hueSlider: SeekBar
    lateinit var saturationSlider: SeekBar
    lateinit var valueSlider: SeekBar
    lateinit var hueText: TextView
    lateinit var saturationText: TextView
    lateinit var valueText: TextView
    lateinit var hashcode: TextView
    lateinit var buttonToggle: Button
    lateinit var buttonLocation: Button
    lateinit var share: Button

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hsv)
        var orientation = resources.configuration.orientation
        colorSquare = findViewById(R.id.color_square)
        hueSlider = findViewById(R.id.seekBarH)
        saturationSlider = findViewById(R.id.seekBarS)
        valueSlider = findViewById(R.id.seekBarV)
        hueText = findViewById(R.id.textViewH)
        saturationText = findViewById(R.id.textViewS)
        valueText = findViewById(R.id.textViewV)
        buttonToggle = findViewById(R.id.buttonHSV)
        buttonLocation = findViewById(R.id.buttonloc)
        share = findViewById(R.id.Bshare)

        println("DECIMAL: " + Integer.parseInt("ff", 16).toString())

        var hue: Float = hueSlider.progress.toFloat()
        var saturation: Float = saturationSlider.progress.toFloat()/100
        var value: Float = valueSlider.progress.toFloat()/100
        var squarecolor = Color.HSVToColor(floatArrayOf(hue.toFloat(), saturation, value))
        var red = Color.red(squarecolor)
        var green = Color.green(squarecolor)
        var blue = Color.blue(squarecolor)
        var hsvArr: FloatArray = floatArrayOf(hue, saturation, value)

        intent.extras?.let{bundle ->
            red = bundle.get("red") as Int
            green = bundle.get("green") as Int
            blue = bundle.get("blue") as Int
            hue = bundle.get("hue") as Float
            saturation = bundle.get("saturation") as Float
            value = bundle.get("value") as Float
            hsvArr = bundle.get("hsvArr") as FloatArray
        }
        var hueint: Int = hue.roundToInt()
        var satint: Int = (saturation*100).roundToInt()
        var valint: Int = (value*100).roundToInt()
        hueSlider.progress = hueint
        saturationSlider.progress = satint
        valueSlider.progress = valint
        colorSquare.setBackgroundColor(Color.rgb(red, green, blue))

        if(orientation == Configuration.ORIENTATION_LANDSCAPE) {
            hashcode = findViewById(R.id.ColorCode)
            var hexString = Integer.toHexString(red).padStart(2, '0') + Integer.toHexString(green).padStart(2, '0') + Integer.toHexString(blue).padStart(2, '0')
            hashcode.text = "#$hexString"
        }
        else {
            hueText.text = "Hue: $hueint°"
            saturationText.text = "Saturation: $satint%"
            valueText.text = "Value: $valint%"
        }

        var lat = "0"

        share.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            var hexString = Integer.toHexString(red).padStart(2, '0') + Integer.toHexString(green).padStart(2, '0') + Integer.toHexString(blue).padStart(2, '0')
            intent.putExtra(Intent.EXTRA_TEXT, "#$hexString")
            intent.type = "text/plain"
            startActivity(intent)
        }

        buttonLocation.setOnClickListener{
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
                // Define the click listener as a member
                class MyRetryListener : View.OnClickListener {

                    override fun onClick(v: View) {
                        ActivityCompat.requestPermissions(this@HSVActivity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 100)
                    }
                }

                Snackbar.make(it, "Location permissions are not granted", Snackbar.LENGTH_LONG)
                .setAction(R.string.snack, MyRetryListener())
                .setActionTextColor(resources.getColor(R.color.green))
                .setDuration(6000).show()
            } else {
                var location = LocationServices.getFusedLocationProviderClient(this)
                location.lastLocation.addOnSuccessListener {
                    println("latitude: " + it.latitude.toString())
                    lat = getColorString(it.latitude)
                    println("lat: " + lat)
                    red = Integer.parseInt(lat.substring(1,3), 16)
                    green = Integer.parseInt(lat.substring(3,5), 16)
                    blue = Integer.parseInt(lat.substring(5,7), 16)
                    println("RGB: $red$green$blue")
                    Color.RGBToHSV(red, green, blue, hsvArr)
                    colorSquare.setBackgroundColor(Color.rgb(red, green, blue))
                    hue = hsvArr[0]
                    saturation = hsvArr[1]
                    value = hsvArr[2]
                    hueint = hsvArr[0].roundToInt()
                    satint = (hsvArr[1]*100).roundToInt()
                    valint = (hsvArr[2]*100).roundToInt()
                    hueSlider.progress = hueint
                    saturationSlider.progress = satint
                    valueSlider.progress = valint

                    if(orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        hashcode = findViewById(R.id.ColorCode)
                        var hexString = lat
                        hashcode.text = "$hexString"
                    }
                    else{
                        hueText.text = "Hue: $hueint°"
                        saturationText.text = "Saturation: $satint%"
                        valueText.text = "Value: $valint%"
                    }
                }
            }
        }

        buttonToggle.setOnClickListener {
            // Handler code here.
            val intent = Intent(this, MainActivity::class.java).apply{
                putExtra("red", red)
                putExtra("green", green)
                putExtra("blue", blue)
                putExtra("hue", hue)
                putExtra("saturation", saturation)
                putExtra("value", value)
                putExtra("hsvArr", hsvArr)
            }
            startActivity(intent)
        }

        hueSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean) {
                squarecolor = Color.HSVToColor(floatArrayOf(progress.toFloat(), saturation, value))
                red = Color.red(squarecolor)
                green = Color.green(squarecolor)
                blue = Color.blue(squarecolor)
                colorSquare.setBackgroundColor(Color.rgb(red, green , blue))
                hue = progress.toFloat()
                hsvArr = floatArrayOf(hue, saturation, value)
                println("hue $hue")
                println("saturation $saturation")
                println("value $value")
                if(orientation == Configuration.ORIENTATION_PORTRAIT) {
                    println("red $red")
                    println("green $green")
                    println("blue $blue")
                    hueText.text = "Hue: $progress°"
                }
                else {
                    var hexString = Integer.toHexString(red).padStart(2, '0') + Integer.toHexString(green).padStart(2, '0') + Integer.toHexString(blue).padStart(2, '0')
                    println(hexString)
                    hashcode.text = "#$hexString"
                }
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
                // write custom code for progress is started
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                // write custom code for progress is stopped
            }
        })

        saturationSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean) {
                squarecolor = Color.HSVToColor(floatArrayOf(hue.toFloat(), progress.toFloat()/100, value))
                red = Color.red(squarecolor)
                green = Color.green(squarecolor)
                blue = Color.blue(squarecolor)
                colorSquare.setBackgroundColor(Color.rgb(red, green, blue))
                saturation = progress.toFloat()/100
                hsvArr = floatArrayOf(hue, saturation, value)
                println("hue $hue")
                println("saturation $saturation")
                println("value $value")
                if(orientation == Configuration.ORIENTATION_PORTRAIT) {
                    println("red $red")
                    println("green $green")
                    println("blue $blue")
                    saturationText.text = "Saturation: $progress%"
                }
                else {
                    var hexString = Integer.toHexString(red).padStart(2, '0') + Integer.toHexString(green).padStart(2, '0') + Integer.toHexString(blue).padStart(2, '0')
                    println(hexString)
                    hashcode.text = "#$hexString"
                }
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
                // write custom code for progress is started
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                // write custom code for progress is stopped
            }
        })

        valueSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean) {
                squarecolor = Color.HSVToColor(floatArrayOf(hue.toFloat(), saturation, progress.toFloat()/100))
                red = Color.red(squarecolor)
                green = Color.green(squarecolor)
                blue = Color.blue(squarecolor)
                colorSquare.setBackgroundColor(Color.rgb(red, green, blue))
                value = progress.toFloat()/100
                hsvArr = floatArrayOf(hue, saturation, value)
                println("hue $hue")
                println("saturation $saturation")
                println("value $value")
                if(orientation == Configuration.ORIENTATION_PORTRAIT) {
                    println("red $red")
                    println("green $green")
                    println("blue $blue")
                    valueText.text = "Value: $progress%"
                }
                else {
                    var hexString = Integer.toHexString(red).padStart(2, '0') + Integer.toHexString(green).padStart(2, '0') + Integer.toHexString(blue).padStart(2, '0')
                    println(hexString)
                    hashcode.text = "#$hexString"
                }
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
                // write custom code for progress is started
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                // write custom code for progress is stopped
            }
        })
    }

    // enter location.latitude as a parameter

    private fun getColorString(latitude : Double) : String {
        return resources.getString(R.string.locationString, ((latitude % 1)*10000).roundToInt().toString().padStart(6, '0'))
    }
}