public class MyThread1 implements Runnable{
    Thread thread;
    DoInThread doInThread;

    MyThread1(DoInThread doInThread){
        try {
            thread.sleep(2);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        this.doInThread = doInThread;
        thread= new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        doInThread.toDo();
        try {
            thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(System.currentTimeMillis());
    }
}