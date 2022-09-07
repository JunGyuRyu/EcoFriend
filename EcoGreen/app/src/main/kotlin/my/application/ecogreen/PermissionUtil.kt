package my.application.ecogreen

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionUtil {

    //가변 인수 활용하여 권한 없는 것 다 요청 가능
    fun requestPermission(activity: Activity, requestCode: Int, vararg permissions: String
    ): Boolean {
        var granted = true
        val permissionNeeded = ArrayList<String>() //얻어야 하는 permission 받아두기

        //index 필요시 for문용, forEachIndexed 사용, index 필요 없이 단독 개체만 필요한 경우 forEach 사용
        permissions.forEach {
            //forEach를 통해서 넘어온 하나가 바로 it이 됨
            val permissionCheck = ContextCompat.checkSelfPermission(activity, it)
            val hasPermission = permissionCheck == PackageManager.PERMISSION_GRANTED
            granted = granted and hasPermission
            if(!hasPermission){
                permissionNeeded.add(it)
            }
        }

        if(granted) return true
        else{
            ActivityCompat.requestPermissions(
                activity, permissionNeeded.toTypedArray(), requestCode
            )
            return false
        }
    }

    fun permissionGranted(
        requestCode: Int, permissionCode: Int, grantResults: IntArray
    ): Boolean {
        //grantResults.size >0는 획득된 권한이 하나라도 있는지 확인, 있다면 첫번째 권한이 얻어졌는지 확인
        return requestCode == permissionCode && grantResults.size >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
    }

}