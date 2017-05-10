package com.iconasystems.android.trumeter.api;

import com.iconasystems.android.trumeter.vo.Task;

import java.util.List;

/**
 * Created by christoandrew on 11/26/16.
 */

public class TaskResponse {
    private List<Task> tasks;

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
