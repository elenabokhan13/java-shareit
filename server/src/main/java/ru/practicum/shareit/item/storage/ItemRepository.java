package ru.practicum.shareit.item.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findByOwnerId(Long userId, Pageable pageable);

    Page<Item> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String textName, String textDescription,
                                                                               Pageable pageable);
}
