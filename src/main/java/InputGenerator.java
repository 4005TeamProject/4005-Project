public class InputGenerator {
    private double lambda;
    private double a;
    private double c;
    private double m;
    private double x0;
    private double x;
    private double r;

    public InputGenerator(double lambda){
        this.lambda = lambda;
        a = 16807;
        c = 0;
        m = 2147483647;
        x0 = 123457;
        x = (a * x0) % m;
        r = x / m;
    }

    public double getInput(){
        double input =  ((-1/lambda) * Math.log(r));
        x =  (a * x) % m;
        r = x / m;
        return input;
    }

    public double getLambda() {
        return lambda;
    }

}
