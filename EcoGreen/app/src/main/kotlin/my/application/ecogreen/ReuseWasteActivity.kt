package my.application.ecogreen

import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import kotlinx.android.synthetic.main.activity_reuse.*
import kotlinx.android.synthetic.main.disuse_waste_image.*
import java.io.IOException
import java.util.*

class ReuseWasteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reuse)
        var myUriString : String = " "
        var site = resources.getStringArray(R.array.site)
        var region = resources.getStringArray(R.array.seoul_region)

        location.setOnClickListener {

            result.text = intent.getStringExtra("gps")

            for(i in region.size -1 downTo 1){
                if(region[i] == result.text.substring(0,result.text.indexOf("구")+1)){
                    myUriString = site[i-1]
                    reuse_site.text = myUriString
                }
            }
        }

        back_arrow.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = myUriString.toUri()
            startActivity(intent)
        }
    }
}