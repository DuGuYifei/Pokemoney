def flowFile = session.create()

flowFile = session.write(flowFile, new OutputStreamCallback() {
    @Override
    void process(OutputStream outputStream) throws IOException {
        int start = 202001
        int end = 202312
        int current = start

//        outputStream.write("time_analysis_id,year,month,quarter\n".getBytes("UTF-8"))
        while (current <= end) {
            int year = current / 100
            int month = (current % 100)
            int quarter = (month - 1) / 3 + 1

            if (month <= 12 && month > 0) {
                String csvLine = "${current},${year},${month},${quarter}\n"
                outputStream.write(csvLine.getBytes("UTF-8"))
            }

            if (month == 12) {
                current += 89
            } else {
                current += 1
            }
        }
    }
})

REL_SUCCESS << flowFile