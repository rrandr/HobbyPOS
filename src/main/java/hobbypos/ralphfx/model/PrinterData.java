package hobbypos.ralphfx.model;

public class PrinterData {
    private int printerID;
    private String printername;
    private String assign;


    public PrinterData(int id, String name, String as) {
        this.printerID = id;
        this.printername = name;
        this.assign = as;
    }

    public int getPrinterID() {
        return printerID;
    }

    public void setPrinterID(int printerID) {
        this.printerID = printerID;
    }

    public String getAssign() {
        return assign;
    }

    public void setAssign(String assign) {
        this.assign = assign;
    }

    public String getPrintername() {
        return printername;
    }

    public void setPrintername(String printername) {
        this.printername = printername;
    }
}
