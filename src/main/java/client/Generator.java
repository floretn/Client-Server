package client;

import java.io.*;
import java.util.Random;
import java.util.function.BiFunction;
import java.net.Socket;

public class Generator extends Thread implements Runnable {

    private static int iGen = 1;
    int iThis;
    final Client client;
    final Socket socket;
    private static BufferedReader in; // поток чтения из сокета
    private static BufferedWriter out; // поток записи в сокет

    public Generator(Client client, Socket socket) throws IOException {
        this.client = client;
        this.socket = socket;
        iThis = iGen;
        iGen++;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public void run(){
        int i = 1;
        Application app;
        Random rnd = new Random();
        String word;
        while(i < 11){
            int a1 = (rnd.nextInt(1000) + i)/i + rnd.nextInt(1000)*i;
            int a2 = (rnd.nextInt(100) + i*i)/(i + i) + rnd.nextInt(1000)/i;
            int numFunc = rnd.nextInt(4);

            double rez = 0.0;
            long time1 = 0;
            long time2 = 0;
            BiFunction<Integer, Integer, Double> func = Generator::amount;

            if(numFunc == 0){
                time1 = System.nanoTime();
                rez = func.apply(a1, a2);
                time2 = System.nanoTime();
            }
            if(numFunc == 1){
                func = Generator::multiplication;
                time1 = System.nanoTime();
                rez = func.apply(a1, a2);
                time2 = System.nanoTime();
            }
            if(numFunc == 2){
                func = Generator::subtraction;
                time1 = System.nanoTime();
                rez = func.apply(a1, a2);
                time2 = System.nanoTime();
            }
            if(numFunc == 3){
                func = Generator::division;
                time1 = System.nanoTime();
                rez = func.apply(a1, a2);
                time2 = System.nanoTime();
            }
            long time = time2 - time1;

            app = new Application(a1, a2, rez, time, func);
            synchronized (socket) {
                try {
                    System.out.println("Генератор" + iThis + ": " + app.toString(numFunc));
                    out.write(app.toString(numFunc) + "\n");
                    out.flush();
                    word = in.readLine();
                    System.out.println("Сервер: " + word);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            i++;
        }
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
