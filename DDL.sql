CREATE TABLE `assessment_details` (
  `SubmissionID` varchar(64) NOT NULL,
  `AssessmentNumber` int(8) NOT NULL,
  `ScoredAt` datetime DEFAULT NULL,
  `Type` enum('PE','ST') DEFAULT NULL,
  `ScorerID` varchar(64) DEFAULT NULL,
  `OverallFeedback` varchar(4096) DEFAULT NULL,
  PRIMARY KEY (`SubmissionID`,`AssessmentNumber`),
  CONSTRAINT `FK_SubmissionID` FOREIGN KEY (`SubmissionID`) REFERENCES `master` (`SubmissionID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `assessment_scores` (
  `SubmissionID` varchar(64) NOT NULL,
  `AssessmentNumber` int(8) NOT NULL,
  `FluencyOfPronunciation(JA)` varchar(32) NOT NULL,
  `FluencyOfPronunciation(EN)` varchar(32) NOT NULL,
  `Feedback` varchar(4096) DEFAULT NULL,
  PRIMARY KEY (`SubmissionID`,`AssessmentNumber`),
  CONSTRAINT `FK_SubmissionID2` FOREIGN KEY (`SubmissionID`) REFERENCES `master` (`SubmissionID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `master` (
  `RowNumber` int(8) NOT NULL,
  `SubmissionID` varchar(64) NOT NULL,
  `ItemID` varchar(64) NOT NULL,
  `AnonymizedStudentID` varchar(64) NOT NULL,
  `DateTimeResponseSubmitted` datetime NOT NULL,
  `DateTimeFinalScoreGiven` datetime DEFAULT NULL,
  `FinalScorePointsEarned` smallint(2) DEFAULT NULL,
  `FinalScorePointsPossible` smallint(2) DEFAULT NULL,
  `FeedbackOnPeerAssessments` varchar(4096) DEFAULT NULL,
  PRIMARY KEY (`RowNumber`) USING BTREE,
  UNIQUE KEY `idx_submissionID` (`SubmissionID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;