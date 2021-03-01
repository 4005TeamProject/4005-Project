import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class Simulation {

    public Simulation(){
        initialize();
    }

    //The five queues for components
    //naming convention cXwX ==> cX = component type I.e. C1, C2 or C3. wX = workstation I.e. W1, W2 or W3.
    //each queue has a min capacity of 0 and max capacity of 2.
    private final int QUEUE_CAP = 2;
    private Queue<String> c1w1;
    private Queue<String> c1w2;
    private Queue<String> c1w3;
    private Queue<String> c2w2;
    private Queue<String> c3w3;

    //amounts of products p1, p2, and p3 produced.
    private int p1;
    private int p2;
    private int p3;

    //Filereaders for the time data files
    private BufferedReader servinsp23Reader;
    private BufferedReader servinsp22Reader;
    private BufferedReader servinsp1Reader;
    private BufferedReader ws1Reader;
    private BufferedReader ws2Reader;
    private BufferedReader ws3Reader;

    private double clock;


    private void initialize(){

        clock = 0; //start clock at 0

        boolean I1isBusy;
        boolean I2isBusy;
        double I2busyTime;
        double I1busyTime;
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
        initialize();
        //Simulation code goes here

        generateReport();

    }

    private void generateReport() {
    }

    // different events ALS1, ALS2, ELS1,ELS2,ALW1, ALW2, ALW3, ELW1, ELW2, ELW3, ES
    private void ScheduleEvent(SimEvent.eventType eType) throws IOException {
        Double timeElapsed = -1.0;
        switch (eType) {
//            case ALS1:
//                timeElapsed = 0.0;
//                break;
//            case ALS2:
//                timeElapsed = 0.0;
//                break;
            case ELS1:
                timeElapsed = Double.parseDouble(servinsp1Reader.readLine());
                break;
            case ELS2:
                timeElapsed = Double.parseDouble(servinsp22Reader.readLine());
                timeElapsed = Double.parseDouble(servinsp23Reader.readLine());
                break;
//            case ALW1:
//                timeElapsed = Double.parseDouble(servinsp23Reader.readLine());
//                break;
//            case ALW2:
//                break;
//            case ALW3:
//                break;
            case ELW1:
                timeElapsed = Double.parseDouble(ws1Reader.readLine());
                break;
            case ELW2:
                timeElapsed = Double.parseDouble(ws1Reader.readLine());
                break;
            case ELW3:
                timeElapsed = Double.parseDouble(ws1Reader.readLine());
                break;
            case ES:
                break;
        }
        SimEvent newEVT = new SimEvent(eType,clock+timeElapsed);
    }
}
