package hobbypos.ralphfx.model;

public class CurrentLogin {

    private int id;
    private String cashier;



    public CurrentLogin(int userID, String names) {
        this.id = userID;
        this.cashier = names;
    }
}
