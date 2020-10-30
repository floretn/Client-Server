package client;

public class Result {
    public double rez;
    public long time;
    public int i;

    public Result(double rez, long time, int i) {
        this.rez = rez;
        this.time = time;
        this.i = i;
    }

    public static Result fromString(String word){
        String[] strings = word.split(" ");
        return new Result(Double.parseDouble(strings[0]), Long.parseLong(strings[1]), Integer.parseInt(strings[2]));
    }

    @Override
    public String toString() {
        return  "Результат = " + rez +
                ", время обработки = " + time +
                ", обработка выполнена Потоком №" + i;
    }
}
