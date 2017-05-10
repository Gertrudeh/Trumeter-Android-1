package com.iconasystems.android.trumeter.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iconasystems.android.trumeter.R;
import com.iconasystems.android.trumeter.databinding.TaskListItemBinding;
import com.iconasystems.android.trumeter.vo.Task;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by christoandrew on 11/28/16.
 */

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskItemViewHolder> {
    final LayoutInflater mLayoutInflater;
    final SortedList<Task> mList;
    final Map<String, Task> mUniqueMapping = new HashMap<>();

    private Callback mCallback;

    public TasksAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mList = new SortedList<>(Task.class,
                new SortedListAdapterCallback<Task>(this) {
                    @Override
                    public int compare(Task t1, Task t2) {
                        return (int) (t1.getCreated_at() - t2.getCreated_at());
                    }

                    @SuppressWarnings("SimplifiableIfStatement")
                    @Override
                    public boolean areContentsTheSame(Task oldTask,
                                                      Task newTask) {

                        return oldTask.getRoute_name().equals(newTask.getRoute_name());
                    }

                    @Override
                    public boolean areItemsTheSame(Task task1, Task task2) {
                        return task1.getId() == task2.getId();
                    }
                });
    }

    @Override
    public TaskItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final TaskListItemBinding binding = DataBindingUtil.inflate(mLayoutInflater,
                R.layout.task_list_item, parent, false);
        TaskItemViewHolder holder = new TaskItemViewHolder(binding);
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mCallback == null) {
                    return;
                }
                Task model = binding.getModel();
                mCallback.onTaskClick(model);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(TaskItemViewHolder holder, int position) {
        Log.d("Tasks Adapter Task", mList.get(position).toString());
        holder.binding.setModel(mList.get(position));
        holder.binding.executePendingBindings();
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class TaskItemViewHolder extends RecyclerView.ViewHolder {
        public final TaskListItemBinding binding;
        public TaskItemViewHolder(TaskListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public interface Callback {
        void onTaskClick(Task task);
    }
    public long getReferenceTimestamp() {
        int size = mList.size();
        if (size == 0) {
            return 0;
        }
        return mList.get(0).getCreated_at();
    }

    public void insert(Task task) {
        String key = createKeyFor(task);
        Task existing = mUniqueMapping.put(key, task);
        if (existing == null) {
            mList.add(task);
        } else {
            int pos = mList.indexOf(existing);
            mList.updateItemAt(pos, task);
        }
    }

    public void insertAll(List<Task> items) {
        for (Task item : items) {
            insert(item);
        }
    }

    public void swapList(List<Task> items) {
        Set<String> newListKeys = new HashSet<>();
        for (Task item : items) {
            newListKeys.add(createKeyFor(item));
        }
        for (int i = mList.size() - 1; i >= 0; i--) {
            Task item = mList.get(i);
            String key = createKeyFor(item);
            if (!newListKeys.contains(key)) {
                mUniqueMapping.remove(key);
                mList.removeItemAt(i);
            }
        }
        insertAll(items);
    }

    public void removePost(Task task) {
        Task model = mUniqueMapping.remove(createKeyFor(task));
        if (model != null) {
            mList.remove(model);
        }
    }

    public void clear() {
        mList.clear();
        mUniqueMapping.clear();
    }

    private static String createKeyFor(Task task) {
        return task.compositeUniqueKey();
    }
}
