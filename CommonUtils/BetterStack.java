package CommonUtils;

import java.util.EmptyStackException;

/**
 * @implNote Implement a stack using an array with initial capacity 8.
 *
 * Implement BetterStackInterface and add a constructor
 *
 * You are explicitly forbidden from using java.util.Stack and any
 * other java.util.* library EXCEPT java.util.EmptyStackException and java.util.Arrays.
 * Write your own implementation of a Stack.
 *
 *
 * @param <E> the type of object this stack will be holding
 */
public class BetterStack<E> implements BetterStackInterface<E> {

    /**
     * Initial size of stack.  Do not decrease capacity below this value.
     */
    private final int INIT_CAPACITY = 8;

    /**
     * If the array needs to increase in size, it should be increased to
     * old capacity * INCREASE_FACTOR.
     *
     * If it cannot increase by that much (old capacity * INCREASE_FACTOR > max int),
     * it should increase by CONSTANT_INCREMENT.
     *
     * If that can't be done either throw OutOfMemoryError()
     *
     */
    private final int INCREASE_FACTOR = 2;
    private final int CONSTANT_INCREMENT = 1 << 5; // 32


    /**
     * If the number of elements stored is < capacity * DECREASE_FACTOR, it should decrease
     * the capacity of the UDS to max(capacity * DECREASE_FACTOR, initial capacity).
     *
     */
    private final double DECREASE_FACTOR = 0.5;


    /**
     * Array to store elements in (according to the implementation
     * note in the class header comment).
     */
    private E[] stack;
    private int capacity;
    private int tail;


    /**
     * Constructs an empty stack
     */
    @SuppressWarnings("unchecked")
    public BetterStack(){
        stack = (E[]) new Object[INIT_CAPACITY];
        capacity = INIT_CAPACITY;
        tail = 0;
    }


    /**
     * Push an item onto the top of the stack
     *
     * @param item item to push
     * @throws OutOfMemoryError if the underlying data structure cannot hold any more elements
     */
    @Override
    public void push(E item) throws OutOfMemoryError {


        if (item == null) {
            throw (new NullPointerException());
        }
        if (size() == capacity - 1) {
            stack = increaseSize();
        }
        stack[tail] = item;
        tail++;
    }

    /**
     * Remove and return the top item on the stack
     *
     * @return the top of the stack
     * @throws EmptyStackException if stack is empty
     */
    @Override
    public E pop() {

        if (isEmpty()) {
            throw(new EmptyStackException());
        }

        E item = stack[tail - 1];

        stack[tail - 1] = null;
        tail--;

        if ((capacity * DECREASE_FACTOR >= INIT_CAPACITY) && (size() < capacity * DECREASE_FACTOR)) {
            stack = decreaseSize();
        }

        return item;
    }

    /**
     * Returns the top of the stack (does not remove it).
     *
     * @return the top of the stack
     * @throws EmptyStackException if stack is empty
     */
    @Override
    public E peek() {
        if (isEmpty()) {
            throw(new EmptyStackException());
        }
        else {
            return stack[tail - 1];
        }
    }
    private E[] increaseSize() {
        E[] newStack;
        int j = 0;

        if (capacity * INCREASE_FACTOR < 0) { //Check if capacity will overflow maximum integer value
            if (capacity + CONSTANT_INCREMENT < 0) {
                throw (new OutOfMemoryError());
            }
            newStack = (E[]) new Object[capacity + CONSTANT_INCREMENT];
        }
        else {
            newStack = (E[]) new Object[capacity * INCREASE_FACTOR];
        }

        for (int i = 0; i < tail; i++) {
            newStack[j++] = stack[i];
        }
        tail = capacity - 1;
        capacity *= INCREASE_FACTOR;
        stack = null;

        return newStack;
    }

    private E[] decreaseSize() {

        E[] newStack = (E[]) new Object[(int) (capacity * DECREASE_FACTOR)];
        int j = 0;
        for (int i = 0; i < tail; i++) {
            newStack[j++] = stack[i];
        }
        capacity *= DECREASE_FACTOR;
        stack = null;
        tail = capacity - 1;

        return newStack;
    }

    /**
     * Returns whether the stack is empty
     *
     * @return true if the stack is empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        if (tail == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns the number of elements in the stack
     *
     * @return integer representing the number of elements in the stack
     */
    @Override
    public int size() {
        return tail;
    }

    public int getTail() {
        return tail;
    }

    public int getCapacity() {
        return capacity;
    }

    /**
     * DO NOT MODIFY NOR IMPLEMENT THIS FUNCTION
     *
     * @param g graphics object to draw on
     */
    @Override
    public void draw(java.awt.Graphics g) {
        //DO NOT MODIFY NOR IMPLEMENT THIS FUNCTION
        if(g != null) g.getColor();
        //todo GRAPHICS DEVELOPER:: draw the stack how we discussed
        //251 STUDENTS:: YOU ARE NOT THE GRAPHICS DEVELOPER!
    }
}
