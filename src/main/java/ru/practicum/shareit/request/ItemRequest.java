package ru.practicum.shareit.request;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@Data
public class ItemRequest {
    Long id;
    Long userId;
    String request;
    List<Item> responses;

}
