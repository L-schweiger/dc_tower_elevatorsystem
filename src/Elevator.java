import java.util.concurrent.Callable;

public class Elevator implements Callable {

    private final int id; // elevator id
    private int currfloor; // current floor of the elevator
    private Request req = null; // current request to handle

    Elevator(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public synchronized void setReq(Request req){
        this.req = req;
    }


    // elevator main loop - checks where elevator has to be moved to
    // returns the elevator itself if finished
    @Override
    public Object call() throws Exception {
        boolean done = false;
        boolean reachedStartfloor = false;

        while(!done){
            if(!reachedStartfloor){
                if(currfloor < req.getStartfloor()){
                    move(true, reachedStartfloor);
                }
                else if(currfloor > req.getStartfloor())
                    move(false, reachedStartfloor);
                else
                    reachedStartfloor = true;
            } else {
                if(currfloor < req.getDestfloor())
                    move(true, reachedStartfloor);
                else if(currfloor > req.getDestfloor())
                    move(false, reachedStartfloor);
                else {
                    done = true;
                    this.req = null;
                    return this;
                }
            }
        }

        return null;
    }

    // moves the elevator (1 floor = 200ms)
    private void move(boolean directionUp, boolean reachedStartfloor) throws InterruptedException {
        Thread.sleep(200);
        if(directionUp)
            currfloor++;
        else
            currfloor--;

        if(!reachedStartfloor)
            System.out.println("Elevator " + this.id + " picking up passenger from " + this.req.getStartfloor() + " [Current floor: " + this.currfloor + "]");
        else
            System.out.println("Elevator " + this.id + " driving passenger to " + this.req.getDestfloor() + " [Current floor: " + this.currfloor + "]");
    }
}
