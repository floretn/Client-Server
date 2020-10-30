package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

public class Server {

    public static ArrayList<Application> apps = new ArrayList<>();
    public static LinkedList<ServerSomething> serverList = new LinkedList<>();

    public static void main(String[] args) throws IOException {
        try (ServerSocket server = new ServerSocket(4200)) {
            System.out.println("Сервер запущен! ");
            while (true) {

                Socket socket = server.accept();
                System.out.println("Словил соединение...");
                try {
                    serverList.add(new ServerSomething(socket));
                    serverList.get(serverList.size() - 1).i = ServerSomething.iClient;
                    ServerSomething.iClient++;
                    serverList.get(serverList.size() - 1).start();
                    System.out.println("Соединение добавил...");
                } catch (IOException e) {
                    socket.close();
                    System.out.println("Соединение не добавил:(");
                }
            }
        }
    }

    static class ServerSomething extends Thread {

        private final BufferedReader in;
        private final BufferedWriter out;
        private final Queue<Application> queue;
        static int iClient = 1;
        int i;

        public ServerSomething(Socket socket) throws IOException {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            queue = new Queue<>();
        }

        @Override
        public void run() {
            String word;
            System.out.println("Работа с Клиентом №" + i + " начата...");
            try {
                word = in.readLine();
                int number = Integer.parseInt(word);
                System.out.println("Клиент №" + i + " заказал n = " + word + " рабочих потоков");
                out.write("Вы заказали n = " + word + " рабочих потоков. Обрабатываем, ждите...\n");
                out.flush();
                ThreadsPool tp = new ThreadsPool(number, queue);
                Thread t = new Thread(tp);
                t.start();
                out.write("Готов к приёму и обработке заявок...\n");
                out.flush();
                System.out.println("Начинаю приём и обработку заявок...");
                while (true){
                    word = in.readLine();
                    if (word.equals("stop")){
                        break;
                    }
                    System.out.println("Клиент №" + i + ": " + word);
                    queue.add(Application.fromString(word));
                    out.write("Заявка принята в обработку...\n");
                    out.flush();
                }

                while (queue.getCounter() != 0){
                    Thread.sleep(10);
                }

                out.write("Обработку закончил, ожидаю готовность клиента к передаче результатов...\n");
                out.flush();
                System.out.println("Приём и обработка заявок завершена, ожидаю готовность клиента к приёму результатов...");
                word = in.readLine();
                System.out.println("Клиент №" + i + ": " + word);

                while (tp.queueRez.hasNext()){
                    out.write(tp.queueRez.next().toString() + "\n");
                    out.flush();
                    word = in.readLine();
                    System.out.println("Клиент №" + i + ": " + word);
                }
                out.write("stop\n");
                out.flush();
                out.write("Обработка закончена, обрываю соединение...\n");
                out.flush();
            } catch (IOException | InterruptedException ignored) {
            }finally {
                try {
                    in.close();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Работа с Клиентом №" + i + " завершена...");
        }
    }
}
