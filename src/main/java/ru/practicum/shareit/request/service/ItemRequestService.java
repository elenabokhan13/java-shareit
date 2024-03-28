package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Collection;

public interface ItemRequestService {
    ItemRequestDto createItemRequest(ItemRequestDto request, Long userId);

    Collection<ItemRequestDto> getAllUserRequests(Long userId);

    Collection<ItemRequestDto> getAllRequests(int from, int size, Long userId);

    ItemRequestDto getItemRequest(Long requestId, Long userId);
}
