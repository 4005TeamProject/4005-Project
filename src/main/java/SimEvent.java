public class SimEvent implements Comparable<SimEvent> {

    /*
     * AI1=Arrival at Inspector1, AI2=Arrival at Inspector2,
     * EI1=End of inspection at Inspector1, ELS2=End of inspection at Inspector2
     * BUF = Buffering between inspection and workstation
     * AW1=Arrival at workstation 1, AW2=Arrival at workstation 2, AW3=Arrival at workstation 3
     * EW1=Product exits workstation 1, EW2=Product exits workstation 2, EW3=Product exits workstation 3
     * ES=End of Simulation
     */

    public static enum eventType {AI1, AI2, EI1,EI2, BUF, AW1, AW2, AW3, EW1, EW2, EW3, ES};
    private eventType eventType;        // Type of the event
    private Double eventTime;          // Event Time
    private Component component;    //the component

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public SimEvent(eventType eventType, double eventTime, Component component) {
        this.eventType = eventType;
        this.eventTime = eventTime;
        this.component = component;
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
