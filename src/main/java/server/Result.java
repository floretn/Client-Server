package server;

public class Result {
    public double rez;
    public long time;
    public int i;

    public Result(double rez, long time, int i) {
        this.rez = rez;
        this.time = time;
        this.i = i;
    }

    @Override
    public String toString() {
        return rez +
                " " + time +
                " " + i;
    }
}
