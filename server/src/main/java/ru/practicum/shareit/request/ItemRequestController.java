package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.Collection;

import static ru.practicum.shareit.constant.Constant.USER_ID;


@RestController
@RequestMapping(path = "/requests")
@Slf4j
public class ItemRequestController {
    ItemRequestService itemRequestService;

    @Autowired
    public ItemRequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @PostMapping
    public ItemRequestDto createRequest(@RequestBody ItemRequestDto request,
                                        @RequestHeader(USER_ID) Long userId) {
        log.info("Получен запрос к эндпойнту /requests для создания реквеста");
        return itemRequestService.createItemRequest(request, userId);
    }

    @GetMapping
    public Collection<ItemRequestDto> getAllUserRequests(@RequestHeader(USER_ID) Long userId) {
        log.info("Получен запрос к эндпойнту /requests для получения списка реквестов пользователя");
        return itemRequestService.getAllUserRequests(userId);
    }

    @GetMapping(value = "/all")
    public Collection<ItemRequestDto> getAllRequests(@RequestParam(defaultValue = "0") int from,
                                                     @RequestParam(defaultValue = "10") int size,
                                                     @RequestHeader(USER_ID) Long userId) {
        log.info("Получен запрос к эндпойнту /requests для получения списка всех реквестов");
        return itemRequestService.getAllRequests(from, size, userId);
    }

    @GetMapping(value = "/{requestId}")
    public ItemRequestDto getItemRequest(@PathVariable Long requestId, @RequestHeader(USER_ID) Long userId) {
        log.info("Получен запрос к эндпойнту /requests для получения реквеста по id {}", requestId);
        return itemRequestService.getItemRequest(requestId, userId);
    }
}
