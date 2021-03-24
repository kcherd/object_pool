import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class main {
    public static void main(String[] args) {
//        MyThreadPool pool = MyThreadPool.getInstance(5);
//        System.out.println(System.currentTimeMillis());
//        for(int i = 0; i < 20; i++){
//            pool.waitForObject(new DoInThread() {
//                @Override
//                public void toDo() {
//                    System.out.println("Do in thread");
//                }
//
//                @Override
//                public void doAfter() {
//                    System.out.println("Do after");
//                }
//            });
//            //System.out.println("thread in main = " + thread.thread.getName());
//        }
//        pool.setFinish(true);


        System.out.println(System.currentTimeMillis());
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(20);
        int N = 20;
        final int[] l = new int[1];
        for (int i = 0; i < N; i++) {
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    l[0] = 9*666+5-4;
                    //System.out.println(System.currentTimeMillis());
                }
            });
        }
        System.out.println(System.currentTimeMillis());

        for (int i = 0; i < N; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    l[0] = 9*666+5-4;
                    //System.out.println("Do in thread");
                }
            });
            thread.start();
        }

        System.out.println(System.currentTimeMillis());
    }
}
