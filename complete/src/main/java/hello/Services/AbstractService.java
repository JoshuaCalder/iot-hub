package hello.Services;

import org.springframework.beans.factory.annotation.Autowired;
import javax.inject.Inject;
import hello.Devices.*;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import java.util.Map;
import java.util.UUID;

import java.io.FileWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.Writer;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public abstract class AbstractService {
    @Autowired
    private HubService hubService;

    /**
     * Write to the database
     */
    public void updateDatabase() {
        Map<UUID, Device> devices = hubService.returnDevices();
        String filePath = new File("").getAbsolutePath();
        FileWriter f = null;
        JSONObject json;
        try {
            f = new FileWriter(filePath + "/src/main/resources/db/database.json");
            f.write("[\n");
            for (Map.Entry<UUID, Device> entry : devices.entrySet()) {
                UUID uuid = entry.getKey();
                String status = entry.getValue().getStatus().toString();
                String type = entry.getValue().getClass().getSimpleName().toString();
                System.out.println(uuid);
                System.out.println(status);
                System.out.println(type);
                json = new JSONObject();
                json.put("Type", type);
                json.put("UUID", uuid);
                json.put("Status", status);
                f.write(json.toString());
                f.write(",\n");
            }
            json = new JSONObject();
            f.write("]");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (f != null) {
                    f.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Write messages to the log
     */
    public void writeLog(String msg) {
        String filePath = new File("").getAbsolutePath();
        FileWriter f = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        try {
            f = new FileWriter(filePath + "/src/main/resources/db/log.txt", true);
            f.write(msg + " @ " + dtf.format(now) + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (f != null) {
                    f.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}