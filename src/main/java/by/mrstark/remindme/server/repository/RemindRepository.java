package by.mrstark.remindme.server.repository;

import by.mrstark.remindme.server.entity.Remind;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by mrstark on 26.2.16.
 */
public interface RemindRepository extends JpaRepository<Remind, Long> {
}
