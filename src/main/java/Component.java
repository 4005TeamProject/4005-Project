public class Component {

    /*
    Inspection is when component enters inspection, buffer is when it is done inspection and
    placed in a buffer to a workstation and workstation is when the component is consumed and a
    product is created.
     */
    public enum serviceType {Waiting, Inspection, Buffer, Workstation}

    private int componentID;
    private int componentNumber; // {1, 2, 3}
    private serviceType currentLocation;

    public Component(int id, int componentNumber){
        this.componentNumber = componentNumber;
        this.componentID = id;
    }

    public int getComponentID() {
        return componentID;
    }

    public void setComponentID(int id){
        this.componentID = id;
    }

    public int getComponentNumber() {
        return componentNumber;
    }

    public void setComponentNumber(int componentNumber) {
        this.componentNumber = componentNumber;
    }

    public serviceType getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(serviceType currentLocation) {
        this.currentLocation = currentLocation;
    }
}
