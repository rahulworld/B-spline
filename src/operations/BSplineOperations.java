package operations;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import Curve.BSplineCurve;


public class BSplineOperations {
    
    public static int getNumberOfSegments(BSplineCurve sBsplineCrv) 
    {
        int numberOfSections = (sBsplineCrv.getKnots().size() - 2 * sBsplineCrv.getDegree()) - 1;
        return numberOfSections;
    }
    
    public static double distanceBetweenTwoPoints(Point2D.Double A, Point2D.Double B)
    {
        return Math.sqrt((B.x-A.x)*(B.x-A.x)+(B.y-A.y)*(B.y-A.y));
    }
    
    public static Point2D.Double midPoint(Point2D.Double A, Point2D.Double B)
    {
        return new Point2D.Double((B.x+A.x)/2, (B.y+A.y)/2);
    }
    
    
    public static BSplineCurve addKnot(BSplineCurve sBsplineCrv, Point2D.Double p)
    {
        BSplineCurve newBsplineCrv = sBsplineCrv.deepCopy();
        
        int startIndex = newBsplineCrv.getDegree();
        int endIndex = sBsplineCrv.getPoints().size();
        
        double start = newBsplineCrv.getKnots().get(startIndex);
        double end = sBsplineCrv.getKnots().get(endIndex);
        
        double avg = (end - start)/(endIndex - startIndex);
        
        double newKnot = end + avg;
        newBsplineCrv.getPoints().add(p);
        newBsplineCrv.getKnots().add(newBsplineCrv.getPoints().size(), newKnot);
        
        for(int i=newBsplineCrv.getPoints().size(); i <newBsplineCrv.getKnots().size(); i++ )
            newBsplineCrv.getKnots().set(i, newKnot);
        
        return newBsplineCrv;
    }
    
    /**=============================================================
     * @param sBsplineCrv
     * @param u
     * @param index
     * @see NOT YET DONE, PLEASE IGNORE http://www.inf.ed.ac.uk/teaching/courses/cg/d3/knotInsert.html 
     * @return BSplineCurve
     */
    public static BSplineCurve insertKnot(BSplineCurve sBsplineCrv, double u, int index)
    {
        BSplineCurve newBsplineCrv = sBsplineCrv.deepCopy();
        
        int k = index + 1;
        
        //insert the knot
        double t = getKnotValueForU(sBsplineCrv,u);
        newBsplineCrv.getKnots().add(k, t);
        
        Point2D.Double A = sBsplineCrv.getPoints().get(index);
        Point2D.Double B = sBsplineCrv.getPoints().get(index + 1);
        
        newBsplineCrv.getPoints().add(k, midPoint(A,B));
        int p = sBsplineCrv.getDegree();
        int s = Math.abs(p/2);
        int n = newBsplineCrv.getPoints().size();
        
        for(int i=k-s; i<=k+s; i++)
        {
            if((i<=1) || (i==k) || (i>=n))
                continue;
            
            Point2D.Double p1 = sBsplineCrv.getPoints().get(i-1);
            Point2D.Double p2 = sBsplineCrv.getPoints().get(i);
            newBsplineCrv.getPoints().set(i, midPoint(p1, p2));
        }
        
                
        
        //TODO Calculate the new position of the points  
        //FIXME
        /*
        int p = sBsplineCrv.getDegree();
        int s = Math.abs(p/2);//??
        int h = 2;//??
        int n = newBsplineCrv.getPoints().size();
        for(int r = 1 ; r < h; r++ )
        {
            for(int i=k-s; i<=k+s; i++)
            {
                if(i-1<0 || (i + p -r + 1) > (n-1))
                    continue;
                
                double ui = getTForIndex(newBsplineCrv, i);
                double uipr = getTForIndex(newBsplineCrv, (i + n -r + 1));
                double ti = getKnotValueForU(newBsplineCrv, ui);
                double tinr =getKnotValueForU(sBsplineCrv, uipr);
                
                double a = getAlpha(t, ti, tinr);
                System.out.println("Alpha -> " + a);
                
                 double x = (1 - a) * newBsplineCrv.getPoints().get(i-1).x + a * newBsplineCrv.getPoints().get(i).x;
                 double y = (1 - a) * newBsplineCrv.getPoints().get(i-1).y + a * newBsplineCrv.getPoints().get(i).y;
                 newBsplineCrv.getPoints().set(i, new Point2D.Double(x,y));
                
            }
        }
        */
        //FIXME
                                    
        return newBsplineCrv;
    }
    
