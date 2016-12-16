package Curve;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class BSplineCurve {
    
    private int degree;
    private ArrayList<Double> knots;
    private ArrayList<Point2D.Double> points;
      
    public BSplineCurve(int degree, ArrayList<Double> knots,
            ArrayList<Point2D.Double> points) {
        super();
        this.degree = degree;
        this.knots = knots;
        this.points = points;
    }
    public int getDegree() {
        return degree;
    }
    public void setDegree(int degree) {
        this.degree = degree;
    }
    
    public ArrayList<Double> getKnots() {
        return knots;
    }
    public void setKnots(ArrayList<Double> knots) {
        this.knots = knots;
    }
    
    public ArrayList<Point2D.Double> getPoints() {
        return points;
    }
    public void setPoints(ArrayList<Point2D.Double> points) {
        this.points = points;
    }
    
    public BSplineCurve deepCopy()
    {
        ArrayList<Double> newKnots = new ArrayList<Double>();
        for(Double k : knots) {
            newKnots.add(new Double(k));
        }
        
        ArrayList<Point2D.Double> newPoints = new ArrayList<Point2D.Double>();
        for(Point2D.Double p : points) {
            newPoints.add(new Point2D.Double(p.x,p.y));
        }
        
        return new BSplineCurve(degree, newKnots, newPoints);
    }
    
    public ArrayList<String> getSerializableStringList()
    {
        ArrayList<String> stringList = new ArrayList<>();
        stringList.add(Integer.toString(getDegree()));
        stringList.add(Integer.toString(getPoints().size()));
        
        String knotStr="";
        for(double d:getKnots())
            knotStr += Double.toString(d) + " ";
        stringList.add(knotStr);

        
        for(Point2D.Double p:getPoints())
            stringList.add(Double.toString(p.x) + " " + Double.toString(p.y));
        
        return stringList;
    }

}
