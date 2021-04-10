public class SimEvent implements Comparable<SimEvent> {

    /*
     * ALS1=Arrival at Inspector1, ALS2=Arrival at Inspector2,
     * ELS1=End of inspection at Inspector1, ELS2=End of inspection at Inspector2
     * BUF = Buffering between inspection and workstation
     * ALW1=Arrival at workstation 1, ALW2=Arrival at workstation 2, ALW3=Arrival at workstation 3
     * ELW1=Product exits workstation 1, ELW2=Product exits workstation 2, ELW3=Product exits workstation 3
     * ES=End of Simulation
     */

    public static enum eventType {ALS1, ALS2, BUF, ELS1,ELS2,ALW1, ALW2, ALW3, ELW1, ELW2, ELW3, ES};
    private eventType eventType;        // Type of the event
    private Double eventTime;          // Event Time

    public SimEvent(eventType eventType, double eventTime) {
        this.eventType = eventType;
        this.eventTime = eventTime;
    }

    @Override
    public int compareTo(SimEvent event) {
        return this.getEventTime().compareTo(event.getEventTime());
    }

    public eventType geteventType() {
        return eventType;
    }

    public void setEventType(eventType eventType) {
        this.eventType = eventType;
    }

    public Double getEventTime() {
        return eventTime;
    }

    public void setEventTime(Double eventTime) {
        this.eventTime = eventTime;
    }
}
