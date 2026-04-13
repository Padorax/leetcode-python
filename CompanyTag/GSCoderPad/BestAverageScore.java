package CompanyTag.GSCoderPad;

import java.util.HashMap;
import java.util.LinkedList;

public class BestAverageScore {
    public static int bestAverageScore(String[][] scores) {
        if (scores.length == 0) return 0; //!!! <- this line
        
        HashMap<String, LinkedList<Integer>> studentScores = new HashMap<>();
        for (String[] score : scores) {
            if (score.length != 2) {
                return 0;
            }
            String student = score[0];
            Integer grade = Integer.parseInt(score[1]);
            
            studentScores.putIfAbsent(student, new LinkedList<Integer>());
            studentScores.get(student).add(grade);
        }

        double maxScore = -Integer.MAX_VALUE;
        for (LinkedList<Integer> grades : studentScores.values()) {
            int sum = 0;
            for (int grade : grades) {
                sum += grade;
            }
            double avgScore = sum / (double) grades.size();
            maxScore = avgScore > maxScore ? avgScore : maxScore;
        }
        return (int) Math.floor(maxScore);
    }
}