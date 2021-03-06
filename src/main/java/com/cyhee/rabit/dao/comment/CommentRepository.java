
package com.cyhee.rabit.dao.comment;

import com.cyhee.rabit.model.cmm.ContentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cyhee.rabit.model.cmm.ContentType;
import com.cyhee.rabit.model.comment.Comment;

// This will be AUTO IMPLEMENTED by Spring into a Bean called commentRepository
// CRUD refers Create, Read, Update, Delete

public interface CommentRepository extends PagingAndSortingRepository<Comment, Long> {
	Page<Comment> findByTypeAndParentIdAndStatusNot(ContentType type, Long parentId, ContentStatus notStatus, Pageable pageable);

	Page<Comment> findByStatusNot(ContentStatus notStatus, Pageable pageable);
}
