package me.lichaoyi;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AssessmentDetailGenerator {
    public static void main(String args[]) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("Z"));

            File excel = new File("in.xlsx");

            FileInputStream fis = new FileInputStream(excel);

            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next();
            rowIterator.next();
            DB.connect();

            Pattern pattern = Pattern.compile(	"#(\\d*)\\s*.*scored_at:\\s*(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\+00:00)\\s*.*type:\\s*(PE|ST)\\s*.*scorer_id:\\s*(.*)(\\s*.*overall_feedback:\\s*((.|\\s)*))?$");

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                /* SubmissionID */
                String submissionID = row.getCell(0).getStringCellValue();

                /* 整体 */
                String fullString = row.getCell(4).getStringCellValue();

                /* 按AssessmentNumber切分 */
                String slices[] = fullString.split("Assessment");
                for (int i = 1; i < slices.length; i++) {
                    String slice = slices[i];

                    /* Submission ID (FK) */
                    AssessmentDetail detail = new AssessmentDetail();
                    detail.submissionID = submissionID;

                    Matcher matcher = pattern.matcher(slice);
                    if (matcher.find()) {
                        /* AssessmentNumber */
                        String assesmentNumber = matcher.group(1);
                        detail.assessmentNumber = Integer.valueOf(assesmentNumber);

                        /* Scored at */
                        String scoredAt = matcher.group(2).substring(0, 19);
                        detail.scoredAt = scoredAt;

                        /* Type */
                        String type = matcher.group(3);
                        detail.type = type;

                        /* Scorer ID */
                        String scorerID = matcher.group(4);
                        detail.scorerID = scorerID;

                        /* Overall feedback */
                        if (matcher.groupCount() > 5) {
                            String overallFeedback = matcher.group(6);
                            detail.overallFeedback = overallFeedback;
                        }
                    } else {
                        System.out.println(slice);
                        System.out.println("error: mismatch at line " + String.valueOf(row.getRowNum()) + " slice " + String.valueOf(i));
                        return;
                    }

                    /* Save DB */
                    DB.saveAssessmentDetail(detail);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            DB.rollback();
        } finally {
            DB.disconnect();
        }
    }
}
