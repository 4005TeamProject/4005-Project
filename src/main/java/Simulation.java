import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

//This class represents the simulation model
public class Simulation {

    private static final double insp1Lambda = 0.0965;
    private static final double insp22Lambda = 0.0643;
    private static final double insp23Lambda = 0.0485;
    private static final double workst1Lambda = 0.2172;
    private static final double workst2Lambda = 0.09015;
    private static final double workst3Lambda = 0.113693;

    private static double clock;
    private static int currentDay, simDay;

    //Future Event List
    private static Queue<SimEvent> FEL;

    //The five queues for components
    //naming convention cXwX ==> cX = component type I.e. C1, C2 or C3. wX = workstation I.e. W1, W2 or W3.
    //each queue has a min capacity of 0 and max capacity of 2.
    //private final int QUEUE_CAP = 2;
    private static Queue<Component> c1w1, c1w2, c1w3, c2w2, c3w3;

    //Two more component queues for insp1 and insp2
    private static Queue<Component> IQ1, IQ2;

    //Last time inspectors/workstations were idle.
    private static int lastI1Idle, lastI2idle, lastW1Idle, lastW2Idle, lastW3Idle;

    //inspector/workstation is busy or not
    private static boolean isI1busy, isI2busy, isW1busy, isW2busy, isW3busy;

    /**
     * ************************Please Help Here*******************************
     */
    //Simulation inputs + any function used to get these random variables.
    private static double[][] ITD = {}; //Inspection times go here (minutes, cumulative probability)
    private static double[][] WTD = {}; //Workstation times go here

    //Variables for the required metrics, statistics, and counters (B = busy, U = util)
    private static double BI1, BI2, BW1, BW2, BW3, UI1, UI2, UW1, UW2, UW3, p1, p2, p3;



    public static void main(String[] args) {
        clock = 0; //start clock at 0
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
        FEL = new PriorityQueue<>();
        IQ1 = new LinkedList<>();
        IQ2 = new LinkedList<>();
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


        initialization();

        //Main while loop
        while ((currentDay<=simDay) && (!FEL.isEmpty())){
            SimEvent imminentEvent = FEL.poll();
            clock = imminentEvent.getEventTime();
            ProcessSimEvent(imminentEvent);
        }
        GenerateReport();
    }

    private static void GenerateReport() {
        System.out.print("Time elapsed:" + clock +"\nProduct1 produced = " + p1
                + "\nProduct2 produced = " + p2 + "\nProduct3 produced = " + p3);
    }

    private static void ProcessSimEvent(SimEvent imminentEvent) {
        switch (imminentEvent.geteventType()){
            case AI1:
                ProcessAI1(imminentEvent);
                break;
            case AI2:
                ProcessAI2(imminentEvent);
                break;
            case EI1:
                ProcessEI1(imminentEvent);
                break;
            case EI2:
                ProcessEI2(imminentEvent);
                break;
            case EW1:
                ProcessEW1(imminentEvent);
                break;
            case EW2:
                ProcessEW2(imminentEvent);
                break;
            case EW3:
                ProcessEW3(imminentEvent);
                break;
            case ES:
                ProcessES(imminentEvent);
                break;
        }
    }

    private static void ProcessES(SimEvent imminentEvent) {
        System.out.print("Simulation finished at: " + clock + "\nProduct1 produced = " + p1
                + "\nProduct2 produced = " + p2 + "\nProduct3 produced = " + p3);
    }

    private static void ProcessEW3(SimEvent imminentEvent) {
            Component componentConsumed = c1w3.poll();
            Component componentConsumed2 = c3w3.poll();
            p3 += 1;
            isW3busy = false;
            System.out.print("Time: " + clock + "Product3 produced");
    }

    private static void ProcessEW2(SimEvent imminentEvent) {
            Component componentConsumed = c1w2.poll();
            Component componentConsumed2 = c2w2.poll();
            System.out.print("Time: " + clock + "Product2 produced");
            p2 += 1;
            isW2busy = false;

    }

