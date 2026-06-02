package client.export;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.image.BufferedImage;
import java.io.File;

public class PDFExporter {
    public void exportToPDF(Canvas canvas, File file) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            WritableImage fxImage = canvas.snapshot(null, null);
            BufferedImage awtImage = SwingFXUtils.fromFXImage(fxImage, null);

            PDImageXObject pdImage = LosslessFactory.createFromImage(document, awtImage);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                float scale = Math.min(600f / awtImage.getWidth(), 750f / awtImage.getHeight());
                float width = awtImage.getWidth() * scale;
                float height = awtImage.getHeight() * scale;

                contentStream.drawImage(pdImage, 5, 792 - height - 5, width, height);
            }
            document.save(file);
            System.out.println("PDF exported successfully to " + file.getAbsolutePath());
        } catch (Exception e) {
            System.err.println("Failed to export PDF: " + e.getMessage());
        }
    }
}