public class Request {
    private final int startfloor; // floor where passenger gets picked up
    private final int destfloor; // floor to bring passenger to


    public Request(int startfloor, int destfloor) {
        this.startfloor = startfloor;
        this.destfloor = destfloor;
    }

    public int getStartfloor() {
        return startfloor;
    }

    public int getDestfloor() {
        return destfloor;
    }
}
