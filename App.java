import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        
        new Start();

    }
}

class Start {   
    public Start() {
        GetAllPrizes gap = new GetAllPrizes();
        Map<Toys, Integer> prizes = new HashMap<>();
        Toys toy = new Toys("bear", 2, 30);
        Toys toy2 = new Toys("wolf", 5, 40);
        Toys toy3 = new Toys("fox", 3, 30);
        ArrayList<Toys> toysArray = new ArrayList<>();
        Scanner in = new Scanner(System.in);
        System.out.println("Сколько игрушек нужно розыграть?");
        int count = in.nextInt();
        toysArray.add(toy);
        toysArray.add(toy2);
        toysArray.add(toy3);
        System.out.println(toysArray);
        ToysLottery tl = new ToysLottery();
        Toys prize = tl.getPrizeToy(toysArray);
        while (count > 0) {
            toysArray = tl.removePrizeToy(toysArray, prize);
            gap.savingPrizes(prize, prizes);
            prize = tl.getPrizeToy(toysArray);
            count--;
        }
        System.out.println("All prizes: ");
        System.out.println(gap.getPrizesMap(prizes));
        System.out.println(toysArray);

    }  
}

class GetAllPrizes{
    public GetAllPrizes() {
    }
    public Map<Toys, Integer> savingPrizes(Toys prizeToy, Map<Toys, Integer> prizesMap){
        if (prizesMap.containsKey(prizeToy)) {
            prizesMap.put(prizeToy, prizesMap.get(prizeToy) + 1);
        }
        else {
            prizesMap.put(prizeToy, 1);
        }
        return prizesMap;
    }

    public String getPrizesMap(Map<Toys, Integer> prizesMap){
        StringBuilder sb = new StringBuilder();
        for (Toys t : prizesMap.keySet()) {
            sb.append(t.getName());
            sb.append(" - " + prizesMap.get(t) + "\n");
        }
        try{
        Path file = Path.of("Prizes.txt");
        Files.writeString(file, sb);
        } catch (IOException e) {
            System.out.println("Неудачно");
        }
        
        return sb.toString();
    }
}

class Toys {
    private int id = 0;
    private static int uniq = 0;
    private String name;
    private int count;
    private int  probability;

    public Toys(String name, int count, int probability) {
        this.name = name;
        this.count = count;
        this.probability = probability;
        this.id = getUniqId();
    }
    private int getUniqId(){
        return this.uniq++;
    }

    public int getId(){
        return this.id;
    }

    @Override
    public String toString() {
        return "ID: " + id + " - " + "Игрушка: " + name + ", остаток " + count + " шт.";
    }

    public int getCount(){
        return count;
    }
    public String getName(){
        return name;
    }
    public int getProbability(){
        return probability;
    }
    public int editCount(int count){
        this.count = count - 1;
        return this.count;
    }
}

class ToysLottery {
    public ToysLottery() {
    }

    Random rand = new Random();
    
    public int chanceNumber(int probability){
        return rand.nextInt(probability);
    }

    public Toys getPrizeToy(ArrayList<Toys> array) {
        int maxChance = chanceNumber(array.get(0).getProbability());
        Toys prizeToy = array.get(0);
        for (int i = 1; i < array.size(); i++) {
            int curChance = chanceNumber(array.get(i).getProbability());
            if (maxChance < curChance) {
                maxChance = curChance;
                prizeToy = array.get(i);
            }
        }
        return prizeToy;
    }

    public ArrayList<Toys> removePrizeToy (ArrayList<Toys> array, Toys prizeToy) {
        for (int i = 0; i < array.size(); i++) {
            if (prizeToy.getId() == array.get(i).getId()){
                prizeToy.editCount(prizeToy.getCount());
                array.remove(i);
                array.add(i, prizeToy);
                if (array.get(i).getCount() == 0){
                    array.remove(array.get(i));
                }
            }
        }
        return array;
    }

}