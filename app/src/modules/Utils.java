package modules;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Utils {
    public static HostInfo loadHostInfo(String pPath) {
        String address = null;
        int port = -1;

        try {
            Scanner reader = new Scanner(new File(pPath));
            String[] line = null;

            while (reader.hasNext()) {
                line = reader.nextLine().split("=");

                if (line[0].equals("ADDRESS")) {
                    address = line[1];
                } else {
                    port = Integer.parseInt(line[1]);
                }
            }
        } catch (FileNotFoundException err) {
            return null;
        }

        return new HostInfo(address, port);
    }
}
