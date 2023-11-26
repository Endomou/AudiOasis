package com.project;

public class LinkedListDeque<E> {
    private Node<E> front;
    private Node<E> back;

    // Inner Node class
    class Node<E> {
        E data;
        Node<E> next;

        public Node(E data) {
            this.data = data;
        }
    }

    // Add an element to the front of the deque
    public void offerFirst(E element) {
        Node<E> newNode = new Node<>(element);
        if (isEmpty()) {
            front = back = newNode;
        } else {
            newNode.next = front;
            front = newNode;
        }
    }

    // Get the element at the specified index
    public E get(int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Index cannot be negative: " + index);
        }

        Node<E> current = front;
        for (int i = 0; i < index; i++) {
            if (current == null) {
                throw new IndexOutOfBoundsException("Index " + index + " is out of bounds");
            }
            current = current.next;
        }

        if (current == null) {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds");
        }

        return current.data;
    }

    public boolean isEmpty() {
        return front == null;
    }

    // Other deque operations

    // Add an element to the end of the deque
    public void offerLast(E element) {
        Node<E> newNode = new Node<>(element);
        if (isEmpty()) {
            front = back = newNode;
        } else {
            back.next = newNode;
            back = newNode;
        }
    }

    // Remove and return the first element from the deque
    public E pollFirst() {
        if (isEmpty()) {
            return null; // or throw an exception if you prefer
        }

        E data = front.data;
        front = front.next;

        if (front == null) {
            back = null; // The deque is now empty
        }

        return data;
    }

    // Remove and return the last element from the deque
    public E pollLast() {
        if (isEmpty()) {
            return null; // or throw an exception if you prefer
        }

        E data = back.data;

        if (front == back) {
            front = back = null; // The deque is now empty
        } else {
            Node<E> current = front;
            while (current.next != back) {
                current = current.next;
            }
            back = current;
            back.next = null;
        }

        return data;
    }
}
