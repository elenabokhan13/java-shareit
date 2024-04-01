package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.item.ItemController.USER_ID;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {
    @Autowired
    RequestClient requestClient;


    @PostMapping
    public ResponseEntity<Object> createRequest(@Valid @RequestBody ItemRequestDto request,
                                                @RequestHeader(USER_ID) Long userId) {
        log.info("Получен запрос к эндпойнту /requests для создания реквеста");
        return requestClient.createItemRequest(request, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUserRequests(@RequestHeader(USER_ID) Long userId) {
        log.info("Получен запрос к эндпойнту /requests для получения списка реквестов пользователя");
        return requestClient.getAllUserRequests(userId);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<Object> getAllRequests(@PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                 @Positive @RequestParam(defaultValue = "10") int size,
                                                 @RequestHeader(USER_ID) Long userId) {
        log.info("Получен запрос к эндпойнту /requests для получения списка всех реквестов");
        return requestClient.getAllRequests(from, size, userId);
    }

    @GetMapping(value = "/{requestId}")
    public ResponseEntity<Object> getItemRequest(@PathVariable Long requestId, @RequestHeader(USER_ID) Long userId) {
        log.info("Получен запрос к эндпойнту /requests для получения реквеста по id {}", requestId);
        return requestClient.getItemRequest(requestId, userId);
    }
}
