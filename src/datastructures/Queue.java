package datastructures;

import datastructures.graphs.Graph;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

public class Queue<T> {
    private int capacity = 10;
    private T[] data = (T[]) new Object[capacity];
    private int top, bottom;

    public Queue(Set<T> set) {
        set.forEach(this::enqueue);
    }

    public Queue() {}


    public static void main(String[] args) {
        Queue<Integer> queue = new Queue<>();
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

    @Contract(pure = true)
    private boolean isFull() {
        return top == bottom && data[bottom] != null;
    }

    public final @Nullable T dequeue() {
        if (isEmpty()) {
            return null;
        }
        T retVal = data[top];
        data[top] = null;
        top = (top + 1) % capacity;
        return retVal;
    }

    public final void enqueue(T object) {
        if (isFull()) {
            copy();
        }
        data[bottom] = object;
        bottom = (bottom + 1) % capacity;
    }

    private void copy() {
        int newCapacity = capacity << 1;
        T[] newData = (T[]) new Object[newCapacity];
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

    public boolean contains(T obj) {
        for (T t : data) {
            if (Objects.equals(obj, t)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public final @NotNull String toString() {
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
