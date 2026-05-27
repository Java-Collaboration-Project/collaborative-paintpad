package client.export;

//import com.google.gson.Gson;
import com.google.gson.Gson;
import server.db.DrawingRepository;
import shared.model.DrawEvent;

import java.io.FileWriter;
import java.io.FileReader;
import java.util.List;

public class SaveManager {

    private final Gson gson = new Gson();
    private final DrawingRepository repository;

    public SaveManager(DrawingRepository repository) {
        this.repository = repository;
    }

    public void saveSessionToFile(String sessionId, String filePath) {

        try {

            List<DrawEvent> events = repository.getEventsBySession(sessionId);

            FileWriter writer = new FileWriter(filePath);
            gson.toJson(events, writer);
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DrawEvent[] loadSessionFromFile(String filePath) {

        try {

            FileReader reader = new FileReader(filePath);
            return gson.fromJson(reader, DrawEvent[].class);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new DrawEvent[0];
    }
}
//import java.io.File;
//import java.io.FileWriter;
//
//public class SaveManager {
//
//    public void saveToFile(String json, File file) {
//
//        try (FileWriter writer = new FileWriter(file)) {
//
//            writer.write(json);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}