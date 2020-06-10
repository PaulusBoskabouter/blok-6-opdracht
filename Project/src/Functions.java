import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author Paul Verhoeven
 * @version jan 30, 2019
 */

public class Functions {
    private final String directory = "/home/paul/Documents/School/Java/Blok_6/Afvinkopdrachten/Eindopdracht/";
    private final String directoryBash = directory + "bash/";
    private final String directoryData = directory + "data_files/";
    private final String directoryLogs = directory + "logs/";

    private ArrayList<Variant> patientData = new ArrayList<>();
    private HashMap<String, String> patientMutation = new HashMap<>();
    private ArrayList<String> hashData = new ArrayList<>(2);
    
    /**
     * Upon initializing Run.java, the file variant_summary_md5.txt will be downloaded to preform a version check
     * on variant_summary.txt.gz
     */
    void DownloadMd5() {   
        try {
            Process permission = Runtime.getRuntime().exec("chmod ugo+rwx " + directoryBash + "downloadmd5.sh");
            permission.waitFor();
            System.out.println("Downloading variant_summary.txt.gz.md5 as variant_summary_mdt.txt...\n");
            Process test = Runtime.getRuntime().exec(directoryBash + "downloadmd5.sh " + directoryData + "variant_summary_md5.txt");
            test.waitFor();
            System.out.println("Succesfully downloaded variant_summary.txt.gz.md5 \nFile has been download to:\t" + directoryData + "variant_summary_md5.txt\n");
        } catch (IOException e) {
            System.out.println("Could not preform update\n\t- Please make sure downloadmd5.sh is installed accordingly.\n\t- Check if you have execute privileges over downloadmd5.sh.");
        } catch (InterruptedException i) {
            System.out.println("Download went wrong downloadmd5.sh was interrupted, cannot preform download.\n");
        }
    }
    
    /**
     * This function makes an Arraylist filled with the hashcodes in the variant_summary.txt.gz.md5 file and calculates 
     * it of variant_summary.txt.gz
     */
    void Md5Sum() {
        String s;
        try {
            Process md5CheckFile = Runtime.getRuntime().exec("md5sum " + directoryData + "variant_summary.txt.gz");
            BufferedReader br = new BufferedReader(new InputStreamReader(md5CheckFile.getInputStream()));

            while ((s = br.readLine()) != null)
                hashData.add(s.split(" ")[0]);
            md5CheckFile.waitFor();
            md5CheckFile.destroy();

            File file = new File(directoryData + "variant_summary_md5.txt");
            BufferedReader fr = new BufferedReader(new FileReader(file));
            while ((s = fr.readLine()) != null) {
                hashData.add(s.split(" ")[0]);

            }
        } catch (InterruptedException i) {
            System.out.println("Process was interupted could not preform md5sum");
        } catch (IOException e) {
            System.out.println("Could not find variant_summary_md5.txt\nMake sure the file is in the right directory.\n")
        }
    }
    
    /** 
     * Checks if the Hashcodes from arraylist hashData are equal to one another.
     * @return This function returns an int of the follwing value:
     * 1 = The hashcodes are equal and thus the variant_summary.txt is up to date
     * 0 = The hashcodes are unequal and thus the variant_summary.txt needs an update.
     * 2 = An errorcode a file might be missing the user will still get the option to update.
     */
    int Check() {
        System.out.println("Preforming md5 check...\n");
        if (hashData.size() == 2) {
            if (hashData.get(0).equals(hashData.get(1))) {
                System.out.println("The variant summary file is up to date!\n");
                return 1;
            } else {
                return 0;
            }
        } else {
            return 2;
        }
    }
    
    /**
     * If the hashcodes did not match the user gets a prompt to if they want to update or not.
     * The user also gets a message showing the last time an update was preformed from the file "updated_log.txt"
     * @param checkValue is the returned value from the Check() function.
     * @return a boolean if user input is equal to Y.
     */
    boolean UserWantUpdate(int checkValue) {
        String s;
        StringBuilder date = new StringBuilder();
        try {
            File file = new File(
                    directoryLogs + "updated_log.txt"
            );
            BufferedReader fr = new BufferedReader(new FileReader(file));
            while ((s = fr.readLine()) != null) {
                date.append(s);
            }
        } catch (IOException e) {
            System.out.println("Could not display last updated date.\n");
            date.append("Date not available");
        }
        if (checkValue == 0) {
            System.out.println("Your variant_summary.txt is not up to date.\n");
            System.out.println("Your file was last updated on: " + date + "\n");
        } else if (checkValue == 2) {
            System.out.println("Could not preform md5 check\n");
            System.out.println("Your file was last updated on: " + date + "\n");
        }
        Scanner scanObj = new Scanner(System.in);
        System.out.println("Would you like to update your variant_summary file?(y/n)");

        String userName = scanObj.nextLine().toUpperCase();
        return userName.equals("Y");
    }
    
