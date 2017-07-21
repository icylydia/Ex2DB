SELECT M.SubmissionID,
M.ItemID,
M.AnonymizedStudentID,
DATE_FORMAT(M.DateTimeResponseSubmitted,'%Y-%m-%d %T+00:00') AS DateTimeResponseSubmitted,
DATE_FORMAT(D.ScoredAt,'%Y-%m-%d %T+00:00') AS ScoredAt,
D.Type,
D.ScorerID,
D.OverallFeedback,
D.`FluencyOfPronunciation(JA)`,
D.`FluencyOfPronunciation(EN)`,
D.Feedback,
DATE_FORMAT(M.DateTimeFinalScoreGiven,'%Y-%m-%d %T+00:00') AS DateTimeFinalScoreGiven,
M.FinalScorePointsEarned,
M.FinalScorePointsPossible,
M.FeedbackOnPeerAssessments
FROM `master` AS M
LEFT JOIN (
SELECT D1.SubmissionID AS SubmissionID,
D1.AssessmentNumber AS AssessmentNumber,
D1.ScorerID AS ScorerID,
D1.Type AS Type,
D1.ScoredAt AS ScoredAt,
D1.OverallFeedback AS OverallFeedback,
D2.`FluencyOfPronunciation(JA)` AS `FluencyOfPronunciation(JA)`,
D2.`FluencyOfPronunciation(EN)` AS `FluencyOfPronunciation(EN)`,
D2.Feedback AS Feedback
FROM assessment_details AS D1
INNER JOIN assessment_scores AS D2
ON D1.SubmissionID = D2.SubmissionID
AND D1.AssessmentNumber = D2.AssessmentNumber
) AS D
ON M.SubmissionID = D.SubmissionID;