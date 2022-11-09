package my.application.ecofriend.datas

import java.io.Serializable

class PersonalData(
    val addr: String?,
    val phoneNum: String?,
    var total: Long?,
): Serializable {
}
