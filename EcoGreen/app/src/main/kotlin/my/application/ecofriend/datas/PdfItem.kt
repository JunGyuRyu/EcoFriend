package my.application.ecofriend.datas

import java.io.Serializable

class PdfItem(
    var classification: String?,
    var item: String?,
    var standard: String?,
    var levy_amt: Long?,
    var count: Long?,
) : Serializable {
}
