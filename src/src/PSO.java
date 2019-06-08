package src;


public class PSO {
    public static void main(String[] args) {

        if (args.length==1) {
            if (args[0].equals("non"))
                new Mainframe(false);
            else
                new Mainframe(true);
        } else
            new Mainframe(true);
    }
}
