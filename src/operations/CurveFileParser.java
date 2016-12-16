package operations;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import Curve.BSplineCurve;


public class CurveFileParser {
    
    public static BSplineCurve parseCurve(File file)
    {
        int degree = -1;
        int numberOfControlPoints = -1;
        ArrayList<Double> knots = new ArrayList<>();
        ArrayList<Point2D.Double> points = new ArrayList<>();
        
        String line;
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));
            
            while ((line = br.readLine()) != null) {
                //System.out.println(line);
                String[] parts = line.split("\\s+");
                if(parts.length==(degree+numberOfControlPoints+1))
                {
                    for(String ks:parts)
                    {
                        try{
                            Double knot = Double.parseDouble(ks);
                            knots.add(knot);
                        }
                        catch(NumberFormatException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
                else if(parts.length==2)
                {
                    if(degree==-1)
                    {
                        try{
                            degree = Integer.parseInt(parts[0].trim());
                            numberOfControlPoints = Integer.parseInt(parts[1].trim());
                        } 
                        catch(NumberFormatException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                    
                    try{
                        Double x = Double.parseDouble(parts[0].trim());
                        Double y = Double.parseDouble(parts[1].trim());
                        
                        Point2D.Double point = new Point2D.Double(x,y);
                        points.add(point);
                    }
                    catch(NumberFormatException e)
                    {
                        e.printStackTrace();
                    }
                    }
                }
                else if(parts.length==1)
                {
                    if(degree==-1)
                    {
                        try{
                            degree = Integer.parseInt(parts[0].trim());
                        } 
                        catch(NumberFormatException e)
                        {}
                    }
                    else
                    {
                        try{
                            numberOfControlPoints = Integer.parseInt(parts[0].trim());
                        } 
                        catch(NumberFormatException e)
                        {}
                    }
                }
                else
                {
                    System.out.println("String parts length: " + parts.length);
                }                   
            }
            
            br.close();
            
            if(degree!=-1 && numberOfControlPoints!=-1 && knots.size()!=0 && points.size()==numberOfControlPoints)
            {
                return new BSplineCurve(degree, knots, points);
            }
            else
            {
                System.out.println("Failed Validation of parsed BSpline Curve!") ;
                System.out.println("Degree- " + degree) ;
                System.out.println("Number of Knots- " + knots.size());
                System.out.println("NumberOfControlPoints==Points " + (numberOfControlPoints==points.size()));
            }
            
         }catch(Exception e){
            e.printStackTrace();
         }
        
        return null;
    }

}
