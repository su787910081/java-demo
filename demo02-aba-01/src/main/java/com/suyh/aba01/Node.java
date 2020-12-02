package com.suyh.aba01;

import lombok.ToString;

@ToString
public class Node {
    public final String item;
    public Node next;
    public Node(String item){
        this.item = item;
    }
}
