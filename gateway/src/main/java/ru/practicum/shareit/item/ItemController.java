package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.constant.Constant.USER_ID;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@Valid @RequestBody ItemDto itemDto, @RequestHeader(USER_ID) Long userId) {
        log.info("Получен запрос к эндпойнту /items для создания предмета");
        return itemClient.createItem(itemDto, userId);
    }

    @PatchMapping(value = "/{itemId}")
    public ResponseEntity<Object> updateItem(@PathVariable Long itemId, @RequestBody ItemDto itemDto,
                                             @RequestHeader(USER_ID) Long userId) {
        log.info("Получен запрос к эндпойнту /items для обновления предмета по id {}", itemId);
        return itemClient.updateItem(itemId, itemDto, userId);
    }

    @GetMapping(value = "/{itemId}")
    public ResponseEntity<Object> getItem(@PathVariable Long itemId, @RequestHeader(USER_ID) Long userId) {
        log.info("Получен запрос к эндпойнту /items для получения предмета по id {}", itemId);
        return itemClient.getItem(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemsByUser(@RequestHeader(USER_ID) Long userId,
                                                 @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                 @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Получен запрос к эндпойнту /items для получения предметов пользователя по id {}", userId);
        return itemClient.getItemsByUser(userId, from, size);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<Object> searchItems(@RequestHeader(USER_ID) Long userId, @RequestParam String text,
                                              @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                              @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Получен запрос к эндпойнту /items для поиска предметов по запросу {}", text);
        return itemClient.searchItems(text, userId, from, size);
    }

    @PostMapping(value = "/{itemId}/comment")
    public ResponseEntity<Object> postComment(@RequestHeader(USER_ID) Long userId, @PathVariable Long itemId,
                                              @Valid @RequestBody CommentDto commentDto) {
        log.info("Получен запрос к эндпойнту /items для размещения комментария к предмету {}", itemId);
        return itemClient.addComment(userId, itemId, commentDto);
    }
}
