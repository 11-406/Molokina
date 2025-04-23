import java.util.Stack;

public class MinStack {
    private Stack<Integer> mainStack;  // Основной стек для хранения элементов
    private Stack<Integer> minStack;   // Стек для хранения минимальных значений

    public MinStack() {
        mainStack = new Stack<>();
        minStack = new Stack<>();
    }

    public void push(int x) {
        mainStack.push(x);
        if (minStack.isEmpty() || x <= minStack.peek()) {
            minStack.push(x);
        }
    }

    public void pop() {
        if (mainStack.isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        if (mainStack.peek().equals(minStack.peek())) {
            minStack.pop();
        }
        mainStack.pop();
    }

    public int top() {
        if (mainStack.isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        return mainStack.peek();
    }

    public int getMin() {
        if (minStack.isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        return minStack.peek();
    }

    public boolean isEmpty() {
        return mainStack.isEmpty();
    }
}