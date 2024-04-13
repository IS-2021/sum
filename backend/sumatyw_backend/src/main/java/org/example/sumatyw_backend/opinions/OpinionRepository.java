package org.example.sumatyw_backend.opinions;

import org.example.sumatyw_backend.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OpinionRepository extends JpaRepository<Opinion, UUID> {

        List<Opinion> findAllByUser(User user);
}
