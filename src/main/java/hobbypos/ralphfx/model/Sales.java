package hobbypos.ralphfx.model;

public class Sales {

    private int id;
    private String cash;
    private String totalsales;
    private String cashier;
    private String recordDate;
    private String recordTime;



    public Sales(int id, String money,String totals,String cashiers,String dates,String times) {
        this.id = id;
        this.cash=money;
        this.totalsales=totals;
        this.cashier = cashiers;
        this.recordDate = dates;
        this.recordTime= times;
    }
}
