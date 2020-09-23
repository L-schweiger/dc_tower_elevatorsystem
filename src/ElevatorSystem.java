import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ElevatorSystem implements Runnable{

    private volatile Queue<Request> reqQueue = new LinkedList<>(); // queue with requests
    private volatile LinkedList<Elevator> evList = new LinkedList<>(); // available elevators
    private volatile LinkedList<Future<Elevator>> checkevList = new LinkedList<>(); // responses of elevators if request is done
    private ExecutorService execServ = Executors.newFixedThreadPool(7); // service to start every elevator in its own thread

    // initialize all 7 elevators
    ElevatorSystem(){
        for (int i = 0; i < 7; i++) {
            evList.add(new Elevator(i+1));
        }
    }


    // system main loop - calls methods to handle requests and to check for finished requests
    @Override
    public void run() {
        while(true){
            tryHandleRequests();

            try {
                tryCheckEvs();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // tries to start an elevator in its own thread with the next request in queue (if there is one)
    private void tryHandleRequests(){
        LinkedList<Elevator> tmpRemovelist = new LinkedList<>();

        if(reqQueue.size() > 0){
            for (Elevator ev: evList) {
                ev.setReq(reqQueue.poll());
                checkevList.add(execServ.submit(ev));
                tmpRemovelist.add(ev);
            }

            for (Elevator ev: tmpRemovelist){
                evList.remove(ev);
            }
        }
    }

    // checks if started elevators are finished - if yes it moves them into list of available ones
    private void tryCheckEvs() throws ExecutionException, InterruptedException {
        LinkedList<Future<Elevator>> tmpRemovelist = new LinkedList<>();

        for (Future<Elevator> f: checkevList) {
            if(f.isDone()){
                evList.add(f.get());
                tmpRemovelist.add(f);
            }
        }

        for (Future<Elevator> f: tmpRemovelist){
            checkevList.remove(f);
        }
    }

    // adds a new request to the queue if request-input was correct
    public synchronized void addRequest(int startfloor, int destfloor){
        if(destfloor >= 0 && destfloor <= 55 && startfloor >= 0 && startfloor <=55){
            reqQueue.offer(new Request(startfloor, destfloor));
            System.out.println("Added Request from floor " + startfloor + " to " + destfloor);
        }
        else
            System.out.println("Request failed! Start/Destinationfloor has not been found (0-55)!");
    }

    // checks for ready elevators
    public void checkAvailableElevators(){
        System.out.println("---------------------------------------------------------");
        System.out.println("Elevators ready:");
        for (Elevator ev: evList) {
            System.out.println("Elevator " + ev.getId() + " is ready!");
        }
        System.out.println("---------------------------------------------------------");
    }
}