    private static void ProcessEW1(SimEvent imminentEvent) {
            Component componentConsumed = c1w1.poll();
            componentConsumed.setCurrentLocation(Component.serviceType.Workstation);
            System.out.print("Time: " + clock + "Product1 produced");
            p1 += 1;
            isW1busy = false;
    }

    private static void ProcessEI2(SimEvent imminentEvent) {
        if(imminentEvent.getComponent().getComponentNumber() == 2){
            if(c2w2.size() < 2){
                imminentEvent.getComponent().setCurrentLocation(Component.serviceType.Buffer);
                c2w2.offer(imminentEvent.getComponent());
                double WET = getRandomTime(workst2Lambda);
                SimEvent evt = new SimEvent(SimEvent.eventType.EW2, clock+WET, imminentEvent.getComponent());
                FEL.offer(evt);
                evt = new SimEvent(SimEvent.eventType.AI2, 0, imminentEvent.getComponent());
                FEL.offer(evt);
                isI2busy = false;
                isW2busy = true;
            }else {
                imminentEvent.getComponent().setCurrentLocation(Component.serviceType.Inspection);
                //TODO need to block the inspector somehow
            }
        }else if(imminentEvent.getComponent().getComponentNumber() == 3) {
            if (c3w3.size() < 2) {
                imminentEvent.getComponent().setCurrentLocation(Component.serviceType.Buffer);
                c3w3.offer(imminentEvent.getComponent());
                double WET = getRandomTime(workst3Lambda);
                SimEvent evt = new SimEvent(SimEvent.eventType.EW3, clock+WET, imminentEvent.getComponent());
                FEL.offer(evt);
                evt = new SimEvent(SimEvent.eventType.AI2, 0, imminentEvent.getComponent());
                FEL.offer(evt);
                isI2busy = false;
                isW3busy = true;
            }else{
                imminentEvent.getComponent().setCurrentLocation(Component.serviceType.Inspection);
                isI2busy = true;
                //TODO need to block the inspector somehow
            }
        }
    }

    //Component 1 leaves inspector 1 and enters shortest queue
    private static void ProcessEI1(SimEvent imminentEvent) {
        if(c1w1.size() < 2 && c1w1.size() < c1w2.size() && c1w1.size() < c1w3.size()){
            imminentEvent.getComponent().setCurrentLocation(Component.serviceType.Buffer);
            c1w1.offer(imminentEvent.getComponent());
            double WET = getRandomTime(workst1Lambda);
            SimEvent evt = new SimEvent(SimEvent.eventType.EW1, clock+WET, imminentEvent.getComponent());
            FEL.offer(evt);
            evt = new SimEvent(SimEvent.eventType.AI1, 0, c1w1.poll());
            FEL.offer(evt);
            isI1busy = false;
            isW1busy = true;
        }else if(c1w2.size() < 2 && c1w2.size() < c1w1.size() && c1w2.size() < c1w3.size() ){
            imminentEvent.getComponent().setCurrentLocation(Component.serviceType.Buffer);
            c1w2.offer(imminentEvent.getComponent());
            double WET = getRandomTime(workst2Lambda);
            SimEvent evt = new SimEvent(SimEvent.eventType.EW2, clock+WET, imminentEvent.getComponent());
            FEL.offer(evt);
            evt = new SimEvent(SimEvent.eventType.AI1, 0, c1w2.poll());
            FEL.offer(evt);
            isI1busy = false;
        }else if(c1w2.size() < 2 && c1w3.size() < c1w1.size() && c1w3.size() < c1w2.size() ){
            imminentEvent.getComponent().setCurrentLocation(Component.serviceType.Buffer);
            c1w3.offer(imminentEvent.getComponent());
            double WET = getRandomTime(workst3Lambda);
            SimEvent evt = new SimEvent(SimEvent.eventType.EW3, clock+WET, imminentEvent.getComponent());
            FEL.offer(evt);
            evt = new SimEvent(SimEvent.eventType.AI1, 0, c1w3.poll());
            FEL.offer(evt);
            isI1busy = false;
        }else{
            imminentEvent.getComponent().setCurrentLocation(Component.serviceType.Inspection);
            isI1busy = true;
        }
    }

