package server;

public class ThreadsPool implements Runnable{

    MyThread[] myThreads;
    Thread[] threads;
    final Queue<Application> queue;
    final Queue<Result> queueRez;
    boolean check = true;

    public ThreadsPool(int n, Queue<Application> queue){
        myThreads = new MyThread[n];
        threads = new Thread[n];
        for(int i = 0; i < n; i++){
            myThreads[i] = new MyThread(i + 1);
            threads[i] = new Thread(myThreads[i]);
        }
        this.queue = queue;
        queueRez = new Queue<>();
    }

    public void run(){
        for(Thread thread : threads){
            thread.start();
        }
    }

    private class MyThread implements Runnable{

        int i;

        public MyThread(int i) {
            this.i = i;
        }

        public void run(){

            System.out.println("Поток " + i + " запущен");
            while (check) {
                synchronized (queue) {
                    if (queue.hasNext()) {
                        Application app = queue.next();
                        queueRez.add(new Result(app.rez, app.time, i));
                        try {
                            java.lang.Thread.sleep(app.time / 1000000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        }
    }
}
