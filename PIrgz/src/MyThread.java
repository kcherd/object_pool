public class MyThread implements Runnable{
    Thread thread;
    public MyThreadPool threadPool;
    DoInThread doInThread;

    MyThread(MyThreadPool pool, DoInThread doInThread){
        threadPool = pool;
        this.doInThread = doInThread;
        thread= new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while (!threadPool.getFinish()) {
            doInThread.toDo();
            try {
                thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            doInThread.doAfter();
            System.out.println(System.currentTimeMillis());
            threadPool.release(this);
            synchronized (this){
                try {
                    //System.out.println("wait in thread " + this.thread.getName());
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}