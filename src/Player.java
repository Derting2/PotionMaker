import java.util.*;

public class Player {
    String clientmessage = "Ожидаем клиента...";
    Boolean beclient = false;
    Integer money = 0;
    Random random = new Random();
    Integer newrecipecost = 100;
    double reputation = 3000;

    String newrecipename = "";

    int cost = 50;

    List<String> potionmaker = new ArrayList<>();

    List<String> replics = new ArrayList<>(List.of(
            "Приветствую, мастер алхимии! Мои походные запасы совсем опустели. Продай мне, пожалуйста, ",
            "Эй, уважаемый, у меня сегодня намечается очень сложный денек... Выдай мне из своих запасов ",
            "Слушай, уважаемый, у меня сегодня квест повышенной сложности. Выдай мне из своих запасов спасительное ",
            "Доброго дня, колдун! Надвигается знатная буря, а мой походный мешок совсем пуст. Есть ли у тебя в наличии ",
            "Мое почтение, мастер! Денек обещает быть просто адским, так что отсыпь мне из своих резервов ",
            "Приветствую, зельевар! Сегодня намечается такая заварушка, что без твоей магии я не протяну и часа. Налей-ка мне ",
            "Уважаемый, не томи, выдай товар прямо из-под прилавка! Мне срочно нужно купить у тебя ",
            "Приветствую, великий смеситель! Моя мана на нуле, а дорога предстоит долгая. Налей мне во флакон ",
            "Привет, алхимик. Силы на исходе, а впереди еще битва с боссом. Продай мне то уникальное ",
            "Эй, мастер котла! Жизнь бьет по всем фронтам, так что отпусти мне, пожалуйста, свой лучший "

    ));

    Map<String, List<String>> recipes = new HashMap<>(Map.of(
            "Зелье лунного света", new ArrayList<>(List.of("Селениум", "Люминолит"))
    ));

    Map<String, List<String>> allrecipes = new HashMap<>(Map.ofEntries(
            Map.entry("Зелье невесомости", new ArrayList<>(List.of("Астрофит", "Аэролит"))),
            Map.entry("Зелье шторма", new ArrayList<>(List.of("Вольтрит", "Акваверде"))),
            Map.entry("Зелье плазмы", new ArrayList<>(List.of("Вольтрит", "Пирокласт"))),
            Map.entry("Зелье ядовитых спор", new ArrayList<>(List.of("Некроцит", "Хлорофит"))),
            Map.entry("Зелье остывшей лавы", new ArrayList<>(List.of("Магмион", "Селениум"))),
            Map.entry("Зелье штиля", new ArrayList<>(List.of("Аэролит", "Акваверде"))),
            Map.entry("Зелье neon-волны", new ArrayList<>(List.of("Люминолит", "Акваверде"))),
            Map.entry("Зелье чистой ауры", new ArrayList<>(List.of("Люминолит", "Аэролит"))),
            Map.entry("Зелье космической бури", new ArrayList<>(List.of("Астрофит", "Вольтрит"))),
            Map.entry("Зелье черной дыры", new ArrayList<>(List.of("Астрофит", "Магмион"))),
            Map.entry("Зелье полярного сияния", new ArrayList<>(List.of("Селениум", "Вольтрит"))),
            Map.entry("Зелье тумана", new ArrayList<>(List.of("Селениум", "Аэролит"))),
            Map.entry("Зелье серебряной росы", new ArrayList<>(List.of("Селениум", "Хлорофит"))),
            Map.entry("Зелье бледной немочи", new ArrayList<>(List.of("Селениум", "Некроцит"))),
            Map.entry("Зелье адского пламени", new ArrayList<>(List.of("Пирокласт", "Магмион"))),
            Map.entry("Зелье пепла", new ArrayList<>(List.of("Пирокласт", "Некроцит"))),
            Map.entry("Зелье молнии", new ArrayList<>(List.of("Вольтрит", "Аэролит"))),
            Map.entry("Зелье цепной реакции", new ArrayList<>(List.of("Вольтрит", "Хлорофит"))),
            Map.entry("Зелье дыма", new ArrayList<>(List.of("Магмион", "Аэролит"))),
            Map.entry("Зелье бездны", new ArrayList<>(List.of("Магмион", "Некроцит")))
    ));

