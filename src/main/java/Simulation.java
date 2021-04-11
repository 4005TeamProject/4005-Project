import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

//This class represents the simulation model
public class Simulation {

    private static final double insp1Lambda = 0.0965;
    private static final double insp22Lambda = 0.0643;
    private static final double insp23Lambda = 0.0485;
    private static final double workst1Lambda = 0.2172;
    private static final double workst2Lambda = 0.09015;
    private static final double workst3Lambda = 0.113693;

    private static double clock;
    private static int simDay;

    //Future Event List
    private static Queue<SimEvent> FEL;

    //The five queues for components
    //naming convention cXwX ==> cX = component type I.e. C1, C2 or C3. wX = workstation I.e. W1, W2 or W3.
    //each queue has a min capacity of 0 and max capacity of 2.
    //private final int QUEUE_CAP = 2;
    private static Queue<Component> c1w1, c1w2, c1w3, c2w2, c3w3;

    //inspector/workstation is busy or not
    private static boolean isW1busy, isW2busy, isW3busy;

    //inspector is blocked if all queues are full for their component.
    private static boolean isI1Blocked, isI2Blocked;

    //Variables for the required metrics, statistics, and counters (B = busy, U = util)
    private static double BI1, BI2, BW1, BW2, BW3, UI1, UI2, UW1, UW2, UW3, p1, p2, p3;



    public static void main(String[] args) {
        initialization();

        //Main while loop
        while ((clock/60<=1)){
            SimEvent imminentEvent = FEL.poll();
            clock = imminentEvent.getEventTime();
            ProcessSimEvent(imminentEvent);
            System.out.println("Clock: " + clock/60);
        }
        GenerateReport();
    }



    private static void ProcessSimEvent(SimEvent imminentEvent) {
        switch (imminentEvent.geteventType()){
            case AI1:
                ProcessAI1(imminentEvent);
                break;
            case AW1:
                ProcessAW1(imminentEvent);
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

    private static void ProcessEW1(SimEvent imminentEvent) {
        clock += imminentEvent.getEventTime();
        Component componentConsumed = c1w1.poll();
        p1 += 1;
        isW1busy = false;
        SimEvent newEvent = new SimEvent(SimEvent.eventType.AI1, clock + 0, Component.getComponent(1));
        FEL.offer(newEvent); //Schedule arrival at workstation AW1
    }

    private static void ProcessAW1(SimEvent imminentEvent) {
        clock += imminentEvent.getEventTime();
        SimEvent newEvent = new SimEvent(SimEvent.eventType.EW1, clock + getRandomTime(workst1Lambda), imminentEvent.getComponent());
        FEL.offer(newEvent); //Schedule arrival at workstation AW1
    }

    //Component 1 leaves inspector 1 and enters shortest queue
    private static void ProcessEI1(SimEvent imminentEvent) {
        clock += imminentEvent.getEventTime();
        if(c1w1.size() == 2){
            isI1Blocked = true;
        }else{
            c1w1.offer(imminentEvent.getComponent());
            SimEvent newEvent = new SimEvent(SimEvent.eventType.AW1, clock + 0, imminentEvent.getComponent());
            FEL.offer(newEvent); //Schedule arrival at workstation AW1
            newEvent = new SimEvent(SimEvent.eventType.AI1, clock + getRandomTime(insp1Lambda), Component.getComponent(1)); //get next component for inspection AI1
        }
    }

    private static void ProcessAI1(SimEvent imminentEvent) {
        clock += imminentEvent.getEventTime();
        double WET = getRandomTime(insp1Lambda);
        SimEvent evt = new SimEvent(SimEvent.eventType.EI1, clock+WET, Component.getComponent(1));
        FEL.offer(evt);  //Add EI1 to fel
    }

    private static void ProcessES(SimEvent imminentEvent) {
        System.out.print("Simulation finished at: " + clock + "\nProduct1 produced = " + p1
                + "\nProduct2 produced = " + p2 + "\nProduct3 produced = " + p3);
    }




    private static double getRandomTime(double lambda){
        InputGenerator ig = new InputGenerator(lambda);
        return ig.getInput();
    }


    private static void initialization() {
        clock = 0; //start clock at 0
        simDay=7; //simulate 1 week
        isI1Blocked = false;
        isI2Blocked = false;
        isW1busy=false;
        isW2busy=false;
        isW3busy=false;
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

        //C1 is at insp1
        double WET = getRandomTime(insp1Lambda);
        SimEvent evt = new SimEvent(SimEvent.eventType.EI1, clock+WET, Component.getComponent(1));
        FEL.offer(evt);  //Add EI1 to fel

    }

    private static void GenerateReport() {
        System.out.print("Time elapsed:" + clock +"\nProduct1 produced = " + p1
                + "\nProduct2 produced = " + p2 + "\nProduct3 produced = " + p3);
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
                isW2busy = true;
            }else {
                imminentEvent.getComponent().setCurrentLocation(Component.serviceType.Inspection);
                isI1Blocked = true;
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
                //isI2busy = false;
                isW3busy = true;
            }else{
                imminentEvent.getComponent().setCurrentLocation(Component.serviceType.Inspection);
                //isI2busy = true;
                //TODO need to block the inspector somehow
            }
        }
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



}
