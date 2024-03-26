package ru.practicum.shareit.request.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.InvalidRequestException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.storage.UserRepository;

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
        if (!userRepository.existsById(userId)) {
            throw new ObjectNotFoundException("Данный пользователь не существет");
        }
        LocalDateTime currentTime = LocalDateTime.now();
        request.setUserId(userId);
        request.setCreated(currentTime);
        return itemRequestMapper.itemRequestToDto(itemRequestRepository
                .save(itemRequestMapper.itemRequestFromDto(request)));
    }

    @Override
    public Collection<ItemRequestDto> getAllUserRequests(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ObjectNotFoundException("Данный пользователь не существет");
        }
        return itemRequestRepository.getByUserIdOrderByCreatedAsc(userId).stream()
                .map(itemRequestMapper::itemRequestToDto).collect(Collectors.toList());
    }

    @Override
    public Collection<ItemRequestDto> getAllRequests(int from, int size, Long userId) {
        if (size < 1) {
            throw new InvalidRequestException("Размер выборки должен быть не меньше одного");
        }
        if (from < 0) {
            throw new InvalidRequestException("Сортировка элементов не может быть с отрицательного индекса");
        }
        if (!userRepository.existsById(userId)) {
            throw new ObjectNotFoundException("Данный пользователь не существет");
        }
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size);
        return itemRequestRepository.findAllByOrderByCreatedAsc(pageable).stream()
                .filter(x -> !Objects.equals(x.getUserId(), userId))
                .map(itemRequestMapper::itemRequestToDto).collect(Collectors.toList());

    }

    @Override
    public ItemRequestDto getItemRequest(Long requestId, Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ObjectNotFoundException("Данный пользователь не существет");
        }
        if (!itemRequestRepository.existsById(requestId)) {
            throw new ObjectNotFoundException("Данный реквест не существет");
        }
        return itemRequestMapper.itemRequestToDto(itemRequestRepository.getReferenceById(requestId));
    }
}
