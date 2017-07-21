package me.lichaoyi;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

public class MasterGenerator {
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
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Master master = new Master();

                int rowNumber = row.getRowNum();
                master.rowNumber = rowNumber;

                /* Submission ID */
                String submissionID = row.getCell(0).getStringCellValue();
                master.submissionID = submissionID;

                /* Item ID */
                Cell itemIDCell = row.getCell(1);
                itemIDCell.setCellType(CellType.STRING);
                String itemID = itemIDCell.getStringCellValue();
                master.itemID = itemID;

                /* Anonymized Student ID */
                String anoymizedStudentID = row.getCell(2).getStringCellValue();
                master.anonymizedStudentID = anoymizedStudentID;

                /* Date Time Response Submitted */
                String dateTimeResponseSubmittedString = row.getCell(3).getStringCellValue();
                master.dateTimeResponseSubmitted = dateTimeResponseSubmittedString.substring(0, 19);

                /* Date Time Final Score Given */
                String dateTimeFinalScoreGivenString = row.getCell(7).getStringCellValue();
                if(!dateTimeFinalScoreGivenString.isEmpty()) {
                    master.dateTimeFinalScoreGiven = dateTimeFinalScoreGivenString.substring(0, 19);
                } else {
                    master.dateTimeFinalScoreGiven = null;
                }

                /* Final Score Points Earned */
                int finalScorePointsEarned = (int)row.getCell(8).getNumericCellValue();
                if(finalScorePointsEarned != 0) {
                    master.finalScorePointsEarned = finalScorePointsEarned;
                } else {
                    master.finalScorePointsEarned = null;
                }

                /* Final Score Points Possible */
                int finalScorePointsPossible = (int)row.getCell(9).getNumericCellValue();
                if(finalScorePointsPossible != 0) {
                    master.finalScorePointsPossible = finalScorePointsPossible;
                } else {
                    master.finalScorePointsPossible = null;
                }

                /* FeedbackOnPeerAssessments */
                String feedbackOnPeerAssessments = row.getCell(10).getStringCellValue();
                if(!feedbackOnPeerAssessments.isEmpty()) {
                    master.feedbackOnPeerAssessments = feedbackOnPeerAssessments;
                } else {
                    master.feedbackOnPeerAssessments = null;
                }

                DB.saveMaster(master);
            }

        } catch (Exception e) {
            e.printStackTrace();
            DB.rollback();
        } finally {
            DB.disconnect();
        }
    }
}
