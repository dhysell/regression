package regression.r2.noclock.claimcenter.other.legacyimport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class LegacyFileInteractions {

    public static List<String> readFile() {
        List<String> list = new ArrayList<String>();

        File file = new File("test\\regression\\r2\\noclock\\claimcenter\\other\\legacyimport\\LegacyClaims.txt");
        System.out.println(file.getAbsolutePath());

        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(file));
            String text = null;

            while ((text = reader.readLine()) != null) {
                list.add(text);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
            }
        }

        return list;
    }

}
