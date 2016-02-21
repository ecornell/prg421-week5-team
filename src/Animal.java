/**
 * Title:          Week 5 - Program Improvement IV
 * Author:         Team B
 * Creation Date:  2016-02-19
 * Class:          PRG/421 - Roland Morales
 */

public class Animal {

    private String name;
    private String color;
    private boolean canSwim;
    private boolean canFly;
    private boolean isVertebrate;
    private int age;

    public Animal() {};

    public Animal(String name) {
        this.name = name;
    }

    public Animal(String name, String color, boolean canSwim, boolean canFly, boolean isVertebrate, int age) {
        this.name = name;
        this.color = color;
        this.canSwim = canSwim;
        this.canFly = canFly;
        this.isVertebrate = isVertebrate;
        this.age = age;
    }

    public void setName(String name) {
        this.name =  name;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean canSwim() {
        return canSwim;
    }

    public void setCanSwim(boolean canSwim) {
        this.canSwim = canSwim;
    }

    public boolean canFly() {
        return canFly;
    }

    public void setCanFly(boolean canFly) {
        this.canFly = canFly;
    }

    public boolean isVertebrate() {
        return isVertebrate;
    }

    public void setVertebrate(boolean vertebrate) {
        isVertebrate = vertebrate;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {

        return getName() + "\n"
                + " - Color         : " + getColor() + "\n"
                + " - Can Swim      : " + canSwim() + "\n"
                + " - Can Fly       : " + canFly() + "\n"
                + " - Is Vertebrate : " + isVertebrate() + "\n"
                + " - Age           : " + getAge() + " seconds old";

    }
}
