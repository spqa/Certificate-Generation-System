/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package system.Course;

/**
 *
 * @author c1409l3544
 */
public class Course {
    private int id;
    private String name;
    private float money;

    public Course() {
    }

    public Course(int id, String name, float money) {
        this.id = id;
        this.name = name;
        this.money = money;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getMoney() {
        return money;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return name;
    }
    
}
