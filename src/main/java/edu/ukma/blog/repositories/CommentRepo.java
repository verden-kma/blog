package edu.ukma.blog.repositories;

import edu.ukma.blog.models.Comment;
import edu.ukma.blog.models.compositeIDs.CommentID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepo extends JpaRepository<Comment, CommentID> {
}
