package com.only.test.Design.iterator;

public class Test {

    public static void main(String[] args) {
        ConcreteContainer concreteContainer = new ConcreteContainer();
        concreteContainer.add(1);
        concreteContainer.add(2);
        Iterator iterator = concreteContainer.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

}