    static double getAlpha(double t, double ti, double tinr)
    {
        System.out.println("t -> " + t);
        System.out.println("ti -> " + ti);
        System.out.println("tinr -> " + tinr);
        return (t - ti)/ (tinr - ti);
    }
    
    public static double getTForIndex(BSplineCurve sBsplineCrv, int i)
    {
        double totalDistance = 0.0;
        double indexDistance = 0.0;
        for (int j = 0; j < sBsplineCrv.getPoints().size()-1; j++) {
            Point2D.Double A = sBsplineCrv.getPoints().get(j);
            Point2D.Double B = sBsplineCrv.getPoints().get(j+1);
            
            if(i==j)
                indexDistance = totalDistance + distanceBetweenTwoPoints(A, B);
            
            totalDistance += distanceBetweenTwoPoints(A, B);
        }
        
        if(indexDistance==0.0)
            return 0.0;
        
        return indexDistance/totalDistance;
    }
    
    
    public static double getKnotValueForU(BSplineCurve sBsplineCrv, double t) {
        double start = sBsplineCrv.getKnots().get(sBsplineCrv.getDegree());
        double end = sBsplineCrv.getKnots().get(sBsplineCrv.getPoints().size());
        
        return ((end - start) * t);
    }

    
    //=============================================================
    public static ArrayList<Point2D.Double> getPointsOnCurveUniform(BSplineCurve sBsplineCrv, int resolution) {
        ArrayList<Point2D.Double> listOfPoints = new ArrayList<>();
        
        double start = sBsplineCrv.getKnots().get(sBsplineCrv.getDegree());
        double end = sBsplineCrv.getKnots().get(sBsplineCrv.getPoints().size());
        
        int totalSplices = (resolution);
        //System.out.println("Uniform resolution" + totalNumberOfIntervals);

        double splice = ((end - start) / (totalSplices));
         //System.out.println("start:" + start);
         //System.out.println("end:" + end);
         //System.out.println("interval:" + interval);

        double t = start;
        for (int i = 0; i <= totalSplices; i++) 
        {            
            int interval = findInterval(sBsplineCrv, t);
            for (int j = sBsplineCrv.getDegree(); j < (sBsplineCrv.getPoints().size()); j++) {
                if (t >= sBsplineCrv.getKnots().get(j) && t < (splice + sBsplineCrv.getKnots().get(j + 1)))
                {
                    interval = j;
                    break;
                }
            }
            
            if(interval==0)
                continue;//interval for t not found

            Point2D.Double curr = BSplineOperations.deBoor(sBsplineCrv, t, sBsplineCrv.getDegree(), interval);
            listOfPoints.add(curr);

            t += splice;
        }
        
        return listOfPoints;
    }
    
    static int findInterval(BSplineCurve sBsplineCrv, double t)
    {
        int i = 0;
        for (int j = sBsplineCrv.getDegree(); j < (sBsplineCrv.getPoints().size()); j++) {
            if (t > sBsplineCrv.getKnots().get(j) && t <= sBsplineCrv.getKnots().get(j + 1))
            {
                i = j;
                break;
            }
        }

        return i;
    }
    
