import java.text.SimpleDateFormat

def flowFile = session.create()

flowFile = session.write(flowFile, new OutputStreamCallback() {
    @Override
    void process(OutputStream outputStream) throws IOException {
        Date date = new Date(System.currentTimeMillis())
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM")
        int current = Integer.parseInt(dateFormat.format(date))
        int year = current / 100
        int month = (current % 100)
        int quarter = (month - 1) / 3 + 1
        month += 1
        if (month > 12) {
            month = 1
            year += 1
            quarter = 1
            current = year * 100 + month
        }
        String csvLine = "${current},${year},${month},${quarter}\n"
        outputStream.write(csvLine.getBytes("UTF-8"))
    }
})

REL_SUCCESS << flowFile