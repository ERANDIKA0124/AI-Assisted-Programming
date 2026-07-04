public class StatisticsSummary {
    private final int totalStudents;
    private final double classAverage;
    private final double highestAverage;
    private final double lowestAverage;
    private final double passRate;

    public StatisticsSummary(int totalStudents, double classAverage, double highestAverage, double lowestAverage, double passRate) {
        this.totalStudents = totalStudents;
        this.classAverage = classAverage;
        this.highestAverage = highestAverage;
        this.lowestAverage = lowestAverage;
        this.passRate = passRate;
    }

    public int getTotalStudents() {
        return totalStudents;
    }

    public double getClassAverage() {
        return classAverage;
    }

    public double getHighestAverage() {
        return highestAverage;
    }

    public double getLowestAverage() {
        return lowestAverage;
    }

    public double getPassRate() {
        return passRate;
    }
}
