package org.tomw.bidirectionallist;

import java.util.ArrayList;
import java.util.List;

/**
 * Bidirectional list of objects
 * @param <T> type of object
 */
public class BiList<T> {
    private List<T> content = new ArrayList<>();
    private int currentIndex = -1;

    public BiList() {
        List<T> list = new ArrayList<>();
        content.addAll(list);
        if (content.size() > 0) {
            currentIndex = 0;
        }
    }

    public BiList(List<T> list) {
        content.addAll(list);
        if (content.size() > 0) {
            currentIndex = 0;
        }
    }

    /**
     * get all objects in list as a regular list
     * @return list of objects
     */
    public List<T> getContent() {
        return content;
    }

    /**
     * add element and position next to current index
     *
     * @param element to be added
     */
    public void add(T element) {
        currentIndex++;
        content.add(currentIndex, element);
    }

    /**
     * get object to at current cursor position
     * @return object at cursor
     */
    public T getCurrent(){
        if(!isEmpty()) {
            return content.get(currentIndex);
        }else {
            return null;
        }
    }

    /**
     * get current cursor value
     * @return cursor
     */
    public int getCurrentIndex(){
        return currentIndex;
    }

    /**
     * remove object at current index. Shift others to left.
     * It his was the last object, then current intext is the new last index
     *
     * @return deleted object
     */
    public T removeCurrent() {
        T result = content.remove(currentIndex);
        if(currentIndex>content.size()-1){
            currentIndex=currentIndex-1;
        }
        return result;
    }

    /**
     * is the buffer empty?
     * @return yes if it is empty
     */
    public boolean isEmpty(){
        return content.isEmpty();
    }

    /**
     * can move to right?
     * @return true if yes
     */
    public boolean canMoveToRight(){
        return !isLast() && !isEmpty();
    }

    /**
     * is this last element in list?
     * @return true if yes
     */
    public boolean isLast(){
        return currentIndex==content.size()-1 && !isEmpty();
    }

    /**
     * can move to the left?
     * @return true if yes
     */
    public boolean canMoveToLeft(){
        return !isFirst() && !isEmpty();
    }

    /**
     * is this first element in list?
     * @return true if yes
     */
    public boolean isFirst(){
        return currentIndex==0;
    }

    /**
     * move one step to right if possible
     */
    public void moveRight(){
        if(canMoveToRight()){
            currentIndex++;
        }
    }

    /**
     * move one step to left if possible
     */
    public void moveLeft(){
        if(canMoveToLeft()){
            currentIndex--;
        }
    }

    /**
     * delete content
     */
    public void clear(){
        content.clear();
        currentIndex=-1;
    }

}
