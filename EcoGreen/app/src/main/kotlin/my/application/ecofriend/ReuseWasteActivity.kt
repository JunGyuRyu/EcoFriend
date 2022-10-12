package my.application.ecofriend

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import kotlinx.android.synthetic.main.activity_reuse.*
import my.application.ecofriend.databinding.ActivityReuseBinding

class ReuseWasteActivity : AppCompatActivity() {
    private var mBinding: ActivityReuseBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_reuse)
        mBinding = ActivityReuseBinding.inflate(layoutInflater)
        setContentView(binding.root)


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