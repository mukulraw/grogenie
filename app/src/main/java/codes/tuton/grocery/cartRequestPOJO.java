package codes.tuton.grocery;

public class cartRequestPOJO implements Comparable<cartRequestPOJO>{

    private String pid;
    private String quantity;

    public String getPid() {
        return pid;
    }

    public String getQuantity() {
        return quantity;
    }

    void setPid(String pid) {
        this.pid = pid;
    }

    void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    @Override
    public int compareTo(cartRequestPOJO o) {
        return 0;
    }
}