    Map<String, Integer> inventory = new HashMap<>(Map.of(
            "Люминолит", 3,
            "Селениум", 3
    ));

    Map<String, Integer> crystals = new HashMap<>(Map.ofEntries(
            Map.entry("Астрофит", 40),
            Map.entry("Пирокласт", 38),
            Map.entry("Хлорофит", 35),
            Map.entry("Акваверде", 32),
            Map.entry("Некроцит", 28),
            Map.entry("Магмион", 25),
            Map.entry("Вольтрит", 23),
            Map.entry("Аэролит", 21),
            Map.entry("Люминолит", 20),
            Map.entry("Селениум", 20)
    ));

    String makedpotion = "";
    String clientpotion = "";

    void comeclient(){
        beclient = true;
        var keysList = new ArrayList<>(recipes.keySet());
        if (keysList.size() == 1) {
            int number = 0;
            clientmessage = "Клиент: " + replics.get(random.nextInt(0, replics.size() - 1)) + keysList.get(number);
            clientpotion = keysList.get(number);
        } else {
            int number = random.nextInt(0, keysList.size());
            clientmessage = "Клиент: " + replics.get(random.nextInt(0, replics.size() - 1)) + keysList.get(number);
            clientpotion = keysList.get(number);
        }
    }

    void clientgooutDone(){
        money += Math.round(coinsforreputation(cost, reputation / 1000));
        var keysList = new ArrayList<>(recipes.keySet());
        reputation += getreputation(keysList.size() - 1);
        if (reputation >= 10000){
            reputation = 10000;
        }
        beclient = false;
        clientmessage = "Ожидаем клиента...";
    }

    void clientgoout(){
        reputation -= 1000;
        if (reputation <= 1000){
            reputation = 1000;
        }
        beclient = false;
        clientmessage = "Ожидаем клиента...";
    }

    void newrecipe(){
        if (allrecipes.size() != 1) {
            var keysList = new ArrayList<>(allrecipes.keySet());
            int randrecipe = random.nextInt(0, keysList.size());
            recipes.put(keysList.get(randrecipe), allrecipes.get(keysList.get(randrecipe)));
            System.out.println(recipes);
            allrecipes.remove(keysList.get(randrecipe));
            newrecipename = keysList.get(randrecipe);
            money -= newrecipecost;
            newrecipecost += 100;
        } else {
            var keysList = new ArrayList<>(allrecipes.keySet());
            recipes.put(keysList.get(0), allrecipes.get(keysList.get(0)));
            newrecipename = keysList.get(0);
            allrecipes.remove(keysList.get(0));
            money -= newrecipecost;
        }
    }

    int coinsforreputation(int baseCoins, double reputation) {
        double r = Math.max(1.0, Math.min(10.0, reputation));

        double multiplier;
        if (r <= 3.0) {
            multiplier = 0.5 + (r - 1.0) * 0.25;
        } else {
            multiplier = 1.0 + (r - 3.0) * (2.0 / 7.0);
        }

        return (int) Math.round(baseCoins * multiplier);
    }

    int getreputation(int recipeCount) {
        if (recipeCount < 0) recipeCount = 0;
        if (recipeCount > 20) recipeCount = 20;

        return 50 + (int) (2.5 * recipeCount);
    }

    int getclientspawninterval(double reputation) {
        double r = Math.max(1.0, Math.min(10.0, reputation));

        double interval;
        if (r <= 3.0) {
            interval = 60.0 - 10.0 * (r - 1.0);
        } else {
            interval = 40.0 - (20.0 / 7.0) * (r - 3.0);
        }

        return (int) Math.round(interval);
    }
}