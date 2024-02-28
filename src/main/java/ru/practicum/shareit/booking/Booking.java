package ru.practicum.shareit.booking;

import lombok.Data;

import java.util.Date;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class Booking {
    Long id;
    Long itemId;
    Date startDate;
    Date endDate;
    String ownerApproval;
    String userFeedback;
}
