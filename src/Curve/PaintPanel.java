
package Curve;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import Curve.BSplineCurve;

import operations.BSplineOperations;


public class PaintPanel
        extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final int PREF_W = 800;
    public static final int PREF_H = 640;
    public static final int X_OFFSET = PREF_W/4;
    public static final int Y_OFFSET = PREF_H/4;
    
    public enum MODE {
        ADD, INSERT, MOVE
    }

    public BSplineCurve oBsplineCrv;
    
    public BSplineCurve sBsplineCrv;

    private boolean drawControlPolygon = true;

    private boolean uniformRender = true;

    private boolean drawPointsOnCurve = true;

    MODE currMode = MODE.MOVE;

    public static int resolution = 100;

    private int pointDragged = -1;

    private static final int sControlPointRadius = 8;
    private static final int sCurvePointRadius = 4;

    private static final Color SAFFRON = new Color(0xBF551F);

    final static float dash1[] = {
            10.0f
    };

    private static final Stroke polygonStroke = new BasicStroke(1.0f,
            BasicStroke.CAP_SQUARE,
            BasicStroke.JOIN_BEVEL,
            10.0f, dash1, 0.0f);

    public PaintPanel() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    PaintPanel.this.handleMousePressed(e.getX(), e.getY());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    PaintPanel.this.handleMouseReleased(e.getX(), e.getY());
                }
            }
        });
        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if ((e.getModifiersEx() & InputEvent.BUTTON1_DOWN_MASK) != 0) {
                    PaintPanel.this.handleMouseDragged(e.getX(), e.getY());
                }
            }
        });
        this.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println("Key Released");
            }
        });
        
        this.setLayout(null);
        this.setBackground(Color.WHITE);
    }

    public void setBSplineCurve(BSplineCurve bsplineCurve) {
        sBsplineCrv = bsplineCurve.deepCopy();
        oBsplineCrv = bsplineCurve;
        this.revalidate();
        this.repaint();
    }

    private void handleMousePressed(int x, int y) {
        if(sBsplineCrv==null)
            return;
        
        if (currMode == MODE.ADD) {
            
            Point2D.Double p = new Point2D.Double(x - X_OFFSET, y - Y_OFFSET);
            sBsplineCrv = BSplineOperations.addKnot(sBsplineCrv, p);
            
            this.revalidate();
            this.repaint();
        } 
        else if (currMode == MODE.INSERT) { // NOT DONE PLEASE IGNORE
            
            Point2D.Double P = new Point2D.Double(x - X_OFFSET, y - Y_OFFSET);
            double currDistance = 0.0;
            for (int i = 0; i < sBsplineCrv.getPoints().size()-1; i++) {
                Point2D.Double A = sBsplineCrv.getPoints().get(i);
                Point2D.Double B = sBsplineCrv.getPoints().get(i+1);
                double dist = pointToLineDistance(A,B,P);
                
                
                if(dist < 3.0)
                {
                    Point2D.Double T = BSplineOperations.midPoint(A, B);
                    currDistance += BSplineOperations.distanceBetweenTwoPoints(A, T);
                    double t = currDistance/getTotalLengthOfBoundingPolyline();
                    BSplineCurve iBsplineCrv = sBsplineCrv.deepCopy();
                    sBsplineCrv = BSplineOperations.insertKnot(iBsplineCrv, t, i);
                    
                    break;
                }
                currDistance += BSplineOperations.distanceBetweenTwoPoints(A, B);
            }
            
            this.revalidate();
            this.repaint();
        } 
        else if (currMode == MODE.MOVE) {
            for (int i = 0; i < sBsplineCrv.getPoints().size(); i++) {
                Point2D.Double p = sBsplineCrv.getPoints().get(i);
                int xval = (x - ((int) p.x + X_OFFSET));
                int yval = (y - ((int) p.y + Y_OFFSET));
                if (xval * xval + yval * yval <= PaintPanel.sControlPointRadius
                        * PaintPanel.sControlPointRadius) {
                    this.pointDragged = i;
                    break;
                }
            }
        }
    }

    private void handleMouseDragged(int x, int y) {
        if (this.pointDragged > -1) {
            Point2D p = sBsplineCrv.getPoints().get(this.pointDragged);
            p.setLocation(x - X_OFFSET, y - Y_OFFSET);
            this.revalidate();
            this.repaint();
        }
    }

    private void handleMouseReleased(int x, int y) {
        this.pointDragged = -1;
    }

    public void reset() {
        sBsplineCrv = oBsplineCrv.deepCopy();
        pointDragged = -1;
        this.revalidate();
        this.repaint();
    }

    public boolean isControlPolygonDrawn() {
        return this.drawControlPolygon;
    }

    public void setMode(MODE mode) {
        currMode = mode;
    }

    public void setDrawControlPolygon(boolean draw) {
        this.drawControlPolygon = draw;
        this.revalidate();
        this.repaint();
    }

    public void setAdaptiveRender(boolean adaptive) {
        this.uniformRender = !adaptive;
        this.revalidate();
        this.repaint();
    }
    
    public void setResolution(int resolution) {
        PaintPanel.resolution = resolution;
        this.revalidate();
        this.repaint();
    }
    
    public void drawCurvePoints(boolean drawCurvePoints) {
        this.drawPointsOnCurve = drawCurvePoints;
        this.revalidate();
        this.repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (sBsplineCrv != null) {
            this.drawControlPolygon(g2d, sBsplineCrv.getPoints(), PaintPanel.SAFFRON);
            this.drawCurve(g2d, sBsplineCrv.getPoints());
        }
    }

    private void drawControlPolygon(Graphics2D g2d, List<Point2D.Double> points, Color c) {
        // Draw control points
        g2d.setColor(c);
        int diameter = PaintPanel.sControlPointRadius << 1;
        for (Point2D.Double p : points) {
            g2d.fill(new Ellipse2D.Double(p.x  + X_OFFSET - sControlPointRadius, p.y  + Y_OFFSET - sControlPointRadius,
                    diameter, diameter));
        }
        if (this.drawControlPolygon) {
            Stroke old = g2d.getStroke();
            g2d.setStroke(polygonStroke);
            int lim = points.size() - 1;
            for (int i = 0; i < lim; ++i) {
                Point2D.Double p1 = points.get(i);
                Point2D.Double p2 = points.get(i + 1);
                g2d.drawLine((int) p1.x + X_OFFSET , (int) p1.y + Y_OFFSET, (int) p2.x + X_OFFSET, (int) p2.y + Y_OFFSET);
            }
            g2d.setStroke(old);
        }
    }

    private void drawCurve(Graphics2D g2d, List<Point2D.Double> points) {
        if (points.size() >= 4) {
            if (uniformRender)// uniform render
                this.uniformRender(g2d);
            else
                this.adaptiveRender(g2d);
        }
    }

    // ============================================================
    void uniformRender(Graphics2D g2d) 
    {            
        ArrayList<Point2D.Double> listOfPoints = BSplineOperations.getPointsOnCurveUniform(sBsplineCrv, resolution);
        Point2D.Double prev = null;
        for(Point2D.Double curr:listOfPoints)
        {
            g2d.setColor(Color.BLUE);
            int diameter = PaintPanel.sCurvePointRadius << 1;

            // Draw the point
            if (drawPointsOnCurve) {
                g2d.fill(new Ellipse2D.Double(curr.x +  X_OFFSET - sCurvePointRadius,
                        curr.y + Y_OFFSET - sCurvePointRadius,
                        diameter, diameter));
            }

            if (prev != null) {
                g2d.drawLine((int) prev.x + X_OFFSET, (int) prev.y + Y_OFFSET, (int) curr.x + X_OFFSET,
                        (int) curr.y + Y_OFFSET);
            }
            prev = curr;
        }        
    }

    // ============================================================
    void adaptiveRender(Graphics2D g2d) {
        Point2D.Double[] bez; // assume the degree is not greater than 29.
        int i;

        for (i = sBsplineCrv.getDegree(); i < sBsplineCrv.getPoints().size(); i++) {
            if (Math.abs(
                    sBsplineCrv.getKnots().get(i) - sBsplineCrv.getKnots().get(i + 1)) < 0.00001)
                continue; // no segment, skip over

            bez = BSplineOperations.extractBezier(sBsplineCrv, i); // extract the i-th Bezier curve
            plotBezier(g2d, bez, sBsplineCrv.getDegree()); // adaptively plot a Bezier curve
        }
    }
    
    public boolean plotUsingBernsteinPolynomial=false;
    // ============================================================
    private void plotBezier(Graphics2D g2d, Point2D.Double[] points, int deg) 
    {
        int numberOfSections = BSplineOperations.getNumberOfSegments(sBsplineCrv);
        int adaptiveResolution = (resolution/numberOfSections) ;
        
        Point2D.Double[] plotPoints = null;
        if(plotUsingBernsteinPolynomial)
        {
            plotPoints = BSplineOperations.bernstienPolynomial(points, deg, adaptiveResolution + 1);
        }
        else
        {
            plotPoints = BSplineOperations.getPointsByDeCasteljau(points, sBsplineCrv.getDegree(), adaptiveResolution + 1);
        }

        g2d.setColor(Color.BLUE);
        int diameter = PaintPanel.sCurvePointRadius << 1;

        Point2D.Double prev = null;
        
        for (Point2D.Double curr : plotPoints) 
        {
            // Draw the point
            if (drawPointsOnCurve) {
                g2d.fill(new Ellipse2D.Double(curr.x + X_OFFSET - sCurvePointRadius,
                        curr.y + Y_OFFSET - sCurvePointRadius,
                        diameter, diameter));
            }

            if (prev != null) {
                g2d.drawLine((int) prev.x + X_OFFSET, (int) prev.y + Y_OFFSET, (int) curr.x + X_OFFSET,
                        (int) curr.y + Y_OFFSET);
            }
            prev = curr;
        }

    }
    
    private double pointToLineDistance(Point2D.Double A, Point2D.Double B, Point2D.Double P) 
    {
        double normalLength = BSplineOperations.distanceBetweenTwoPoints(A,B);
        
        return Math.abs((P.x-A.x)*(B.y-A.y)-(P.y-A.y)*(B.x-A.x))/normalLength;
    }
    
    private double getTotalLengthOfBoundingPolyline()
    {
        double totalLength = 0.0;
        for (int i = 0; i < sBsplineCrv.getPoints().size()-1; i++) {
            Point2D.Double A = sBsplineCrv.getPoints().get(i);
            Point2D.Double B = sBsplineCrv.getPoints().get(i+1);
            
            totalLength += BSplineOperations.distanceBetweenTwoPoints(A,B);            
        }
        
        return totalLength;
    }

}
