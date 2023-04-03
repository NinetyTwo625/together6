package com.together.domain.image;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Integer> {
    @Query(value = "SELECT * FROM image WHERE userId IN (SELECT toUserId FROM subscribe WHERE fromUserId = :principalId) OR userId = :principalId ORDER BY id DESC", nativeQuery = true)
    Page<Image> mStory(int principalId, Pageable pageable);

    @Query(value = "SELECT i.* FROM image i INNER JOIN (SELECT imageId, COUNT(imageId) likeCount FROM likes GROUP BY imageId) c ON i.id = c.imageId ORDER BY likeCount DESC", nativeQuery = true)
    List<Image> mPopular();

    void deleteAllByUserId(int userId);

    @Query(value = "SELECT id, caption, postImageUrl, createDate, userId, \r\n"
            + "(SELECT COUNT(*) from likes where imageId = :imageId)likeCount, \r\n"
            + "(SELECT EXISTS (SELECT id FROM likes WHERE userId = :principalId and imageId = :imageId))likeState\r\n"
            + "FROM image WHERE id = :imageId", nativeQuery = true)
    Image imageDetail(@Param("imageId") int imageId, @Param("principalId") int principalId);

    void deleteById(int imageId);
}