package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.AccessForbidenError;
import ru.practicum.shareit.exception.InvalidRequestException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;
    private final ItemMapper itemMapper;

    @Autowired
    public ItemServiceImpl(ItemStorage itemStorage, UserStorage userStorage, ItemMapper itemMapper) {
        this.userStorage = userStorage;
        this.itemStorage = itemStorage;
        this.itemMapper = itemMapper;
    }

    @Override
    public ItemDto createItem(ItemDto itemDto, Long userId) {
        if (userId == null) {
            throw new InvalidRequestException("Не указан id пользователя");
        }
        if (userStorage.getUserById(userId) == null) {
            throw new ObjectNotFoundException("Данный пользователь не существет");
        }
        Item item = itemMapper.dtoToItem(itemDto);
        item.setOwnerId(userId);
        return itemMapper.itemToDto(itemStorage.createItem(item));
    }

    @Override
    public ItemDto updateItem(Long itemId, ItemDto itemDto, Long userId) {
        if (userId == null) {
            throw new InvalidRequestException("Не указан id пользователя");
        }
        Item item = itemStorage.getItemById(itemId);
        if (!Objects.equals(item.getOwnerId(), userId)) {
            throw new AccessForbidenError("У данного пользователя нет доступа к редактированию этого предмета.");
        }
        return itemMapper.itemToDto(itemStorage.updateItem(itemId, itemDto.getName(), itemDto.getDescription(),
                itemDto.getAvailable()));
    }

    @Override
    public ItemDto getItem(Long itemId) {
        return itemMapper.itemToDto(itemStorage.getItemById(itemId));
    }

    @Override
    public Collection<ItemDto> getItemsByUser(Long userId) {
        return itemStorage.getItemsByUser(userId).stream().map(itemMapper::itemToDto).collect(Collectors.toList());
    }

    @Override
    public Collection<ItemDto> searchItems(String text) {
        if (Objects.equals(text, "")) {
            return new ArrayList<>();
        }
        return itemStorage.searchItems(text).stream().map(itemMapper::itemToDto).collect(Collectors.toList());
    }
}