    /** 
     * Upon user request will update the variant_summary.txt.gz with update.sh bash script. Also makes a call to
     * function WriteLog to update the date file.
     */    
    void Update() {
        try {
            System.out.println("\nWARNING: this is a big file and could take a while, so get some coffee Martijn! :-).\nDownloading variant_summary.txt.gz...");
            Process permission = Runtime.getRuntime().exec("chmod ugo+rwx " + directoryBash + "update.sh");
            permission.waitFor();
            Process download = Runtime.getRuntime().exec(directoryBash + "update.sh " + directoryData + "variant_summary.txt.gz");
            download.waitFor();
            System.out.println("Succesfully downloaded variant_summary.txt.gz \nFile has been download to:\t" + directory + "\n");
            WriteLog();


        } catch (IOException e) {
            System.out.println("Could not preform update\n\t- Please make sure update.sh is installed accordingly.\n\t- Check if you have execute privileges over update.sh.");
        } catch (InterruptedException i) {
            System.out.println("Download went wrong update.sh was interrupted, cannot preform update.\n");
        }
    }
    
    /** 
     * Updates the updated_log.txt with the current date and time. 
     */
    void WriteLog() {
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            try (PrintWriter out = new PrintWriter(directoryLogs + "updated_log.txt")) {
                out.println(dtf.format(now));
            }
            System.out.println("File has now been last updated on: " + dtf.format(now));
        } catch (FileNotFoundException e) {
            System.out.println("Could not update log...\n");
        }
    }
    
    /**
     * Loads in the variant_summary.txt and saves the content of a line and its info as an object only
     * when the patient has the same mutation.
     */
    void AnalyzePatient() {
        String s;
        String mutation;
        try {
            File file = new File(
                    directoryData + "variant_summary.txt"
            );
            boolean read = false;
            BufferedReader fr = new BufferedReader(new FileReader(file));
            while ((s = fr.readLine()) != null) {
                String[] info = s.split("\t");
                mutation = getMutation("rs" + info[9]);
                if (read && !mutation.equals("null") && mutation.contains(info[22])) {
                    // If the patient has a disease from the line it wil initiate a variant object from Variant.java and save in an arraylist patientData.
                    Variant variant = new Variant(Integer.parseInt(info[0]), Integer.parseInt(info[20]), Integer.parseInt(info[7]),
                            Integer.parseInt(info[3]), Integer.parseInt(info[9]), info[12], info[22], info[13], info[21], info[16]);
                    patientData.add(variant);


                } else {
                    read = true;
                }
            }
            patientMutation.clear(); // Cleanup the no longer needed hashmap.

        } catch (IOException e) {
            System.out.println("Could not find file \"variant_summary.txt\" on " + directoryData + "\n\t- Check if the file is in the right path.\n");
        }
    }
    
    /** 
     * Loads in the 23andMe patient file and puts identifying (rs disease number : alternate allele) information in the hashmap patientMutation 
     */
    void LoadPatientData() {
       String s;
        try {
            File file = new File(
                    directoryData + "9313.23andme.7630"
            );
            BufferedReader fr = new BufferedReader(new FileReader(file));
            while ((s = fr.readLine()) != null) {
                String[] info = s.split("\t");
                if (!s.startsWith("#") && Pattern.matches("rs[0-9]*", info[0])) {
                    patientMutation.put(info[0], info[3]);
                }
            }
        } catch (IOException e) {
            System.out.println("Could not find \"9313.23.andme.7630\" in " + directoryData + "\n\t- Check if the file is in the right path.\n");
        }
    }
    
    /** 
     * Returns the alternate allele with disease id From hashmap patientMutation. If it's not the hashmap it returns a string with the value "null" 
     * @return The patient mutation stored in hashmap getMutation else returns string value null. 
     */
    String getMutation(String pattern) {
        if (patientMutation.get(pattern) != null) {
            return patientMutation.get(pattern);
        } else {
            return "null";
        }
    }
    
    /**
     * A short call to the CompareTo function in Variant.java 
     */
    void OrderData() {
        
        Collections.sort(patientData);
    }
    
    /**
     * Writes the ordered results to a file results.tsv in the /data_files folder. 
     */
    void WriteData() {
        try (PrintWriter out = new PrintWriter(directoryData + "results.tsv")) {
            out.println("Pathoghenicity:\tGene Identifier\tRef Allele\tAlt Allele\tDisease\tPositie:\tChromosoom:\n");
            for (Variant object : patientData) {
                out.println(object.getPathogenicity() + "\t" +
                        object.getGeneId() + "\t" +
                        object.getReferenceAllele() + "\t" +
                        object.getAlternateAllele() + "\t" +
                        object.getDisease() + "\t" +
                        object.getPosition() + "\t" +
                        object.getChromosome() + "\n");
            }
            System.out.println("The file has analyzed and written to: " + directoryData + "results.tsv\n" +
                    "There are " + patientData.size() + " mutations in this patient.");
        } catch (FileNotFoundException e) {
            System.out.println("Could not write to results.tsv check if the filepath is correct.\n");
        }

    }

}



