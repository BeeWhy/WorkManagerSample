package beewhy.yanina.piper

import java.text.SimpleDateFormat
import java.util.*

class DateUtils {
    companion object {
        const val APP_FORMAT = "MM/dd/yyyy hh:mm:ss aa"
        fun generateDateString(): String {
            val dateFormat = SimpleDateFormat(APP_FORMAT)
            val date = Date()
            return dateFormat.format(date)
        }

        fun convertDate(stringDate: String): Long {
            val dateFormat = SimpleDateFormat(APP_FORMAT)
            return dateFormat.parse(stringDate).time
        }
    }
}