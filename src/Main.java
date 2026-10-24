Scanner scan = new Scanner(System.in);
Player pl = new Player();
Console cons = new Console(pl);
Random random = new Random();
Boolean ongamepage = false;
private Thread clientTimerThread;

volatile boolean clientServed = false;

public static volatile boolean stopInputSignal = false;

void main() {
    System.out.println();
    System.out.println();
    menu();
}

void menu(){
    ongamepage = false;
    while (true){
        System.out.println();
        cons.ShowStartPage();
        System.out.print("Выберите действие: ");
        String answer = scan.next();
        answer = answer.toLowerCase();
        Boolean getanswer = false;
        cons.showerror = false;
        switch (answer){
            case "и":
            case "играть":
                getanswer = true;
                game();
                break;
            case "о":
            case "обучение":
                tutorial();
                getanswer = true;
                break;
            case "в":
            case "выйти":
                 return;
            default:
                cons.showerror = true;
        }
        if (getanswer) {
            break;
        }
    }
}

void tutorial(){
    while (true){
        System.out.println("");
        cons.ShowTutorialPage();
        System.out.print("Выберите действие: ");
        String answer = scan.next().toLowerCase();

        switch (answer){
            case "в":
            case "выйти":
                menu();
                return;
            default:
                cons.showerror = true;
        }
    }
}

void inventory(){
    ongamepage = false;
    while (true){
        System.out.println("");
        cons.ShowInventory();
        System.out.print("Выберите действие: ");
        String answer = scan.next().toLowerCase();

        if (answer.equals("в") || answer.equals("выйти")) {
            return;
        }
        cons.showerror = true;
    }
}

void recipes(){
    ongamepage = false;
    while (true){
        System.out.println("");
        cons.ShowRecipes();
        System.out.print("Выберите действие: ");
        String answer = scan.next().toLowerCase();

        if (answer.equals("в") || answer.equals("выйти")) {
            return;
        }

        cons.showerror = true;
    }
}

void TimerNewClient(){
    try{
        TimeUnit.SECONDS.sleep(pl.getclientspawninterval(pl.reputation / 1000));
    } catch (InterruptedException e){
        System.out.println("Таймер сломался");
    }

    pl.comeclient();

    if (ongamepage) {
        stopInputSignal = true;
    }

    clientgoout();
}

void game() {

    while (true) {
        ongamepage = true;
        boolean oldThreadAlive = clientTimerThread != null && clientTimerThread.isAlive();


        if (!pl.beclient && !stopInputSignal && !oldThreadAlive) {
            stopInputSignal = false;

            clientTimerThread = new Thread(() -> TimerNewClient());
            clientTimerThread.start();
        }

        System.out.println("");
        cons.ShowBaseGame();
        System.out.print("Выберите действие: ");

        String answer = "";
        boolean forcedExit = false;

        try {
            while (System.in.available() == 0) {
                if (stopInputSignal) {
                    forcedExit = true;
                    break;
                }
                Thread.sleep(50);
            }

            if (forcedExit) {
                stopInputSignal = false;
                continue;
            }

            answer = scan.next().toLowerCase();

        } catch (Exception e) {
            e.printStackTrace();
        }

        cons.showerror = false;

        switch (answer) {
            case "и":
            case "инвентарь":
                inventory();
                break;
            case "р":
            case "рецепты":
                recipes();
                break;
            case "в":
            case "варить зелье":
                makepotions();
                break;
            case "п":
            case "продать зелье":
                if (pl.beclient == true){
                    sellpotion();
                }
                break;
            case "м":
            case "магазин":
                shopmenu();
                break;
            case "выйти":
                return;
            default:
                cons.showerror = true;
        }
    }
}

void makepotions(){
    ongamepage = false;
    pl.makedpotion = "";
    pl.potionmaker = new ArrayList<>(List.of("", ""));
    int stage = 0;

    while (true){

        System.out.println("");
        cons.ShowMakerPotion(stage);

        if (stage >= 2){
            System.out.print("Выберите действие: ");
        } else if (stage <= 1){
            System.out.print("Напишите название кристалла, который хотите добавить: ");
        }

        String answer = scan.next().toLowerCase();
        cons.showerror = false;


        if (answer.equals("в") || answer.equals("выйти")) {
            return;
        }

        var keysList = new ArrayList<>(pl.inventory.keySet());

        if (stage <= 1) {
            boolean itemFound = false;


            for (int i = 0; i < keysList.size(); i++) {
                String currentKey = keysList.get(i);

                if (answer.equals(currentKey.toLowerCase())) {
                    pl.potionmaker.set(stage, currentKey);
                    stage += 1;
                    itemFound = true;
                    break;
                }
            }


            if (!itemFound) {
                cons.showerror = true;
            }

        } else if (stage == 2){
            if (answer.equals("вр") || answer.equals("варить")){
                int count = 0;
                var keysListi = new ArrayList<>(pl.recipes.keySet());
                List<String> elements;
                if (!pl.potionmaker.get(0).equals(pl.potionmaker.get(1))) {
                    for (int i = 0; i < pl.recipes.size(); i++) {
                        elements = pl.recipes.get(keysListi.get(i));

                        for (int j = 0; j < 2; j++) {
                            if (elements.contains(pl.potionmaker.get(j))) {
                                count += 1;
                            }
                        }

                        if (count == 2) {
                            pl.makedpotion = String.valueOf(keysListi.get(i));
                            addIninventory(elements);
                            break;
                        }
                    }
                } else {
                    if (pl.inventory.get(pl.potionmaker.getFirst()) >= 2){
                        if (count <= 1){
                            pl.makedpotion = "Какашка";
                            addIninventory(pl.potionmaker);
                        }

                    } else {
                        cons.showerror = true;
                        cons.anotherproblem = "nocrystals";
                        stage -= 1;
                    }
                }
                stage += 1;
            } else {
                cons.showerror = true;
            }
        } else if (stage == 3){
            if (!answer.equals("в") || !answer.equals("выйти")) {
                cons.showerror = true;
            }
        }
    }
}