    private static void ProcessAI2(SimEvent imminentEvent) {
        if(IQ2.isEmpty()) {
            if (!isI2busy) {
                isI2busy = true;
                imminentEvent.getComponent().setCurrentLocation(Component.serviceType.Inspection);
            }
        }
        imminentEvent.getComponent().setCurrentLocation(Component.serviceType.Waiting);
        IQ2.offer(imminentEvent.getComponent());
    }

    //component arrives at inspector 1
    private static void ProcessAI1(SimEvent imminentEvent) {
        if(IQ1.isEmpty()) {
            if (!isI1busy) {
                isI1busy = true;
                imminentEvent.getComponent().setCurrentLocation(Component.serviceType.Inspection);
            }
        }
        imminentEvent.getComponent().setCurrentLocation(Component.serviceType.Waiting);
        IQ1.offer(imminentEvent.getComponent());
    }


    private static double getRandomTime(double lambda){
        InputGenerator ig = new InputGenerator(lambda);
        return ig.getInput();
    }


    private static void initialization() {
        Component C1 = new Component(1, 1);
        Component C2 = new Component(2, 1);
        Component C3 = new Component(3, 1);
        Component C4 = new Component(4, 1);
        Component C5 = new Component(5, 2);
        Component C6 = new Component(6, 2);
        Component C7 = new Component(7, 2);
        Component C8 = new Component(8, 3);
        Component C9 = new Component(9, 3);
        Component C10 = new Component(10, 3);

        //C1 is at insp1
        isI1busy = true;
        C1.setCurrentLocation(Component.serviceType.Inspection);
        double WET = getRandomTime(insp1Lambda);
        SimEvent evt = new SimEvent(SimEvent.eventType.EI1, clock+WET, C1);
        FEL.offer(evt);

        //C2, C3, and C4 are in the queue for Inspector 1
        C2.setCurrentLocation(Component.serviceType.Waiting);
        C3.setCurrentLocation(Component.serviceType.Waiting);
        C4.setCurrentLocation(Component.serviceType.Waiting);
        IQ1.offer(C2);
        IQ1.offer(C3);
        IQ1.offer(C4);


        //C5 is at insp2
        isI2busy = true;
        C5.setCurrentLocation(Component.serviceType.Inspection);
        WET = getRandomTime(insp22Lambda);
        evt = new SimEvent(SimEvent.eventType.EI2, clock+WET, C5);
        FEL.offer(evt);

        //C6, C7, C8, C9, and C10 are in the queue for Inspector 2
        C6.setCurrentLocation(Component.serviceType.Waiting);
        C7.setCurrentLocation(Component.serviceType.Waiting);
        C8.setCurrentLocation(Component.serviceType.Waiting);
        C9.setCurrentLocation(Component.serviceType.Waiting);
        C10.setCurrentLocation(Component.serviceType.Waiting);
        IQ2.offer(C6);
        IQ2.offer(C7);
        IQ2.offer(C8);
        IQ2.offer(C9);
        IQ2.offer(C10);

    }


    private static void ScheduleSimEvent(SimEvent.eventType ei1, Component component) {
        Integer newRN = -1;
        switch (ei1){
            case AI1:
                newRN = (int)getRandomTime(insp1Lambda);
                break;
            case AI2:
                newRN = (int)getRandomTime(insp22Lambda);
                break;
            case EW1:
                newRN = (int)getRandomTime(workst1Lambda);
                break;
            case EW2:
                newRN = (int)getRandomTime(workst2Lambda);
                break;
            case EW3:
                newRN = (int)getRandomTime(workst3Lambda);
                break;
        }
        SimEvent newEvt= new SimEvent(ei1, clock+newRN, component);
        FEL.offer(newEvt);
    }
}
