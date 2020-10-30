package server;

import java.util.function.BiFunction;

public class Application {

    public int a1;
    public int a2;
    public double rez;
    public long time;
    public BiFunction<Integer, Integer, Double> func;

    public Application(int a1, int a2, double rez, long time, BiFunction<Integer, Integer, Double> func){
        this.a1 = a1;
        this.a2 = a2;
        this.rez = rez;
        this.time = time;
        this.func = func;
    }

    public static Application fromString(String app) {
        String[] strings = app.split(" ");
        BiFunction<Integer, Integer, Double> func = Application::amount;
        if (Integer.parseInt(strings[4]) == 1){
            func = Application::multiplication;
        }
        if (Integer.parseInt(strings[4]) == 2){
            func = Application::subtraction;
        }
        if (Integer.parseInt(strings[4]) == 3){
            func = Application::division;
        }
        return new Application(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]),
                Double.parseDouble(strings[2]), Long.parseLong(strings[3]), func);
    }

    public static double amount(int a1, int a2){
        return a1 + a2;
    }

    public static double multiplication(int a1, int a2){
        return a1 * a2;
    }

    public static double subtraction(int a1, int a2){
        return a1 - a2;
    }

    public static double division(int a1, int a2){
        if (a2 != 0) {
            return a1 / a2;
        }
        return 0;
    }
}
