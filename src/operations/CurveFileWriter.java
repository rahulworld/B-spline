package operations;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import Curve.BSplineCurve;


public class CurveFileWriter {

    public static void writeCurveToFile(File file, BSplineCurve crv)
    {
        try {

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            
            for(String s:crv.getSerializableStringList() )
                bw.write(s + System.lineSeparator());
            
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