    /** ============================================================
     * @param sBsplineCrv
     * @param t
     * @param r
     * @param i
     * @return Point2D.Double
     * @see http://www.inf.ed.ac.uk/teaching/courses/cg/d3/deBoor.html
     */
    public static Point2D.Double deBoor(BSplineCurve sBsplineCrv, double t, int r, int i) 
    {
        double x = 0.0;
        double y = 0.0;

        if (r == 0) 
        {
            x = sBsplineCrv.getPoints().get(i).x;
            y = sBsplineCrv.getPoints().get(i).y;

        } 
        else 
        {
            double a = getAlpha(sBsplineCrv.getKnots(), sBsplineCrv.getDegree() + 1, t, r, i);
            x = (1 - a) * deBoor(sBsplineCrv, t, r - 1, i - 1).x + a * deBoor(sBsplineCrv, t, r - 1, i).x;
            y = (1 - a) * deBoor(sBsplineCrv, t, r - 1, i - 1).y + a * deBoor(sBsplineCrv, t, r - 1, i).y;
            
        }

        return new Point2D.Double(x, y);
    }
    
    static double getAlpha(ArrayList<Double> knots, int order, double t, int r, int i)
    {
        return (t - knots.get(i))/ (knots.get(i + order - r) - knots.get(i));
    }
        
    // ============================================================
    public static Point2D.Double[] extractBezier(BSplineCurve sBsplineCrv, int ind) {
        int i, j;
        int k;
        double knots[] = new double[50];
        Point2D.Double cnt[] = new Point2D.Double[30];

        k = sBsplineCrv.getDegree();

        // copy one segment
        for (i = ind - k, j = 0; i <= ind; i++) {

            double x = sBsplineCrv.getPoints().get(i).x;
            double y = sBsplineCrv.getPoints().get(i).y;
            cnt[j] = new Point2D.Double(x, y);
            j++;
        }
        for (i = ind - k, j = 0; i <= ind + k + 1; i++) {
            knots[j] = sBsplineCrv.getKnots().get(i);
            j++;
        }

        // insert knots to make the left end be Bezier end
        while (true) {
            for (i = k - 1; i > 0; i--) {
                if (knots[i] < knots[k]) {
                    j = i;
                    break;
                }
                j = 0;
            }

            if (j == 0)
                break;

            // update control points
            for (i = 0; i < j; i++) {
                cnt[i].x = ((knots[k + 1 + i] - knots[k]) / (knots[k + i + 1] - knots[i + 1]))
                        * cnt[i].x
                        + ((knots[k] - knots[i + 1]) / (knots[k + i + 1] - knots[i + 1]))
                                * cnt[i + 1].x;
                cnt[i].y = ((knots[k + 1 + i] - knots[k]) / (knots[k + i + 1] - knots[i + 1]))
                        * cnt[i].y
                        + ((knots[k] - knots[i + 1]) / (knots[k + i + 1] - knots[i + 1]))
                                * cnt[i + 1].y;
            }
            // update knots
            for (i = 0; i < j; i++)
                knots[i] = knots[i + 1];
            knots[j] = knots[k];
        }

        // insert knots to make the right end be Bezier end
        while (true) {
            for (i = k + 2; i < k + k + 1; i++) {
                if (knots[i] > knots[k + 1]) {
                    j = i;
                    break;
                }
                j = 0;
            }

            if (j == 0)
                break;

            // update control points
            for (i = k; i >= j - k; i--) {
                cnt[i].x = ((knots[k + i] - knots[k + 1]) / (knots[k + i] - knots[i]))
                        * cnt[i - 1].x
                        + ((knots[k + 1] - knots[i]) / (knots[k + i] - knots[i])) * cnt[i].x;
                cnt[i].y = ((knots[k + i] - knots[k + 1]) / (knots[k + i] - knots[i]))
                        * cnt[i - 1].y
                        + ((knots[k + 1] - knots[i]) / (knots[k + i] - knots[i])) * cnt[i].y;
            }
            // update knots
            for (i = k + k + 1; i > j; i--)
                knots[i] = knots[i - 1];
            knots[j] = knots[k + 1];
        }

        Point2D.Double[] bez = new Point2D.Double[sBsplineCrv.getDegree() + 1];
        // return the Bezier control points
        for (i = 0; i < sBsplineCrv.getDegree() + 1; i++) {
            bez[i] = cnt[i];
        }

        return bez;
    }

