package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.request.dto.ItemRequestDtoToSave;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestControllerTest {
    @Mock
    private RequestClient requestClient;

    @InjectMocks
    private ItemRequestController itemRequestController;

    ItemRequestDtoToSave requestDtoToSave = new ItemRequestDtoToSave("save Description");

    @Test
    void create() {
        long userId = 1;
        ResponseEntity<Object> exceptedEntity = new ResponseEntity<>(requestDtoToSave, HttpStatus.OK);
        when(requestClient.create(userId, requestDtoToSave)).thenReturn(exceptedEntity);

        ResponseEntity<Object> response = itemRequestController.create(userId, requestDtoToSave);

        verify(requestClient, times(1)).create(userId, requestDtoToSave);
        assertEquals(exceptedEntity.getStatusCode(), response.getStatusCode());
        assertEquals(exceptedEntity.getClass(), response.getClass());
        assertEquals(exceptedEntity.getHeaders(), response.getHeaders());
        assertEquals(exceptedEntity.getBody(), response.getBody());
    }

    @Test
    void findAllByUserId() {
        long userId = 1;
        ResponseEntity<Object> exceptedEntity = new ResponseEntity<>(requestDtoToSave, HttpStatus.OK);
        when(requestClient.findAllByUserId(userId)).thenReturn(exceptedEntity);

        ResponseEntity<Object> response = itemRequestController.findAllByUserId(userId);

        verify(requestClient, times(1)).findAllByUserId(userId);
        assertEquals(exceptedEntity.getStatusCode(), response.getStatusCode());
        assertEquals(exceptedEntity.getClass(), response.getClass());
        assertEquals(exceptedEntity.getHeaders(), response.getHeaders());
        assertEquals(exceptedEntity.getBody(), response.getBody());
    }

    @Test
    void findById() {
        long userId = 1;
        long requestId = 1;
        ResponseEntity<Object> exceptedEntity = new ResponseEntity<>(requestDtoToSave, HttpStatus.OK);
        when(requestClient.findById(userId, requestId)).thenReturn(exceptedEntity);

        ResponseEntity<Object> response = itemRequestController.findById(userId, requestId);

        verify(requestClient, times(1)).findById(userId, requestId);
        assertEquals(exceptedEntity.getStatusCode(), response.getStatusCode());
        assertEquals(exceptedEntity.getClass(), response.getClass());
        assertEquals(exceptedEntity.getHeaders(), response.getHeaders());
        assertEquals(exceptedEntity.getBody(), response.getBody());
    }

    @Test
    void findAllById() {
        long userId = 1;
        int from = 0;
        int size = 1;
        ResponseEntity<Object> exceptedEntity = new ResponseEntity<>(requestDtoToSave, HttpStatus.OK);
        when(requestClient.findAllByUserIdToPageable(userId, from, size)).thenReturn(exceptedEntity);

        ResponseEntity<Object> response = itemRequestController.findAllById(userId, from, size);

        verify(requestClient, times(1)).findAllByUserIdToPageable(userId, from, size);
        assertEquals(exceptedEntity.getStatusCode(), response.getStatusCode());
        assertEquals(exceptedEntity.getClass(), response.getClass());
        assertEquals(exceptedEntity.getHeaders(), response.getHeaders());
        assertEquals(exceptedEntity.getBody(), response.getBody());
    }
}