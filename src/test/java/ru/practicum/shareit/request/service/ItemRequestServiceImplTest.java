package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.exception.InvalidRequestException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {

    private ItemRequestService itemRequestService;
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRequestMapper itemRequestMapper;

    @BeforeEach
    public void setUp() {
        itemRequestService = new ItemRequestServiceImpl(itemRequestRepository, userRepository, itemRequestMapper);
    }

    @Test
    void createItemRequestTest() {
        ItemRequestDto requestDtoOne = ItemRequestDto.builder()
                .userId(1L)
                .description("Нужен фотоаппарат")
                .build();
        ItemRequest requestOne = ItemRequest.builder()
                .userId(1L)
                .description("Нужен фотоаппарат")
                .build();

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRequestRepository.save(any())).thenReturn(requestOne);
        when(itemRequestMapper.itemRequestToDto(any())).thenReturn(requestDtoOne);

        ItemRequestDto response = itemRequestService.createItemRequest(requestDtoOne, 1L);
        assertEquals(response, requestDtoOne);
    }

    @Test
    void createItemRequestThrowsObjectNotFoundExceptionTest() {
        ItemRequestDto requestDtoOne = ItemRequestDto.builder()
                .userId(1L)
                .description("Нужен фотоаппарат")
                .build();

        when(userRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(ObjectNotFoundException.class, () -> itemRequestService.createItemRequest(requestDtoOne, 1L));
    }

    @Test
    void getAllUserRequestsTest() {
        ItemRequestDto requestDtoOne = ItemRequestDto.builder()
                .userId(1L)
                .description("Нужен фотоаппарат")
                .build();
        ItemRequest requestOne = ItemRequest.builder()
                .userId(1L)
                .description("Нужен фотоаппарат")
                .build();

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRequestRepository.getByUserIdOrderByCreatedAsc(anyLong())).thenReturn(List.of(requestOne));
        when(itemRequestMapper.itemRequestToDto(any(ItemRequest.class))).thenReturn(requestDtoOne);

        Collection<ItemRequestDto> response = itemRequestService.getAllUserRequests(1L);
        assertEquals(response.size(), 1);
        assert (response.contains(requestDtoOne));
    }

    @Test
    void getAllUserRequestsThrowsObjectNotFoundExceptionTest() {
        when(userRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(ObjectNotFoundException.class, () -> itemRequestService.getAllUserRequests(1L));

    }

    @Test
    void getAllRequestsTest() {
        ItemRequestDto requestDtoOne = ItemRequestDto.builder()
                .userId(1L)
                .description("Нужен фотоаппарат")
                .build();
        ItemRequest requestOne = ItemRequest.builder()
                .userId(1L)
                .description("Нужен фотоаппарат")
                .build();

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRequestRepository.findAllByOrderByCreatedAsc(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(requestOne)));
        when(itemRequestMapper.itemRequestToDto(any(ItemRequest.class))).thenReturn(requestDtoOne);

        Collection<ItemRequestDto> response = itemRequestService.getAllRequests(0, 2, 2L);
        assertEquals(response.size(), 1);
        assert (response.contains(requestDtoOne));
    }

    @Test
    void getAllRequestsThrowsObjectNotFoundExceptionTest() {
        when(userRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(ObjectNotFoundException.class, () -> itemRequestService.getAllRequests(0, 2, 2L));
    }

    @Test
    void getAllRequestsThrowsInvalidRequestExceptionSizeTest() {
        assertThrows(InvalidRequestException.class, () -> itemRequestService.getAllRequests(0, 0, 2L));
    }

    @Test
    void getAllRequestsThrowsInvalidRequestExceptionFromTest() {
        assertThrows(InvalidRequestException.class, () -> itemRequestService.getAllRequests(-1, 2, 2L));
    }


    @Test
    void getItemRequestTest() {
        ItemRequestDto requestDtoOne = ItemRequestDto.builder()
                .userId(1L)
                .description("Нужен фотоаппарат")
                .build();
        ItemRequest requestOne = ItemRequest.builder()
                .userId(1L)
                .description("Нужен фотоаппарат")
                .build();

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRequestRepository.existsById(anyLong())).thenReturn(true);
        when(itemRequestRepository.getReferenceById(anyLong())).thenReturn(requestOne);
        when(itemRequestMapper.itemRequestToDto(any(ItemRequest.class))).thenReturn(requestDtoOne);

        ItemRequestDto response = itemRequestService.getItemRequest(1L, 1L);
        assertEquals(response, requestDtoOne);
    }

    @Test
    void getItemRequestThrowsObjectNotFoundExceptionForUserTest() {
        when(userRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(ObjectNotFoundException.class, () -> itemRequestService.getItemRequest(1L, 1L));
    }

    @Test
    void getItemRequestThrowsObjectNotFoundExceptionForRequestTest() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRequestRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(ObjectNotFoundException.class, () -> itemRequestService.getItemRequest(1L, 1L));
    }
}