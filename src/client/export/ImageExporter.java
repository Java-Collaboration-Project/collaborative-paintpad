package client.export;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.io.File;

public class ImageExporter {

    public void exportAsPNG(Canvas canvas, File file) {

        try {

            WritableImage image = canvas.snapshot(new SnapshotParameters(), null);

            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void exportAsJPG(Canvas canvas, File file) {
        WritableImage image = canvas.snapshot(null, null);

        try {
            ImageIO.write(
                    SwingFXUtils.fromFXImage(image, null),
                    "jpg",
                    file
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}