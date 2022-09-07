package my.application.ecogreen.datas

import java.io.Serializable

class DetailItem(
    val todaydate: String?,
    val selectdate: String?,
    val addr: String?,
    val classification: String?,
    val item: String?,
    var total: Long?,
    var state: Long?
): Serializable {
}

