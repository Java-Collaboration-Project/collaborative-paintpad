package client.export;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class ImageExporter {

    public void exportAsPNG(Canvas canvas, File file) {
        saveImage(canvas, file, "png");
    }

    public void exportAsJPG(Canvas canvas, File file) {
        // JPG doesn't support transparency, fill background with white
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(javafx.scene.paint.Color.WHITE);
        saveImage(canvas, file, "jpg", params);
    }

    private void saveImage(Canvas canvas, File file, String format) {
        saveImage(canvas, file, format, new SnapshotParameters());
    }

    private void saveImage(Canvas canvas, File file, String format, SnapshotParameters params) {
        WritableImage image = canvas.snapshot(params, null);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), format, file);
            System.out.println("Exported " + format.toUpperCase() + " successfully.");
        } catch (IOException e) {
            System.err.println("Failed to export image: " + e.getMessage());
        }
    }
}

//package client.export;
//
//import javafx.embed.swing.SwingFXUtils;
//import javafx.scene.SnapshotParameters;
//import javafx.scene.canvas.Canvas;
//import javafx.scene.image.WritableImage;
//
//import javax.imageio.ImageIO;
//import java.io.File;
//
//public class ImageExporter {
//
//    public void exportAsPNG(Canvas canvas, File file) {
//
//        try {
//
//            WritableImage image = canvas.snapshot(new SnapshotParameters(), null);
//
//            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    public void exportAsJPG(Canvas canvas, File file) {
//        WritableImage image = canvas.snapshot(null, null);
//
//        try {
//            ImageIO.write(
//                    SwingFXUtils.fromFXImage(image, null),
//                    "jpg",
//                    file
//            );
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}