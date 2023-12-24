import org.apache.nifi.processor.io.OutputStreamCallback
import java.text.SimpleDateFormat
import java.util.Calendar

def startDate = Calendar.getInstance()
startDate.set(2020, Calendar.JANUARY, 1)
def endDate = Calendar.getInstance()
endDate.set(2023, Calendar.DECEMBER, 23)

def dateFormat = new SimpleDateFormat('yyyyMMdd')

flowFile = session.create()
flowFile = session.write(flowFile, new OutputStreamCallback() {
    @Override
    void process(OutputStream outputStream) throws IOException {
        Calendar current = startDate
        while (current.before(endDate) || current.equals(endDate)) {
            int year = current.get(Calendar.YEAR)
            int month = current.get(Calendar.MONTH) + 1
            int day = current.get(Calendar.DAY_OF_MONTH)
            int quarter = (month - 1) / 3 + 1

            int dayOfWeek = current.get(Calendar.DAY_OF_WEEK)
            boolean isWeekday = !(dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY)
            boolean isWeekend = !isWeekday
            boolean isHoliday = (month == 12 && day == 25)
            String holidayName = isHoliday ? 'Christmas' : ''

            String timeId = dateFormat.format(current.getTime())
            String csvLine = "${timeId},${timeId},${year},${month},${day},${quarter},${dayOfWeek},${isWeekday},${isWeekend},${isHoliday},\"${holidayName}\"\n"
            outputStream.write(csvLine.getBytes("UTF-8"))

            current.add(Calendar.DAY_OF_MONTH, 1)
        }
    }
})

session.transfer(flowFile, REL_SUCCESS)