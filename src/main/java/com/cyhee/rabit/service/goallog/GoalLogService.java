
package com.cyhee.rabit.service.goallog;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cyhee.rabit.model.goallog.GoalLog;

public interface GoalLogService {
	Page<GoalLog> getGoalLogs(Pageable pageable);
	
	void addGoalLog(GoalLog log);
	
	GoalLog getGoalLog(long id);
	
	void updateGoalLog(long id, GoalLog goallogForm);
	
	void deleteGoalLog(long id);
}
