import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class Simulation {

    private final int QUEUE_CAP = 2;
    private Queue<String> c1w1;
    private Queue<String> c1w2;
    private Queue<String> c1w3;
    private Queue<String> c2w2;
    private Queue<String> c3w3;



    private void initialize(){
        c1w1 = new PriorityBlockingQueue<String>(QUEUE_CAP);
        c1w2 = new PriorityBlockingQueue<String>(QUEUE_CAP);
        c1w3 = new PriorityBlockingQueue<String>(QUEUE_CAP);
        c2w2 = new PriorityBlockingQueue<String>(QUEUE_CAP);
        c3w3 = new PriorityBlockingQueue<String>(QUEUE_CAP);


    }

    public void run(){

    }
}
