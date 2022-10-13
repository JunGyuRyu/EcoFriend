package my.application.ecofriend.datas

import java.io.Serializable

class CheckedItem(
    // 클래스의 생성자에서 변수들을 나열해서 클래스가 가져야하는 정보 항목들로 설정
    val classification: String?,
    val dockey: String,
    val item: String?,
    val standard: String?,
    val levy_amt: Long?,
    var count: Long?
): Serializable {
}

