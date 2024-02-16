package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemStorage {
    Item createItem(Item item);

    Item updateItem(Long itemId, String itemName, String itemDescription, Boolean available);

    Collection<Item> getItems();

    Item getItemById(Long itemId);
}
