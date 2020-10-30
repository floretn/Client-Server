package client;

import java.io.*;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Client {

    private static Socket clientSocket;
    private static BufferedReader in;
    private static BufferedWriter out;

    public static int intInput(){
        int rez;
        String mistake = "";
        Scanner sc = new Scanner(System.in);
        do {
            try {
                rez = sc.nextInt();
                break;
            } catch (InputMismatchException nfe) {
                mistake = sc.nextLine();
            }
            System.out.print("Некорректный ввод " + mistake +
                    ", ожидается целое число, пожалуйста, повторите: ");
        } while(true);
        return rez;
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        System.out.print("Введите количество генераторов: ");
        int m = intInput();
        while (m < 0){
            System.out.print("Введите неотрицательное число: ");
            m = intInput();
        }
        Generator[] generators = new Generator[m];
        Thread[] threads = new Thread[m];
        try {
            try {
                clientSocket = new Socket("localhost", 4200);
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                System.out.print("Введите количество потоков: ");
                int n = intInput();
                while (n < 1){
                    System.out.print("Введите число больше 0: ");
                    n = intInput();
                }
                String number = Integer.toString(n);
                out.write(number + "\n");
                out.flush();
                String serverWord = in.readLine();
                System.out.println("Сервер: " + serverWord);
                serverWord = in.readLine();
                System.out.println("Сервер: " + serverWord);
                for (int i = 0; i < m; i++) {
                    generators[i] = new Generator(client, clientSocket);
                    threads[i] = new Thread(generators[i]);
                    threads[i].start();
                }

                for (int i = 0; i < m; i++){
                    while (threads[i].getState() != Thread.State.TERMINATED){
                        Thread.sleep(100);
                    }
                }
                out.write("stop\n");
                out.flush();

                serverWord = in.readLine();
                System.out.println("Сервер: " + serverWord);
                out.write("Готов принимать результаты...\n");
                out.flush();
                System.out.println("Ваши результаты:");
                serverWord = in.readLine();
                while (!serverWord.equals("stop")) {
                    Result rez = Result.fromString(serverWord);
                    System.out.println(rez);
                    out.write("Результат получил...\n");
                    out.flush();
                    serverWord = in.readLine();
                }
                serverWord = in.readLine();
                System.out.println("Сервер: " + serverWord);
            } finally {
                System.out.println("Клиент был закрыт...");
                if (clientSocket != null) {
                    clientSocket.close();
                    in.close();
                    out.close();
                }
            }
        } catch (IOException | InterruptedException e) {
            System.err.println(e);
        }
    }
}
