import java.util.ArrayList;

/**
 * @author Paul Verhoeven
 * @version jan 30, 2019
 */
public class Run {
     /** 
      * Runs every function from Functions.java 
      */
    public static void main(String[] args) {
       
        int upToDate;
        boolean shouldUpdate;

        ArrayList<String> md5Sum;
        Functions function = new Functions();
        function.DownloadMd5();
        function.Md5Sum();
        upToDate = function.Check();

        // Check if variant_summary.txt is up to date. If not it will ask the user if they want to preform an update.
        if (upToDate != 1) {
            shouldUpdate = function.UserWantUpdate(upToDate);
            if (shouldUpdate) {
                function.Update();
            }
        }

        function.LoadPatientData();
        function.AnalyzePatient();
        function.OrderData();
        function.WriteData();

    }


}
