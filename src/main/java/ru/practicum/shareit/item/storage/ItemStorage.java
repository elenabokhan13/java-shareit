package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemStorage {
    Item createItem(Item item);

    Item updateItem(Long itemId, String itemName, String itemDescription, Boolean available);

    Collection<Item> getItems();

    Collection<Item> getItemsByUser(Long userId);

    Collection<Item> searchItems(String text);

    Item getItemById(Long itemId);
}
