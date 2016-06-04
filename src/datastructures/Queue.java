package datastructures;

import java.util.Arrays;

public class Queue {
    private int capacity = 10;
    private Object[] data = new Object[capacity];
    private int top, bottom;


    public static void main(String[] args) {
        Queue queue = new Queue();
        queue.enqueue(1);
        queue.enqueue(1);
        queue.enqueue(1);
        queue.enqueue(1);
        queue.enqueue(1);
        queue.enqueue(1);
        queue.enqueue(1);
        queue.enqueue(1);
        queue.enqueue(1);
        queue.enqueue(1);
        queue.dequeue();
        queue.dequeue();
        queue.enqueue(2);
        queue.enqueue(2);
    }

    public boolean isEmpty() {
        return top == bottom && data[bottom] == null;
    }
    
    public boolean isFull() {
        return top == bottom && data[bottom] != null;
    }

    public Object dequeue() {
        if (isEmpty()) {
            return null;
        }
        Object retVal = data[top];
        data[top] = null;
        top = (top + 1) % capacity;
        return retVal;
    }

    public void enqueue(Object object) {
        if (isFull()) {
            copy();
        }
        data[bottom] = object;
        bottom = (bottom + 1) % capacity;
    }

    private void copy() {
        int newCapacity = capacity << 1;
        Object[] newData = new Object[newCapacity];
        int j = 0;
        for (int i = top; j < capacity; i = (i + 1) % capacity, j++) {
            newData[j] = data[i];
            data[i] = null;
        }
        capacity = newCapacity;
        data = newData;
        top = 0;
        bottom = j;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Queue{");
        sb.append("capacity=").append(capacity);
        sb.append(", data=").append(Arrays.toString(data));
        sb.append(", top=").append(top);
        sb.append(", bottom=").append(bottom);
        sb.append(", empty=").append(isEmpty());
        sb.append(", full=").append(isFull());
        sb.append(", queue=").append(dequeue());
        sb.append('}');
        return sb.toString();
    }
}
