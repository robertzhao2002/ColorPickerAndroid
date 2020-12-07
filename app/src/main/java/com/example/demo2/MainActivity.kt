package com.example.demo2

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    lateinit var colorSquare: View
    lateinit var redSlider: SeekBar
    lateinit var greenSlider: SeekBar
    lateinit var blueSlider: SeekBar
    lateinit var redText: TextView
    lateinit var greenText: TextView
    lateinit var blueText: TextView
    lateinit var hashcode: TextView
    lateinit var buttonToggle: Button
    lateinit var share: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)
        var orientation = resources.configuration.orientation
        colorSquare = findViewById(R.id.color_square)
        redSlider = findViewById(R.id.seekBarR)
        greenSlider = findViewById(R.id.seekBarG)
        blueSlider = findViewById(R.id.seekBarB)
        redText = findViewById(R.id.textViewR)
        greenText = findViewById(R.id.textViewG)
        blueText = findViewById(R.id.textViewB)
        buttonToggle = findViewById(R.id.buttonRGB)
        share = findViewById(R.id.BshareR)

        var red: Int = redSlider.progress
        var green: Int = greenSlider.progress
        var blue: Int = blueSlider.progress
        var hsvArr: FloatArray = floatArrayOf(0f, 0f, 0f)
        Color.RGBToHSV(red, green, blue, hsvArr)
        var hue = hsvArr[0]
        var saturation: Float = hsvArr[1]
        var value: Float = hsvArr[2]

        intent.extras?.let{bundle ->  
            red = bundle.get("red") as Int
            green = bundle.get("green") as Int
            blue = bundle.get("blue") as Int
            hue = bundle.get("hue") as Float
            saturation = bundle.get("saturation") as Float
            value = bundle.get("value") as Float
            hsvArr = bundle.get("hsvArr") as FloatArray
        }

        redSlider.progress = red
        greenSlider.progress = green
        blueSlider.progress = blue

        colorSquare.setBackgroundColor(Color.rgb(red, green, blue))

        println("red $red")
        println("green $green")
        println("blue $blue")



        if(orientation == Configuration.ORIENTATION_LANDSCAPE) {
            hashcode = findViewById(R.id.ColorCode)
            var hexString = Integer.toHexString(red).padStart(2, '0') + Integer.toHexString(green).padStart(2, '0') + Integer.toHexString(blue).padStart(2, '0')
            hashcode.text = "#$hexString"
        }
        else {
            redText.text = "Red: $red"
            greenText.text = "Green: $green"
            blueText.text = "Blue: $blue"
        }

        share.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            var hexString = Integer.toHexString(red).padStart(2, '0') + Integer.toHexString(green).padStart(2, '0') + Integer.toHexString(blue).padStart(2, '0')
            intent.putExtra(Intent.EXTRA_TEXT, "#$hexString")
            intent.type = "text/plain"
            startActivity(intent)
        }

        buttonToggle.setOnClickListener {
            // Handler code here.
            val intent = Intent(this, HSVActivity::class.java).apply{
                putExtra("red", red)
                putExtra("green", green)
                putExtra("blue", blue)
                putExtra("hue", hue)
                putExtra("saturation", saturation)
                putExtra("value", value)
                putExtra("hsvArr", hsvArr)
            }
            startActivity(intent);
        }

        redSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean) {
                colorSquare.setBackgroundColor(Color.rgb(progress,green,blue))
                red = progress
                Color.RGBToHSV(red, green, blue, hsvArr)
                hue = hsvArr[0]
                saturation = hsvArr[1]
                value = hsvArr[2]
                if(orientation == Configuration.ORIENTATION_PORTRAIT) {
                    println("red $red")
                    println("green $green")
                    println("blue $blue")
                    println("redhex" + Integer.toHexString(red))
                    redText.text = "Red: $progress"
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

        greenSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean) {
                colorSquare.setBackgroundColor(Color.rgb(red,progress,blue))
                green = progress
                Color.RGBToHSV(red, green, blue, hsvArr)
                hue = hsvArr[0]
                saturation = hsvArr[1]
                value = hsvArr[2]
                if(orientation == Configuration.ORIENTATION_PORTRAIT) {
                    println("red $red")
                    println("green $green")
                    println("blue $blue")
                    greenText.text = "Green: $progress"
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

        blueSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean) {
                colorSquare.setBackgroundColor(Color.rgb(red,green,progress))
                blue = progress
                Color.RGBToHSV(red, green, blue, hsvArr)
                hue = hsvArr[0]
                saturation = hsvArr[1]
                value = hsvArr[2]
                if(orientation == Configuration.ORIENTATION_PORTRAIT) {
                    println("red $red")
                    println("green $green")
                    println("blue $blue")
                    blueText.text = "Red: $progress"
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

}