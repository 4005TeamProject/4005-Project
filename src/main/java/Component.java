import java.util.Random;

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

    public Component(int componentNumber){
        this.componentNumber = componentNumber;
    }

    //@param componentNumber = {1, 2, 3}
    public static Component getComponent(int inspectorNum){
        if(inspectorNum == 1){
            return new Component(1);
        }else{
            Random random = new Random();
            double temp = random.nextDouble();
            if(temp <= 0.5){
                return new Component(2);
            }else{
                return new Component(3);
            }

        }
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
