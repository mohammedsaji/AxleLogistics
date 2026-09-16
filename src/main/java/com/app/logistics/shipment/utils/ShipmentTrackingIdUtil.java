package com.app.logistics.shipment.utils;

import org.springframework.stereotype.Component;

@Component
public final class ShipmentTrackingIdUtil {

    private static final String PREFIX = "PLSTRCK";
    private static final int ID_LENGTH = 8;

    private ShipmentTrackingIdUtil() {
    }

    public static String encode(Integer id) {

        if (id == null || id < 0) {
            throw new IllegalArgumentException("Shipment ID must be a valid positive number.");
        }

        String idValue = String.valueOf(id);

        if (idValue.length() > ID_LENGTH) {
            throw new IllegalArgumentException(
                    "Shipment ID cannot contain more than " + ID_LENGTH + " digits."
            );
        }

        return PREFIX + String.format("%0" + ID_LENGTH + "d", id);
    }


    public static Integer decode(String trackingId) {

        if (trackingId == null || trackingId.isBlank()) {
            throw new IllegalArgumentException("Tracking ID cannot be null or empty.");
        }

        if (!trackingId.startsWith(PREFIX)) {
            throw new IllegalArgumentException("Invalid shipment tracking ID.");
        }

        String idValue = trackingId.substring(PREFIX.length());

        if (idValue.length() != ID_LENGTH) {
            throw new IllegalArgumentException("Invalid shipment tracking ID.");
        }

        if (!idValue.matches("\\d{" + ID_LENGTH + "}")) {
            throw new IllegalArgumentException("Invalid shipment tracking ID.");
        }

        try {
            return Integer.parseInt(idValue);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Invalid shipment tracking ID.", exception);
        }
    }
}
