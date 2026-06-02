package server.service;

import server.db.DrawingRepository;
import shared.model.DrawEvent;

public class DrawService {

    private final DrawingRepository drawingRepository;

    public DrawService(DrawingRepository drawingRepository) {
        this.drawingRepository = drawingRepository;
    }

    public void processDrawEvent(DrawEvent event) {

        validate(event);

        drawingRepository.saveDrawEvent(event);

    }

    private void validate(DrawEvent event) {

        if (event == null) {
            throw new IllegalArgumentException("DrawEvent cannot be null");
        }
    }
}