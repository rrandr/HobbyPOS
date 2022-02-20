package hobbypos.ralphfx.model;

public class Voucher {

    private int id;
    private String vouchername;
    private double content;

    public Voucher(int id, String name, double discount) {
        this.id = id;
        this.vouchername = name;
        this.content = discount;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getContent() {
        return content;
    }

    public String getVouchername() {
        return vouchername;
    }

    public int getId() {
        return id;
    }

    public void setContent(double content) {
        this.content = content;
    }

    public void setVouchername(String vouchername) {
        this.vouchername = vouchername;
    }
}
