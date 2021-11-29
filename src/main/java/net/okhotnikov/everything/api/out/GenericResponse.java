package net.okhotnikov.everything.api.out;

public class GenericResponse <T> {
    public T value;

    public GenericResponse() {
    }

    public GenericResponse(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
