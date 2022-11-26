package ru.practicum.explore_with_me.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explore_with_me.model.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
}
