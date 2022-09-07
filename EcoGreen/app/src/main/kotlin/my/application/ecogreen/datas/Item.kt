package my.application.ecogreen.datas

import java.io.Serializable

// ListView가 뿌려줄 데이터들을 묶어서 표현하는 데이터 클래스 생성
class Item(
    // 클래스의 생성자에서 변수들을 나열해서 클래스가 가져야하는 정보 항목들로 설정
    var checked: Boolean,
    val classification: String?,
    val dockey: String,
    val item: String?,
    val standard: String?,
    val levy_amt: Long?,
    ): Serializable {
}


