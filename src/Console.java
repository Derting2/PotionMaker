import java.util.ArrayList;
import java.util.List;

public class Console {
    private Player pl;

    String anotherproblem = "";

    public Console(Player player) {
        this.pl = player;
    }

    Boolean showerror = false;

    void wrong(){
        if (showerror == true){
            System.out.println("Неправильно ввели действие. Введите правильный вариант действия");
            showerror = false;
        }
    }

    void ShowStartPage(){
        List<String> text = new ArrayList<>(List.of(
                "Быстрый ввод написан в скобках, если он есть. Пример: Играть (И)",
                "Добро пожаловать в игру Potion Maker!",
                "Играть (И)",
                "Обучение (О)",
                "Выйти (В)"
        ));

        for (int i = 0; i < text.size(); i++) {
            System.out.println(text.get(i));
        }

        wrong();
    }

    void ShowTutorialPage(){
        List<String> text = new ArrayList<>(List.of(
                "Добро пожаловать в игру Potion Maker!",
                "Это быстрый туториал по игре",
                "Ты алхимик, и ты должен варить зелья для клиентов, которые будут к тебе приходить.",
                "Зелья варятся из кристаллов, которые ты можешь приобрести в магазине",
                "Но как варить зелья, если я не не знаю из чего можно варить?",
                "Для этого есть рецепты, в вкладке рецептов, ты увидишь все рецепты, которые ты купил",
                "Покупаются в том же магазине, где и кристаллы.",
                "Стоит помнить, что у тебя есть репутация, которая влияет на игру",
                "Если ты пропустишь клиента, то тогда у тебя снизится репутация, так что следи за этим!",
                "И помни, чем больше рецептов открыто, тем больше репутции ты получешь",
                "Удачи в игре!"
        ));

        for (int i = 0; i < text.size(); i++) {
            System.out.println(text.get(i));
        }

        System.out.println("\nВыйти (В)");

        wrong();
    }

    void ShowBaseGame(){
        for (int i = 0; i < 3; i++) {
            System.out.println("");
        }

        List<String> text = new ArrayList<>(List.of(
                pl.clientmessage,
                "",
                "Инвентарь (И)",
                "Рецепты (Р)",
                "Магазин (М)",
                "Варить зелье (В)",
                "Продать зелье (П)",
                "",
                "Репутация: " + pl.reputation / 1000,
                "Монеты: " + pl.money
        ));

        for (int i = 0; i < text.size(); i++) {
            if (i == 6){
                if (pl.beclient == true){
                    System.out.println(text.get(i));
                }
            } else if (i != 6) {
                System.out.println(text.get(i));
            }
        }

        wrong();
    }

    void ShowInventory(){
        for (int i = 0; i < 3; i++) {
            System.out.println("");
        }

        System.out.println("Инвентарь:");

        if (pl.inventory.size() == 0){
            System.out.print("В инвентаре ничего нет :(");
        } else {
            for (int i = 0; i < Math.round((double) pl.inventory.size() / 2); i++) {
                for (int j = 0; j < 2; j++) {
                    var keysList = new ArrayList<>(pl.inventory.keySet());
                    int index = j + (2 * i);
                    if (index < keysList.size()) {
                        String key = keysList.get(index);
                        Integer value = pl.inventory.get(key);
                        System.out.print(key + ": " + value);
                        System.out.print(" ".repeat(5));
                    } else {
                        break;
                    }
                }
                System.out.println("");
            }

        }
        System.out.println("\nВыйти (В)");

    }

    void ShowRecipes(){
        for (int i = 0; i < 3; i++) {
            System.out.println("");
        }

        System.out.println("Рецепты:");

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 3; j++) {
                var keysList = new ArrayList<>(pl.recipes.keySet());
                int index = j + (3 * i);
                if (index < keysList.size()) {
                    String key = keysList.get(index);
                    List<String> value = pl.recipes.get(key);
                    System.out.print(key + " = " + value.get(0) + " + " + value.get(1));
                    System.out.print(" ".repeat(5));
                } else {
                    System.out.print("???");
                    System.out.print(" ".repeat(5));
                }
            }
            System.out.println("");
        }
        System.out.println("\nВыйти (В)");
    }

    void ShowMakerPotion(Integer stage){
        for (int i = 0; i < 3; i++) {
            System.out.println("");
        }

        System.out.println("Варка зельев:");

        switch(stage){
            case 0:
                System.out.println("??? + ??? = ???");
                break;
            case 1:
                System.out.println(pl.potionmaker.get(0) + " + ??? = ???");
                break;
            case 2:
                System.out.println(pl.potionmaker.get(0) + " + " + pl.potionmaker.get(1) + " = ???");
                System.out.println("\nВарить (Вр)");
                break;
            case 3:
                System.out.println("У вас получилось " + pl.makedpotion);
                break;
        }

        System.out.println("Выйти (В)");
        if (stage < 2) {
            if (showerror == true){
                System.out.println("Такого кристалла в инвентаре нету");
            }
        } else if (anotherproblem.equals("nocrystals")){
            if (showerror == true){
                System.out.println("Недостаточно кристаллов");
                anotherproblem = "";
            }
        } else {
            wrong();
        }
    }

    void ShowShopMenu(){
        for (int i = 0; i < 3; i++) {
            System.out.println("");
        }
        System.out.println("Магазин: ");

        System.out.println("Кристаллы (К)");
        System.out.println("Рецепты (Р)");

        System.out.println("Выйти (В)");

        wrong();
    }

    void ShowShopCrystal(){
        for (int i = 0; i < 3; i++) {
            System.out.println("");
        }
        System.out.println("Магазин кристаллов: ");

        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 2; j++) {
                int index = j + (2 * i);
                var keyList = new ArrayList<>(pl.crystals.keySet());
                String key = keyList.get(index);
                System.out.print(key + pl.crystals.get(key) + " Монет" + " ".repeat(5));
            }
            System.out.println("\n");
        }

        System.out.println("Выйти (В)");
        if (anotherproblem.equals("nomoney")) {
            if (showerror == true) {
                System.out.println("Недостаточно монет");
                anotherproblem = "";
            }
        } else {
            wrong();
        }
    }

    void ShowShopRecipes(){
        for (int i = 0; i < 3; i++) {
            System.out.println("");
        }
        System.out.println("Магазин рецептов: ");

        if (pl.allrecipes.size() != 0){
            System.out.println("Купить крутку: " + pl.newrecipecost + " Монет (К)");
        } else {
            System.out.println("Вы получили все рецепты зельев!");
        }

        System.out.println("Выйти (В)");

        if (anotherproblem.equals("nomoney")) {
            if (showerror == true) {
                System.out.println("Недостаточно монет");
                anotherproblem = "";
            }
        } else if (anotherproblem.equals("-money")) {
            if (showerror == true) {
                System.out.println("Вы получили рецепт " + pl.newrecipename);
                anotherproblem = "";
            }
        } else {
            wrong();
        }
    }
}
