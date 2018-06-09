package com.onlinetest.vuquang.filemanager.data.manager;

import com.onlinetest.vuquang.filemanager.data.model.action.FileAction;

import java.util.Stack;

/**
 * Created by VuQuang on 6/9/2018.
 */

public class ActionManager {
    private Stack<FileAction> actions = new Stack<>();
    private Stack<FileAction> undoActions = new Stack<>();

    public boolean addAction(FileAction action) {
        if(action.execute()) {
            actions.push(action);
            undoActions.clear();
            return true;
        }
        return false;
    }

    public boolean undo() {
        if(canUndo()) {
            FileAction action = actions.peek();
            if(action.undo()) {
                actions.pop();
                undoActions.push(action);
                return true;
            }
        }
        return false;
    }

    public boolean redo() {
        if(canRedo()) {
            FileAction action = undoActions.peek();
            if(action.execute()) {
                actions.push(action);
                undoActions.pop();
                return true;
            }
        }
        return false;
    }

    public boolean canUndo() {
        return !actions.isEmpty();
    }

    public boolean canRedo() {
        return !undoActions.isEmpty();
    }
}