void addIninventory(List<String> elements){
    for (int i = 0; i < elements.size(); i++) {
        pl.inventory.put(elements.get(i), pl.inventory.get(elements.get(i)) - 1);
        if (pl.inventory.get(elements.get(i)) == 0){
            pl.inventory.remove(elements.get(i));
        }
    }

    if (pl.inventory.containsKey(pl.makedpotion)){
        pl.inventory.put(pl.makedpotion, pl.inventory.get(pl.makedpotion) + 1);
    } else {
        pl.inventory.put(pl.makedpotion, 1);
    }
}

void sellpotion(){
    if (pl.inventory.containsKey(pl.clientpotion)) {
        pl.inventory.put(pl.clientpotion, pl.inventory.get(pl.clientpotion) - 1);

        if (pl.inventory.get(pl.clientpotion) == 0) {
            pl.inventory.remove(pl.clientpotion);
        }

        clientServed = true;
        pl.clientgooutDone();

        if (ongamepage) {
            stopInputSignal = true;
        }

        if (clientTimerThread != null && clientTimerThread.isAlive()) {
            clientTimerThread.interrupt();
        }
    }
}

void shopmenu(){
    ongamepage = false;
    while (true){
        System.out.println("");
        cons.ShowShopMenu();
        System.out.print("Выберите действие: ");
        String answer = scan.next().toLowerCase();

        switch (answer){
            case "к":
            case "кристаллы":
                showcrystalshop();
                return;
            case "р":
            case "рецепты":
                showrecipeshop();
                return;
            case "в":
            case "выйти":
                return;
            default:
                cons.showerror = true;
        }
    }
}

void showcrystalshop(){
    ongamepage = false;
    while (true){
        System.out.println("");
        cons.ShowShopCrystal();
        System.out.print("Какой кристалл вы хотите купить?: ");
        String answer = scan.next().toLowerCase();

        if (answer.equals("в") || answer.equals("выйти")) {
            return;
        }

        var keysListl = new ArrayList<>(pl.crystals.keySet());

        for (int i = 0; i < keysListl.size(); i++) {
            keysListl.set(i, keysListl.get(i).toLowerCase());
        }
        String spanswer = answer + ": ";
        System.out.println(answer.toLowerCase() + " " + keysListl.getFirst() + " " +  keysListl.getLast());
        if (keysListl.contains(spanswer)) {
            String key = answer.substring(0, 1).toUpperCase() + answer.substring(1);
            String spkey = spanswer.substring(0, 1).toUpperCase() + spanswer.substring(1);
            if (pl.money >= pl.crystals.get(spkey)) {
                pl.money -= pl.crystals.get(spkey);
                if (pl.inventory.containsKey(key)){
                    pl.inventory.put(key, pl.inventory.get(key) + 1);
                } else {
                    pl.inventory.put(key, 1);
                }
                return;
            } else {
                cons.showerror = true;
                cons.anotherproblem = "nomoney";
            }
        }
    }
}

void showrecipeshop(){
    ongamepage = false;
    while (true){
        System.out.println("");
        cons.ShowShopRecipes();
        System.out.print("Выберите действие: ");
        String answer = scan.next().toLowerCase();

        if (answer.equals("в") || answer.equals("выйти")) {
            return;
        } else if (answer.equals("к") || answer.equals("крутить")){
            if (pl.allrecipes.size() != 0) {
                if (pl.money >= pl.newrecipecost) {
                    pl.newrecipe();
                    cons.showerror = true;
                    cons.anotherproblem = "-money";
                } else {
                    cons.showerror = true;
                    cons.anotherproblem = "nomoney";
                }
            } else {
                cons.showerror = true;
            }
        } else {
            cons.showerror = true;
        }
    }
}

void clientgoout(){
    try {

        TimeUnit.SECONDS.sleep(20);
    } catch (InterruptedException e) {

        if (clientServed) {
            if (ongamepage) {
                stopInputSignal = true;
            }
            clientServed = false;
            return;
        }

        System.out.println("Таймер отменен. Клиент остается.");
        if (ongamepage) {
            stopInputSignal = true;
        }
        return;
    }


    if (ongamepage) {
        stopInputSignal = true;
    }
    pl.clientgoout();
}