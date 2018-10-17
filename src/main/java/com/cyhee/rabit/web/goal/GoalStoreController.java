package com.cyhee.rabit.web.goal;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cyhee.rabit.model.cmm.ContentStatus;
import com.cyhee.rabit.model.cmm.ContentType;
import com.cyhee.rabit.model.comment.Comment;
import com.cyhee.rabit.model.goal.Goal;
import com.cyhee.rabit.model.goallog.GoalLog;
import com.cyhee.rabit.model.like.Like;
import com.cyhee.rabit.model.user.User;
import com.cyhee.rabit.service.cmm.AuthHelper;
import com.cyhee.rabit.service.comment.CommentService;
import com.cyhee.rabit.service.goal.CompanionService;
import com.cyhee.rabit.service.goal.GoalService;
import com.cyhee.rabit.service.goal.GoalStoreService;
import com.cyhee.rabit.service.goallog.GoalLogService;
import com.cyhee.rabit.service.like.LikeService;
import com.cyhee.rabit.service.user.UserService;

@RestController
@RequestMapping("rest/v1/goals/{id}")
public class GoalStoreController {
	@Autowired
	private GoalService goalService;
	@Autowired
	private GoalStoreService goalStoreService;
	@Autowired
	private UserService userService;
	@Autowired
	private LikeService likeService;
	@Autowired
	private CommentService commentService;
	@Autowired
	private GoalLogService goalLogService;
	@Autowired
	private CompanionService companionService;
	
	@GetMapping("/companions")
	public ResponseEntity<Page<User>> getCompanions(@PathVariable Long id, @PageableDefault Pageable pageable) {
		Goal goal = goalService.getGoal(id);
		return ResponseEntity.ok(companionService.getCompanions(goal, pageable));
	}
	
	@GetMapping("/companion-goals")
	public ResponseEntity<Page<Goal>> getCompanionGoals(@PathVariable Long id, @PageableDefault Pageable pageable) {
		Goal goal = goalService.getGoal(id);
		return ResponseEntity.ok(companionService.getCompanionGoals(goal, pageable));
	}
	
	@GetMapping("/goallogs")
	public ResponseEntity<Page<GoalLog>> getGoalLogs(@PathVariable Long id, @PageableDefault Pageable pageable) {
		Goal goal = goalService.getGoal(id);
        return new ResponseEntity<>(goalStoreService.getGoalLogs(goal, ContentStatus.all(), pageable), HttpStatus.OK);
    }
	
	@PostMapping("/goallogs")
	public ResponseEntity<Void> addGoalLog(@PathVariable Long id, @RequestBody GoalLog goalLog) {
		Goal goal = goalService.getGoal(id);
    	
    	goalLog.setGoal(goal);
    	
    	goalLogService.addGoalLog(goalLog);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@GetMapping("/comments")
	public ResponseEntity<Page<Comment>> getComments(@PathVariable Long id, @PageableDefault Pageable pageable) {
		Goal goal = goalService.getGoal(id);
        return new ResponseEntity<>(goalStoreService.getComments(goal, pageable), HttpStatus.OK);
    }
	
	@PostMapping("/comments")
	public ResponseEntity<Void> addComment(@PathVariable Long id, @RequestBody Comment comment) {
		Goal goal = goalService.getGoal(id);
		
    	String username = AuthHelper.getUsername();
    	User author = userService.getUserByUsername(username);
    	
    	comment.setAuthor(author);
    	comment.setParentId(goal.getId());
    	comment.setType(ContentType.GOAL);
    	
    	commentService.addComment(comment);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

    @GetMapping("/likes")
	public ResponseEntity<Page<Like>> getLikes(@PathVariable Long id, @PageableDefault Pageable pageable) {
		Goal goal = goalService.getGoal(id);
		return new ResponseEntity<>(goalStoreService.getLikes(goal, pageable), HttpStatus.OK);
	}
    
    @PostMapping("/likes")
    public ResponseEntity<Void> addLike(@PathVariable Long id) {
		Goal goal = goalService.getGoal(id);
		
    	String username = AuthHelper.getUsername();
    	User liker = userService.getUserByUsername(username);
    	
    	likeService.addLike(goal, liker);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
