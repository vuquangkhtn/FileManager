package com.onlinetest.vuquang.filemanager.data.manager;

import com.onlinetest.vuquang.filemanager.data.model.modify.ModifyAction;

import java.util.Stack;

/**
 * Created by VuQuang on 6/9/2018.
 */

public class ModifyActionManager {
    private Stack<ModifyAction> actions = new Stack<>();
    private Stack<ModifyAction> undoActions = new Stack<>();

    public boolean addAction(ModifyAction action) {
        if(action.execute()) {
            actions.push(action);
            undoActions.clear();
            return true;
        }
        return false;
    }

    public boolean undo() {
        if(canUndo()) {
            ModifyAction action = actions.peek();
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
            ModifyAction action = undoActions.peek();
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
