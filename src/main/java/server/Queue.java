package server;

public class Queue<E> {
    private Node first = null;
    private Node last = null;
    private int counter = 0;

    public void add(E obj){
        if (counter == 0){
            first = new Node(obj);
            last = first;
        }else{
            last.next = new Node(obj);
            last = last.next;
        }
        counter++;

    }

    public boolean hasNext(){
        return first != null;
    }

    public E next(){
        Node firstNode = first;
        first = first.next;
        counter--;
        return firstNode.obj;
    }

    public int getCounter(){
        return counter;
    }

    private class Node{
        E obj;
        Node next;

        Node(E obj){
            this.obj = obj;
            next = null;
        }
    }
}
