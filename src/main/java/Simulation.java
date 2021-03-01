import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class Simulation {

    //The five queues for components
    //naming convention cXwX ==> cX = component type I.e. C1, C2 or C3. wX = workstation I.e. W1, W2 or W3.
    //each queue has a min capacity of 0 and max capacity of 2.
    private final int QUEUE_CAP = 2;
    private Queue<String> c1w1;
    private Queue<String> c1w2;
    private Queue<String> c1w3;
    private Queue<String> c2w2;
    private Queue<String> c3w3;

    //Filereaders for the time data files
    private BufferedReader servinsp23Reader;
    private BufferedReader servinsp22Reader;
    private BufferedReader servinsp1Reader;
    private BufferedReader ws1Reader;
    private BufferedReader ws2Reader;
    private BufferedReader ws3Reader;


    private void initialize(){
        c1w1 = new PriorityBlockingQueue<String>(QUEUE_CAP);
        c1w2 = new PriorityBlockingQueue<String>(QUEUE_CAP);
        c1w3 = new PriorityBlockingQueue<String>(QUEUE_CAP);
        c2w2 = new PriorityBlockingQueue<String>(QUEUE_CAP);
        c3w3 = new PriorityBlockingQueue<String>(QUEUE_CAP);
        try {
            servinsp1Reader = new BufferedReader(new FileReader("servinsp1.txt"));
            servinsp22Reader = new BufferedReader(new FileReader("servinsp22.txt"));
            servinsp23Reader = new BufferedReader(new FileReader("servinsp23.txt"));
            ws1Reader = new BufferedReader(new FileReader("ws1.txt"));
            ws2Reader = new BufferedReader(new FileReader("ws2.txt"));
            ws3Reader = new BufferedReader(new FileReader("ws3.txt"));
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    public void run(){

    }
}