    //=============Berstein Polynominal======================== 
    private static double factorial(int n) {
        int fact = 1;
        for (int i = 1; i <= n; i++) {
            fact *= i;
        }
        return fact;
    }

    private static double binomial_coefficient(int n, int i) {
        double rez, factn, fact_i, fact_n_i;

        factn = factorial(n);
        fact_i = factorial(i);
        fact_n_i = factorial(n - i);
        rez = factn / (fact_i * fact_n_i);

        return rez;
    }

    private static double Bernstein(int n, int i, double t) {

        double basis;

        double v; /* t^i */

        double k; /* (1 - t)^i */

        if (t == 0.0 && i == 0)
            v = 1.0;
        else
            v = Math.pow(t, i);

        if (n == i && t == 1.0)
            k = 1.0;
        else
            k = Math.pow((1 - t), (n - i));

        basis = binomial_coefficient(n, i) * v * k;
        return basis;
    }

    public static Point2D.Double[] bernstienPolynomial(Point2D.Double[] initial_points, int degree, int resolution) {
        //System.out.println("Adaptive resolution" + resolution);
        
        Point2D.Double[] points_on_curve = new Point2D.Double[resolution];

        double basis, step, t;

        t = 0;
        step = (double) 1.0 / (resolution);

        for (int j = 0; j < resolution; j++) {
            if ((1.0 - t) < step)
                t = 1.0;

            points_on_curve[j] = new Point2D.Double(0.0, 0.0);

            for (int i = 0; i < initial_points.length; i++) {
                basis = Bernstein(degree, i, t);
                points_on_curve[j].x += basis * initial_points[i].x;
                points_on_curve[j].y += basis * initial_points[i].y;

            }
            t += step;
        }

        return points_on_curve;
    }
    
    /** ===============================================
     * @param initial_points
     * @param degree
     * @param resolution
     * @return Point2D.Double[]
     * @see http://www.inf.ed.ac.uk/teaching/courses/cg/d3/Casteljau.html
     */
    public static Point2D.Double[] getPointsByDeCasteljau(Point2D.Double[] initial_points, int degree, int resolution)
    {
        Point2D.Double[] points_on_curve = new Point2D.Double[resolution];

        double t;

        t = 0;
        double step = (double) 1.0 / (resolution);

        for (int j = 0; j < resolution; j++) {
            if ((1.0 - t) < step)
                t = 1.0;

            points_on_curve[j] = new Point2D.Double(0.0, 0.0);

            for (int i = 0; i < initial_points.length; i++) {
                points_on_curve[j] = deCasteljau(initial_points, t, initial_points.length-1, 0);

            }
            t += step;
        }

        return points_on_curve;
    }
    
    public static Point2D.Double deCasteljau (Point2D.Double[] initial_points, double t, int r, int i)
    {
        double x = 0.0;
        double y = 0.0;

        if (r == 0) 
        {
            x = initial_points[i].x;
            y = initial_points[i].y;

        } 
        else 
        {
            x = (1 - t) * deCasteljau(initial_points, t, r - 1, i).x + t * deCasteljau(initial_points, t, r - 1, i+1).x;
            y = (1 - t) * deCasteljau(initial_points, t, r - 1, i).y + t * deCasteljau(initial_points, t, r - 1, i+1).y;
            
        }

        return new Point2D.Double(x, y);
    }
    
    public static int findInterval(Point2D.Double[] initial_points, double t)
    {
        return (int)(initial_points.length * t);
    }
}
