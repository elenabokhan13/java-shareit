package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.CommentService;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

import static ru.practicum.shareit.constant.Constant.USER_ID;


@RestController
@RequestMapping("/items")
@Slf4j
public class ItemController {
    private final ItemService itemService;
    private final CommentService commentService;

    @Autowired
    public ItemController(ItemService itemService, CommentService commentService) {
        this.itemService = itemService;
        this.commentService = commentService;
    }

    @PostMapping
    public ItemDto createItem(@RequestBody ItemDto itemDto, @RequestHeader(USER_ID) Long userId) {
        log.info("Получен запрос к эндпойнту /items для создания предмета");
        return itemService.createItem(itemDto, userId);
    }

    @PatchMapping(value = "/{itemId}")
    public ItemDto updateItem(@PathVariable Long itemId, @RequestBody ItemDto itemDto,
                              @RequestHeader(USER_ID) Long userId) {
        log.info("Получен запрос к эндпойнту /items для обновления предмета по id {}", itemId);
        return itemService.updateItem(itemId, itemDto, userId);
    }

    @GetMapping(value = "/{itemId}")
    public ItemDto getItem(@PathVariable Long itemId, @RequestHeader(USER_ID) Long userId) {
        log.info("Получен запрос к эндпойнту /items для получения предмета по id {}", itemId);
        return itemService.getItem(itemId, userId);
    }

    @GetMapping
    public Collection<ItemDto> getItemsByUser(@RequestHeader(USER_ID) Long userId,
                                              @RequestParam(defaultValue = "0") int from,
                                              @RequestParam(defaultValue = "10") int size) {
        log.info("Получен запрос к эндпойнту /items для получения предметов пользователя по id {}", userId);
        return itemService.getItemsByUser(userId, from, size);
    }

    @GetMapping(value = "/search")
    public Collection<ItemDto> searchItems(@RequestParam String text,
                                           @RequestParam(defaultValue = "0") int from,
                                           @RequestParam(defaultValue = "10") int size) {
        log.info("Получен запрос к эндпойнту /items для поиска предметов по запросу {}", text);
        return itemService.searchItems(text, from, size);
    }

    @PostMapping(value = "/{itemId}/comment")
    public CommentDto postComment(@RequestHeader(USER_ID) Long userId, @PathVariable Long itemId,
                                  @RequestBody CommentDto commentDto) {
        log.info("Получен запрос к эндпойнту /items для размещения комментария к предмету {}", itemId);
        return commentService.addComment(userId, itemId, commentDto);
    }
}
