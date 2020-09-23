public class Main {

    public static void main(String[] args) throws InterruptedException {
        // initialize elevator system in seperate thread
        ElevatorSystem sys = new ElevatorSystem();
        Thread systhread = new Thread(sys);
        systhread.start();

        // tests
        sys.addRequest(5,47);
        sys.addRequest(6,38);
        sys.addRequest(7,31);
        sys.addRequest(8,34);
        sys.addRequest(9,19);
        sys.addRequest(10,35);
        sys.addRequest(11,17);
        sys.addRequest(12,1);
        sys.addRequest(30,5);
        sys.addRequest(28,30);
        sys.addRequest(50,2);
        sys.addRequest(49,9);
        sys.addRequest(31,1);
        sys.addRequest(22,3);
        sys.addRequest(28,26);
        sys.addRequest(1,19);
        sys.addRequest(3,16);
        sys.addRequest(26,22);
        Thread.sleep(17000); // waits 17 seconds until a few elevators are free again to check for available ones
        sys.checkAvailableElevators();
    }
}
