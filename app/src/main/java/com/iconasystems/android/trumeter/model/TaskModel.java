package com.iconasystems.android.trumeter.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import com.iconasystems.android.trumeter.config.App;
import com.iconasystems.android.trumeter.vo.Task;
import com.iconasystems.android.trumeter.vo.Task$Table;
import com.raizlabs.android.dbflow.runtime.TransactionManager;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import static com.iconasystems.android.trumeter.AppPreferences.PREFS_NAME;

/**
 * Created by christoandrew on 11/27/16.
 */

public class TaskModel extends BaseModel {
    private static final String PREF_NAME = "task_pref";
    private static final String KEY_LAST_TASK_TIMESTAMP = "timestamp";
    private static final String KEY_LOCAL_TASK_ID = "local_task_id";
    private SharedPreferences mPrefs;

    @Inject
    Context mAppContext;

    public TaskModel(App app, SQLiteDatabase database) {
        super(app, database);
    }

    public List<Task> loadTasks(int meterReaderId) {
        List<Task> tasks =  new Select().distinct().from(Task.class).where(
                Condition.column(Task$Table.METER_READER_ID).eq(meterReaderId)
        ).queryList();

        Set<Task> taskHashSet = new HashSet<>();
        for (Task task : tasks){
            taskHashSet.add(task);
        }

        List<Task> tasksFiltered = new ArrayList<>(taskHashSet);
        for (Task task : taskHashSet){
            tasksFiltered.add(task);
        }

        return tasksFiltered;
    }

    public Task load(int taskId){
        return new Select().from(Task.class)
                .where(Condition.column(Task$Table.ID).eq(taskId)).querySingle();
    }

    public synchronized void saveAll(final List<Task> tasks) {
        if (tasks.isEmpty()) {
            return;
        }
        TransactionManager.transact(mSQLiteDatabase, new Runnable() {
            @Override
            public void run() {
                for (Task task : tasks) {
                    saveValid(task);
                }
            }
        });
    }

    private void saveValid(Task task) {
        Task existing = loadTaskById(task.getId());
        if (existing == null) {
            task.save();
        } else {
            task.setId(existing.getId());
            task.update();
        }
    }

    @Nullable
    public synchronized Task loadTaskById(int taskId) {
        if (StringUtils.isEmpty(String.valueOf(taskId))) {
            return null;
        }
        return new Select().from(Task.class)
                .where(Condition.column(Task$Table.ID).eq(taskId))
                .querySingle();
    }

    private SharedPreferences getPref() {
        return mAppContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public String getLatestTimestamp(int readerId) {
        return getPref().getString(createTaskTimestampKey(readerId),null);
    }

    public synchronized long generateIdForNewLocalTask() {
        long id = getPref().getLong(KEY_LOCAL_TASK_ID, Long.MIN_VALUE);
        getPref().edit().putLong(KEY_LOCAL_TASK_ID, id + 1).apply();
        return id;
    }

    public void saveTaskTimestamp(String timestamp, int readerId) {
        getPref().edit().putString(createTaskTimestampKey(readerId), timestamp).apply();
    }

    private static String createTaskTimestampKey(int readerId) {
        return KEY_LAST_TASK_TIMESTAMP + "_" + readerId;
    }

    public void clear() {
        getPref().edit().clear().apply();
    }

    public void delete(Task task) {
        task.delete();
    }


}
