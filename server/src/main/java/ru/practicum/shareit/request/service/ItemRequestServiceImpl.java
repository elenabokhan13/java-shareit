package ru.practicum.shareit.request.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.storage.UserRepository;
import ru.practicum.shareit.validator.Validator;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRequestMapper itemRequestMapper;

    @Autowired
    public ItemRequestServiceImpl(ItemRequestRepository itemRequestRepository, UserRepository userRepository,
                                  ItemRequestMapper itemRequestMapper) {
        this.itemRequestRepository = itemRequestRepository;
        this.userRepository = userRepository;
        this.itemRequestMapper = itemRequestMapper;
    }

    @Override
    public ItemRequestDto createItemRequest(ItemRequestDto request, Long userId) {
        Validator.validateUser(userRepository, userId);
        LocalDateTime currentTime = LocalDateTime.now();
        request.setUserId(userId);
        request.setCreated(currentTime);
        return itemRequestMapper.itemRequestToDto(itemRequestRepository
                .save(itemRequestMapper.itemRequestFromDto(request)));
    }

    @Override
    public Collection<ItemRequestDto> getAllUserRequests(Long userId) {
        Validator.validateUser(userRepository, userId);
        return itemRequestRepository.getByUserIdOrderByCreatedAsc(userId).stream()
                .map(itemRequestMapper::itemRequestToDto).collect(Collectors.toList());
    }

    @Override
    public Collection<ItemRequestDto> getAllRequests(int from, int size, Long userId) {
        Validator.validateSizeAndFrom(from, size);
        Validator.validateUser(userRepository, userId);
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size);
        return itemRequestRepository.findAllByOrderByCreatedAsc(pageable).stream()
                .filter(x -> !Objects.equals(x.getUserId(), userId))
                .map(itemRequestMapper::itemRequestToDto).collect(Collectors.toList());
    }

    @Override
    public ItemRequestDto getItemRequest(Long requestId, Long userId) {
        Validator.validateUser(userRepository, userId);
        Validator.validateRequest(itemRequestRepository, requestId);
        return itemRequestMapper.itemRequestToDto(itemRequestRepository.getReferenceById(requestId));
    }
}
