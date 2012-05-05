package util;

public class Score implements Comparable<Score> {
    private String name;
    private int score;

    public Score(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public int compareTo(Score s) {
        if (s.getScore() < getScore())
            return -1;
        else if (s.getScore() > getScore())
            return 1;
        else
            return 0;
    }
}