package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.CommentService;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.Collection;


@RestController
@RequestMapping("/items")
@Slf4j
public class ItemController {
    public static final String USER_ID = "X-Sharer-User-Id";
    ItemService itemService;
    CommentService commentService;

    @Autowired
    public ItemController(ItemService itemService, CommentService commentService) {
        this.itemService = itemService;
        this.commentService = commentService;
    }

    @PostMapping
    public ItemDto createItem(@Valid @RequestBody ItemDto itemDto, @RequestHeader(USER_ID) Long userId) {
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
    public Collection<ItemDto> getItemsByUser(@RequestHeader(USER_ID) Long userId) {
        log.info("Получен запрос к эндпойнту /items для получения предметов пользователя по id {}", userId);
        return itemService.getItemsByUser(userId);
    }

    @GetMapping(value = "/search")
    public Collection<ItemDto> searchItems(@RequestParam String text) {
        log.info("Получен запрос к эндпойнту /items для поиска предметов по запросу {}", text);
        return itemService.searchItems(text);
    }

    @PostMapping(value = "/{itemId}/comment")
    public CommentDto postComment(@RequestHeader(USER_ID) Long userId, @PathVariable Long itemId,
                                  @Valid @RequestBody CommentDto commentDto) {
        log.info("Получен запрос к эндпойнту /items для размещения комментария к предмету {}", itemId);
        return commentService.postComment(userId, itemId, commentDto);
    }
}
