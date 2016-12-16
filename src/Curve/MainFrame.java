package Curve;

import java.awt.Container;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import Curve.BSplineCurve;

import operations.CurveFileParser;
import operations.CurveFileWriter;



public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
    private UIPane uiPane; 

    public MainFrame() {
        uiPane = new UIPane();
        Container cp = this.getContentPane();
        cp.add(uiPane);
        this.setTitle("Rahul Chauhan");
        this.setJMenuBar(this.createMenuBar());
        this.setSize(1080, 720);
        this.setLocationRelativeTo(null);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        JMenuItem open = new JMenuItem("Open", KeyEvent.VK_O);
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileDialog fd = new FileDialog(MainFrame.this, "Choose a curve file", FileDialog.LOAD);
                fd.setFile("*.txt");
                fd.setVisible(true);
                String filename = fd.getFile();
                if (filename != null)
                {
                    BSplineCurve crv = CurveFileParser.parseCurve(fd.getFiles()[0]);
                    if(crv!=null)
                    {
                        //
                        System.out.println("Degree of curve" + crv.getDegree());

                        System.out.println("Knots->  ");
                        for(double d:crv.getKnots())
                            System.out.print(d + " ");

                        System.out.println("Points->  ");
                        for(java.awt.geom.Point2D p:crv.getPoints())
                            System.out.print(p.toString() + " ");

                        System.out.println(" ");
                        //

                        uiPane.canvas.setBSplineCurve(crv);
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(MainFrame.this,
                                "Curve file not in correct format!",
                                "Parse error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        fileMenu.add(open);

        fileMenu.addSeparator();
        // fileMenu.addSeparator();

        JMenuItem save = new JMenuItem("Save", KeyEvent.VK_S);
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileDialog fd = new FileDialog(MainFrame.this, "Save curve to file", FileDialog.SAVE);
                fd.setFile("*.txt");
                fd.setVisible(true);
                String filename = fd.getFile();
                if (filename != null)
                {               
                    CurveFileWriter.writeCurveToFile(fd.getFiles()[0], uiPane.canvas.sBsplineCrv);  
                }
                
                
                
            }
        });
        fileMenu.add(save);

        fileMenu.addSeparator();
        //fileMenu.addSeparator();

        JMenuItem exit = new JMenuItem("Exit", KeyEvent.VK_ESCAPE);
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(exit);

        menuBar.add(fileMenu);
        return menuBar;
    }



    public static void main(String[] args) {
        MainFrame mf = new MainFrame();
        mf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mf.setVisible(true);
    }
}
