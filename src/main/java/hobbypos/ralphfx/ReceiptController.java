package hobbypos.ralphfx;

/**
 * @author ccs
 */


import javafx.fxml.FXML;

import javax.swing.*;
import java.awt.*;
import java.awt.print.*;

public class ReceiptController extends javax.swing.JFrame {

    /**
     * Creates new form Home
     */


    //  String billNo="";
    Double totalAmount = 0.0;
    Double cash = 0.0;
    Double balance = 0.0;
    Double bHeight = 0.0;

    protected static double cm_to_pp(double cm) {
        return toPPI(cm * 0.393600787);
    }

    protected static double toPPI(double inch) {
        return inch * 72d;
    }

    @FXML
    public void startPrint(javafx.event.ActionEvent actionEvent) {

        bHeight = 5.0;
        //JOptionPane.showMessageDialog(rootPane, bHeight);

        PrinterJob pj = PrinterJob.getPrinterJob();
        pj.setPrintable(new BillPrintable(), getPageFormat(pj));
        try {
            pj.print();
            System.err.println("sulodprinter");

        } catch (PrinterException ex) {
            ex.printStackTrace();
        }
    }

    public PageFormat getPageFormat(PrinterJob pj) {

        PageFormat pf = pj.defaultPage();
        Paper paper = pf.getPaper();

        double bodyHeight = bHeight;
        double headerHeight = 5.0;
        double footerHeight = 5.0;
        double width = cm_to_pp(8);
        double height = cm_to_pp(headerHeight + bodyHeight + footerHeight);
        paper.setSize(width, height);
        paper.setImageableArea(0, 10, width, height - cm_to_pp(1));

        pf.setOrientation(PageFormat.PORTRAIT);
        pf.setPaper(paper);

        return pf;
    }

    public class BillPrintable implements Printable {


        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
                throws PrinterException {

            int r = 5;
            ImageIcon icon = new ImageIcon("../../../resources/hobbypos/ralphfx/icon.jpg");
            int result = NO_SUCH_PAGE;
            if (pageIndex == 0) {

                Graphics2D g2d = (Graphics2D) graphics;
                double width = pageFormat.getImageableWidth();
                g2d.translate((int) pageFormat.getImageableX(), (int) pageFormat.getImageableY());


                //  FontMetrics metrics=g2d.getFontMetrics(new Font("Arial",Font.BOLD,7));

                try {
                    int y = 20;
                    int yShift = 10;
                    int headerRectHeight = 15;
                    // int headerRectHeighta=40;


                    g2d.setFont(new Font("Monospaced", Font.PLAIN, 9));
                    g2d.drawImage(icon.getImage(), 50, 20, 90, 30, rootPane);
                    y += yShift + 30;
                    g2d.drawString("-------------------------------------", 12, y);
                    y += yShift;
                    g2d.drawString("         CodeGuid.com        ", 12, y);
                    y += yShift;
                    g2d.drawString("   No 00000 Address Line One ", 12, y);
                    y += yShift;
                    g2d.drawString("   Address Line 02 SRI LANKA ", 12, y);
                    y += yShift;
                    g2d.drawString("   www.facebook.com/CodeGuid ", 12, y);
                    y += yShift;
                    g2d.drawString("        +94700000000      ", 12, y);
                    y += yShift;
                    g2d.drawString("-------------------------------------", 12, y);
                    y += headerRectHeight;

                    g2d.drawString(" Item Name                  Price   ", 10, y);
                    y += yShift;
                    g2d.drawString("-------------------------------------", 10, y);
                    y += headerRectHeight;

                    //  for (int s = 0; s < r; s++) {
                    //     g2d.drawString(" " + itemName.get(s) + "                            ", 10, y);
                    //       y += yShift;
                    //     g2d.drawString("      " + quantity.get(s) + " * " + itemPrice.get(s), 10, y);
                    //      g2d.drawString(subtotal.get(s), 160, y);
                    //      y += yShift;
                    //   }

                    g2d.drawString("-------------------------------------", 10, y);
                    y += yShift;
                    g2d.drawString(" Total amount:                  ", 10, y);
                    y += yShift;
                    g2d.drawString("-------------------------------------", 10, y);
                    y += yShift;
                    g2d.drawString(" Cash      :                 ", 10, y);
                    y += yShift;
                    g2d.drawString("-------------------------------------", 10, y);
                    y += yShift;
                    g2d.drawString(" Balance   :                 ", 10, y);
                    y += yShift;

                    g2d.drawString("*************************************", 10, y);
                    y += yShift;
                    g2d.drawString("       THANK YOU COME AGAIN            ", 10, y);
                    y += yShift;
                    g2d.drawString("*************************************", 10, y);
                    y += yShift;
                    g2d.drawString("       SOFTWARE BY:CODEGUID          ", 10, y);
                    y += yShift;
                    g2d.drawString("   CONTACT: contact@codeguid.com       ", 10, y);
                    y += yShift;
                    System.err.println(g2d);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                result = PAGE_EXISTS;
            }
            return result;
        }
    }
}