import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.PriorityBlockingQueue;

//This class represents the simulation model
public class Simulation {

    private static int clock, workingHours, currentDay, simDay;

    //Future Event List
    private static Queue<SimEvent> FEL;

    //The five queues for components
    //naming convention cXwX ==> cX = component type I.e. C1, C2 or C3. wX = workstation I.e. W1, W2 or W3.
    //each queue has a min capacity of 0 and max capacity of 2.
    //private final int QUEUE_CAP = 2;
    private static Queue<Component> c1w1, c1w2, c1w3, c2w2, c3w3;

    //Rng for time taken by inspection and workstations
    private static Random RNGInsp1, RNGInsp2, RNGWork1, RNGWork2, RNGWork3 ;

    //Last time inspectors/workstations were idle.
    private static int lastI1Idle, lastI2idle, lastW1Idle, lastW2Idle, lastW3Idle;

    //inspector/workstation is busy or not
    private static boolean isI1busy, isI2busy, isW1busy, isW2busy, isW3busy;

    //Simulation inputs + any function used to get these random variables.
    private static double[][] ITD = {}; //Inspection times go here (minutes, probability)
    private static double[][] IWD = {}; //Workstation times go here

    //Variables for the required metrics, statistics, and counters (B = busy, U = util)
    private static double BI1, BI2, BW1, BW2, BW3, UI1, UI2, UW1, UW2, UW3, p1, p2, p3;

    //Filereaders for the time data files
    private static BufferedReader servInsp23Reader;
    private static BufferedReader servInsp22Reader;
    private static BufferedReader servInsp1Reader;
    private static BufferedReader ws1Reader;
    private static BufferedReader ws2Reader;
    private static BufferedReader ws3Reader;


    public static void main(String[] args) {
        clock = 0; //start clock at 0
        workingHours=0;
        currentDay=1;
        simDay=7; //simulate 1 week
        lastI1Idle=0;
        lastI2idle=0;
        lastW1Idle=0;
        lastW2Idle=0;
        lastW3Idle=0;
        isI1busy=false;
        isI2busy=false;
        isW1busy=false;
        isW2busy=false;
        isW3busy=false;
        RNGInsp1= new Random();
        RNGInsp2= new Random();
        RNGWork1= new Random();
        RNGWork2= new Random();
        RNGWork3= new Random();
        FEL = new PriorityQueue<>();
        c1w1 = new LinkedList<>();
        c1w2 = new LinkedList<>();
        c1w3 = new LinkedList<>();
        c2w2 = new LinkedList<>();
        c3w3 = new LinkedList<>();
        BI1 = 0;
        BI2 = 0;
        BW1 = 0;
        BW2 = 0;
        BW3 = 0;
        UI1 = 0;
        UI2 = 0;
        UW1 = 0;
        UW2 = 0;
        UW3 = 0;
        p1 = 0;
        p2 = 0;
        p3 = 0;

        try {
            servInsp1Reader = new BufferedReader(new FileReader("servinsp1.txt"));
            servInsp22Reader = new BufferedReader(new FileReader("servinsp22.txt"));
            servInsp23Reader = new BufferedReader(new FileReader("servinsp23.txt"));
            ws1Reader = new BufferedReader(new FileReader("ws1.txt"));
            ws2Reader = new BufferedReader(new FileReader("ws2.txt"));
            ws3Reader = new BufferedReader(new FileReader("ws3.txt"));
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        initialization();
    }

    private static void initialization() {
    }
}
