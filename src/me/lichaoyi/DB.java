package me.lichaoyi;

import java.sql.*;

public class DB {

    static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    static final String DB_URL = "jdbc:mariadb://127.0.0.1:32771/main";

    static final String USER = "root";
    static final String PASS = "admin888";

    static Connection conn = null;

    static void connect() {
        try {
            Class.forName(JDBC_DRIVER);

            System.out.println("Connecting to a selected database...");
            conn = DriverManager.getConnection(
                    DB_URL, USER, PASS);
            System.out.println("Connected database successfully...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void saveMaster(Master master) {
        try {
            String sql = "INSERT INTO master " +
                    "(`RowNumber`, SubmissionID, ItemID, AnonymizedStudentID, DateTimeResponseSubmitted, DateTimeFinalScoreGiven, " +
                    "FinalScorePointsEarned, FinalScorePointsPossible, FeedbackOnPeerAssessments) VALUES " +
                    "(?,?,?,?,?,?,?,?,?); ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, master.rowNumber);
            ps.setString(2, master.submissionID);
            ps.setString(3, master.itemID);
            ps.setString(4, master.anonymizedStudentID);
            ps.setString(5, master.dateTimeResponseSubmitted);
            ps.setString(6, master.dateTimeFinalScoreGiven);
            if(master.finalScorePointsEarned != null) {
                ps.setInt(7, master.finalScorePointsEarned);
            } else {
                ps.setNull(7, Types.INTEGER);
            }
            if(master.finalScorePointsPossible != null) {
                ps.setInt(8, master.finalScorePointsPossible);
            } else {
                ps.setNull(8, Types.INTEGER);
            }
            ps.setString(9, master.feedbackOnPeerAssessments);
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void saveAssessmentDetail(AssessmentDetail assessmentDetail) {
        try {
            String sql = "INSERT INTO assessment_details " +
                    "(SubmissionID, AssessmentNumber, ScoredAt, `Type`, ScorerID, OverallFeedback) VALUES " +
                    "(?,?,?,?,?,?); ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, assessmentDetail.submissionID);
            ps.setInt(2, assessmentDetail.assessmentNumber);
            ps.setString(3, assessmentDetail.scoredAt);
            ps.setString(4, assessmentDetail.type);
            ps.setString(5, assessmentDetail.scorerID);
            ps.setString(6, assessmentDetail.overallFeedback);

            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void saveAssessmentScore(AssessmentScore assessmentScore) {
        try {
            String sql = "INSERT INTO assessment_scores " +
                    "(SubmissionID, AssessmentNumber, `FluencyOfPronunciation(JA)`, `FluencyOfPronunciation(EN)`, Feedback) VALUES " +
                    "(?,?,?,?,?); ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, assessmentScore.submissionID);
            ps.setInt(2, assessmentScore.assessmentNumber);
            ps.setString(3, assessmentScore.fluencyOfPronunciationJA);
            ps.setString(4, assessmentScore.fluencyOfPronunciationEN);
            ps.setString(5, assessmentScore.feedback);

            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    static void rollback() {
        try {
            conn.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void disconnect() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
