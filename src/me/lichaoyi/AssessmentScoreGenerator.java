package me.lichaoyi;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AssessmentScoreGenerator {
    public static void main(String args[]) {
        try {
            File excel = new File("in.xlsx");

            FileInputStream fis = new FileInputStream(excel);

            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next();
            rowIterator.next();
            DB.connect();

            Pattern pattern = Pattern.compile("^(\\d*)\\s*.*(?:(Fluency of Pronunciation)|(Pronunciation Accuracy)):\\s*([^\\r\\n\\t\\f 　]*)( |　)((\\S| )*)(\\s*.*feedback:\\s*((\\S|\\s)*))?$");

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                /* SubmissionID */
                String submissionID = row.getCell(0).getStringCellValue();

                /* 整体 */
                String fullString = row.getCell(5).getStringCellValue();

                /* 按AssessmentNumber切分 */
                String slices[] = fullString.split("Assessment #");
                for (int i = 1; i < slices.length; i++) {
                    String slice = slices[i];

                    /* Submission ID (FK) */
                    AssessmentScore score = new AssessmentScore();
                    score.submissionID = submissionID;

                    Matcher matcher = pattern.matcher(slice);
                    if (matcher.find()) {
                        /* AssessmentNumber */
                        String assesmentNumber = matcher.group(1);
                        score.assessmentNumber = Integer.valueOf(assesmentNumber);

                        /* fluencyOfPronunciationJA */
                        String fluencyOfPronunciationJA = matcher.group(4);
                        score.fluencyOfPronunciationJA = fluencyOfPronunciationJA;

                        /* fluencyOfPronunciationEN */
                        String fluencyOfPronunciationEN = matcher.group(6);
                        score.fluencyOfPronunciationEN = fluencyOfPronunciationEN;

                        /* feedback */
                        if (matcher.groupCount() > 7) {
                            String feedback = matcher.group(9);
                            score.feedback = feedback;
                        }
                    } else {
                        System.out.println(slice);
                        System.out.println("error: mismatch at line " + String.valueOf(row.getRowNum()) + " slice " + String.valueOf(i));
                        return;
                    }

                    /* Save DB */
                    DB.saveAssessmentScore(score);
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
