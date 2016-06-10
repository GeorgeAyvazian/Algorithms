package datastructures.graphs;

import java.util.Objects;

class Vertex<T> {

    final T data;

    Vertex(T data) {
        Objects.requireNonNull(data);
        this.data = data;
    }

    @Override
    public final String toString() {
        return "Vertex{" + "data=" + data +
                '}';
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Vertex<?> vertex = (Vertex<?>) o;
        return data.equals(vertex.data);

    }

    @Override
    public final int hashCode() {
        return data.hashCode();
    }
}
