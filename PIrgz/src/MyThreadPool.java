import java.lang.reflect.Array;

public class MyThreadPool {
    private final MyThread[] pool;

    private volatile Integer size;
    private volatile Integer countCreateObject;
    private boolean finish;
    private static MyThreadPool instance;

    private MyThreadPool(int capacity){
        size = 0;
        finish = false;
        countCreateObject = 0;
        pool = (MyThread[]) Array.newInstance(MyThread.class, capacity);
    }

    public static synchronized MyThreadPool getInstance(int capacity){
        if (instance == null) {
            instance = new MyThreadPool(capacity);
        }
        return instance;
    }

    public boolean getFinish(){
        return finish;
    }

    public void setFinish(boolean finish){
        this.finish = finish;
    }

    public int getSize() {
        synchronized (size) {
            return size;
        }
    }

    public int getCapacity(){
        synchronized (pool) {
            return pool.length;
        }
    }

    public int getCountCreateObject(){
        synchronized (countCreateObject) {
            return countCreateObject;
        }
    }

//    public void setCapacity(int newValue) throws IllegalAccessException {
//        if (newValue<=0){
//            String msg = "Capacity must be greater than zero:" + newValue;
//            throw new IllegalAccessException(msg);
//        }
//        synchronized (pool){
//            MyThread[] newPool = new MyThread[newValue];
//            System.arraycopy(pool, 0, newPool, 0, newValue);
//            pool = newPool;
//        }
//    }

    public MyThread getObject(DoInThread doInThread) {
        synchronized (pool){
            if (size>0){
                return removeObject();
            } else if ( getCountCreateObject() < getCapacity()){
                return createObject(doInThread);
            } else {
                return null;
            }
        }
    }

    public MyThread waitForObject(DoInThread doInThread){
        synchronized (pool){
            MyThread obj = getObject(doInThread);
            if (obj == null){
                do {
                    //ожидает извещение о том, чтообект был возвращен назад в пул
                    try {
                        pool.wait();
                        //System.out.println("жду...");
                    } catch (InterruptedException ex) {
                        ex.getMessage();
                    }
                } while (size <= 0);
                obj = removeObject();
                //System.out.println("obj " + obj.thread.getName());
            }
            synchronized (obj) {
                obj.notify();
            }
            return obj;
        }
    }

    private MyThread createObject(DoInThread doInThread) {
        synchronized (pool) {
            MyThread newObject = new MyThread(this, doInThread);
            //System.out.println("Создание потока " + newObject.thread.getName());

            synchronized (countCreateObject) {
                countCreateObject++;
            }

            synchronized (size){
                size++;
            }

            return newObject;
        }
    }

    private MyThread removeObject() {
        synchronized (pool) {
            synchronized (size){
                size--;
            }
            return pool[size];
        }
    }

    public void release(MyThread obj){
        //System.out.println("Освободился " + obj.thread.getName());
        if (obj == null){
            throw new NullPointerException();
        }
        synchronized (pool){
            if (size < getCapacity()) {
                pool[size] = obj;
                synchronized (size){
                    size++;
                }
                //оповещаем ожидающий поток о том, что мы поместили объект в пул
                pool.notify();
            }
        }
    }
}

