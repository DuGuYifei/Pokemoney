import org.apache.nifi.processor.io.OutputStreamCallback
import java.text.SimpleDateFormat
import java.util.Calendar

flowFile = session.create()
flowFile = session.write(flowFile, new OutputStreamCallback() {
    @Override
    void process(OutputStream outputStream) throws IOException {
        Calendar tomorrow = Calendar.getInstance()
        tomorrow.add(Calendar.DAY_OF_MONTH, 1)

        int year = tomorrow.get(Calendar.YEAR)
        int month = tomorrow.get(Calendar.MONTH) + 1 // Calendar.MONTH 是从 0 开始的
        int day = tomorrow.get(Calendar.DAY_OF_MONTH)
        int quarter = (month - 1) / 3 + 1

        def dateFormat = new SimpleDateFormat('yyyyMMdd')
        String timeId = dateFormat.format(tomorrow.getTime())

        int dayOfWeek = tomorrow.get(Calendar.DAY_OF_WEEK)
        boolean isWeekday = !(dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY)
        boolean isWeekend = !isWeekday
        boolean isHoliday = (month == 12 && day == 25) // 假设12月25日是假期
        String holidayName = isHoliday ? 'Christmas' : ''

        String csvLine = "${timeId},${timeId},${year},${month},${day},${quarter},${dayOfWeek},${isWeekday},${isWeekend},${isHoliday},\"${holidayName}\"\n"
        outputStream.write(csvLine.getBytes("UTF-8"))
    }
})

session.transfer(flowFile, REL_SUCCESS)
