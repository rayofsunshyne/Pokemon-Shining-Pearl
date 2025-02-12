import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.io.*;
import java.util.*;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

public class Woo {

  static Scanner in = new Scanner(System.in); // user inputs

  // should generate a pokemon that is an instance of the type
  public static Pokemon generatePokemon(Player name, int type) {
    int random = (int) ((Math.random() * 10)); // 0-5
    String[] fire = new String[] { "Vulpix", "Slugma", "Numel", "Charmander", "Cyndaquil", "Torchic", "Ponyta", "Growlithe", "Magmar", "Flareon" };
    String[] water = new String[] { "Feebas", "Wooper", "Lotad", "Marill", "Wingull", "Poliwag", "Psyduck", "Tentacool", "Buizel", "Manaphy" };
    String[] grass = new String[] { "Cherubi", "Seedot", "Bulbasaur", "Chikorita", "Treecko", "Bellsprout" , "Roselia", "Burmy", "Oddish", "Sunkern" };
    int level = generateLvl(name);

    if (type == 0) { // 0 is fire
      String pokemon = fire[random];
      Pokemon pok = new Fire(pokemon, level, level * 100);
      return pok;
    }
    else if (type == 1) { // 1 is water
      String pokemon = water[random];
      Pokemon pok = new Water(pokemon, level, level * 100);
      return pok;
    }
    else { // 2 is grass
      String pokemon = grass[random];
      Pokemon pok = new Grass(pokemon, level, level * 100);
      return pok;
    }
  }

  // level is randomly generated. range is two levels below the starter pokemon level and two levels above
  public static int generateLvl(Player name) {
    int random = (int) ((Math.random() * 5) + ((name._pokedex[0].getLvl()) - 2));
    return random;
  }

  // type is indicated by the random number generated from 0-3
  public static int generateType() {
    int random = (int) ((Math.random() * 3));
    if (random == 0) {
      // type is Fire
      return 0;
    }
    else if (random == 1) {
      // type is Water
      return 1;
    }
    else {
      // type is Grass
      return 2;
    }
  }


  // generates name for civilian
  public static String generateName() {
    String[] name = new String[] { "Dawn", "Lucas", "Barry", "Riley", "Dr. Footstep", "Cynthia" };
    int random = (int) ((Math.random() *6));
    return name[random];
  }


  public static void wait(int s) {
    try {
      Thread.sleep(s * 500);
    }
    catch (InterruptedException ex) {
      Thread.currentThread().interrupt();
    }
  }

  public static boolean runaway() {
    int random = (int) (Math.random() * 10);
    if (random <= 5) {
      return true;
    } else {
      return false;
    }
  }

  // to make walk invoke with the same parameteres, just return false because there is a while loop in the main method making the player walk until the gym is completed
  public static boolean walk(Player name, String region) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
    File file = new File("city.au");
		AudioInputStream ais = AudioSystem.getAudioInputStream(file);
		Clip clipCity = AudioSystem.getClip();
		clipCity.open(ais);
    FloatControl gainControl = (FloatControl) clipCity.getControl(FloatControl.Type.MASTER_GAIN);
    gainControl.setValue(-30.0f);
    clipCity.start();
    clipCity.loop(Clip.LOOP_CONTINUOUSLY);

    wait(1);
    System.out.println();
    System.out.print("\u001b[38;2;180;180;180m");
    System.out.println("\nYou went on a walk...");
    wait(2);
    boolean cont = false;
    int random = (int) (Math.random() * 10); // random number from 0-9
    if (random <= 2) { // 30% chance that nothing appears
      System.out.println("\nNothing appeared.");
      wait(2);
      System.out.println();
      System.out.println("\nWalk again or go to a gym? (walk/gym)"); // if nothing appears, can go to gym or walk again
      wait(1);
      System.out.print("\u001b[38;2;255;255;255m");
      System.out.print("> ");
      System.out.print("\033[?25h"); // SHOW CURSOR
      String response = "";
      response = in.nextLine().toLowerCase();
      System.out.print("\033[?25l"); // HIDE CURSOR
      if (response.equals("walk")) {
        clipCity.close();
        return cont;
      }
      else if (response.equals("gym")) {
        if (name._numPokemon  < 4) { // cannot go to gym if player has less than four pokemon
          wait(2);
          System.out.print("\u001b[38;2;180;180;180m");
          System.out.println("\nYou cannot go to the gym with less than four Pokemon. You walk again...");
          clipCity.close();
          return cont;
        }
        else {
          boolean gym = goToGym(name, region);
          if (gym == false) {
            clipCity.close();
            goToNurse(name, region);
            return cont;
          }
          else {
            clipCity.close();
            return true;
          }
        }
      }
      else {
       wait(2);
       System.out.print("\u001b[38;2;180;180;180m");
       System.out.println("\nThat is not a valid response. You go on a walk...");
       clipCity.close();
       return false;
      }
    }
    else if (random >= 3 && random <= 4) {
      // meet someone battle is probability of 20%
      wait(2);
      System.out.print("\u001b[38;2;180;180;180m");
      System.out.println("\nA fellow Pokemon trainer appeared. They challenge you to a Pokemon battle.");
      wait(2);
      System.out.println("\nDo you want to battle or run away? (battle/run away)");
      wait(1);
      System.out.print("\u001b[38;2;255;255;255m");
      System.out.print("> ");
      System.out.print("\033[?25h"); // SHOW CURSOR
      String runawayOr = "";
      runawayOr = in.nextLine().toLowerCase();
      System.out.print("\033[?25l"); // HIDE CURSOR
      if (runawayOr.equals("run away")) {
        boolean run = runaway(); // runaway opporates on a probability. If it can run away, true is returned
        if (run == false) {
          wait(2);
          System.out.print("\u001b[38;2;180;180;180m");
          System.out.println("\nRunning away failed. You must engage in battle.");
          int type = generateType(); // generates a type for pokemon
          Pokemon pok = generatePokemon(name, type); // generates pokemon to battle
          clipCity.close();
          boolean win = battle(name, pok, region); // battles the pokemon
          goToNurse(name, region); // is lose, go to nurse
          return cont;
        }
        else {
          wait(2);
          System.out.print("\u001b[38;2;180;180;180m");
          System.out.println("\nYou sucessfully escaped!");
          wait(2);
          System.out.println();
          System.out.println("\nWalk again or go to a gym? (walk/gym)"); // if nothing appears, can go to gym or walk again
          wait(1);
          System.out.print("\u001b[38;2;255;255;255m");
          System.out.print("> ");
          System.out.print("\033[?25h"); // SHOW CURSOR
          String response = "";
          response = in.nextLine().toLowerCase();
          System.out.print("\033[?25l"); // HIDE CURSOR
          if (response.equals("walk")) {
            clipCity.close();
            return cont;
          }
          else if (response.equals("gym")) {
            if (name._numPokemon < 4) { // cannot go to gym if player has less than four pokemon
              wait(2);
              System.out.print("\u001b[38;2;180;180;180m");
              System.out.println("\nYou cannot go to the gym with less than four Pokemon. You walk again...");
              clipCity.close();
              return cont;
            }
            else {
              boolean gym = goToGym(name, region);
              if (gym == false) {
                clipCity.close();
                goToNurse(name, region);
                return cont;
              }
              else {
                return true;
              }
            }
          }
          else {
            wait(2);
            System.out.print("\u001b[38;2;180;180;180m");
            System.out.println("\nThat is not a valid response. You go on a walk...");
            clipCity.close();
            return false;
          }
          // ask
        }
      }
      else if (runawayOr.equals("battle")) {
        File file1 = new File("victory.au");
        AudioInputStream ais1 = AudioSystem.getAudioInputStream(file1);
        Clip clipVictory = AudioSystem.getClip();
        wait(2);
        System.out.print("\u001b[38;2;180;180;180m");
        System.out.println("\nYou accept the challenge and engage in battle.");
        int type = generateType();
        Pokemon pok = generatePokemon(name, type);
        clipCity.close();
        boolean win = battle(name, pok, region);
        if (win == true) {
          // clipCity.setMicrosecondPosition(0);
          // clipCity.start();
          clipVictory.open(ais1);
          FloatControl gainControlV1 = (FloatControl) clipVictory.getControl(FloatControl.Type.MASTER_GAIN);
          gainControlV1.setValue(-30.0f);
          clipVictory.start();
          clipVictory.loop(Clip.LOOP_CONTINUOUSLY);
          System.out.println();
          wait(2);
          System.out.print("\u001b[38;2;180;180;180m");
          System.out.println("\nThe trainer gave you a Pokeball for your amazing fighting!");
          wait(1);
          name.displayInventory();
          System.out.println();
          name._numPokeball = name._numPokeball + 1;
        }
        clipCity.close();
        clipVictory.close();
        goToNurse(name, region);
        return cont;
      }
      else {
        File file1 = new File("victory.au");
        AudioInputStream ais1 = AudioSystem.getAudioInputStream(file1);
        Clip clipVictory = AudioSystem.getClip();
        wait(2);
        System.out.print("\u001b[38;2;180;180;180m");
        System.out.println("\nThat is not a valid response. You engage in battle...");
        wait(2);
        System.out.println("\nYou accept the challenge and engage in battle.");
        int type = generateType();
        Pokemon pok = generatePokemon(name, type);
        clipCity.close();
        boolean win = battle(name, pok, region);
        if (win == true) {
          // clipCity.setMicrosecondPosition(0);
          // clipCity.start();
          clipVictory.open(ais1);
          FloatControl gainControlV2 = (FloatControl) clipVictory.getControl(FloatControl.Type.MASTER_GAIN);
          gainControlV2.setValue(-30.0f);
          clipVictory.start();
          clipVictory.loop(Clip.LOOP_CONTINUOUSLY);
          System.out.println();
          wait(2);
          System.out.print("\u001b[38;2;180;180;180m");
          System.out.println("\nThe trainer gave you a Pokeball for your amazing fighting!");
          wait(1);
          name.displayInventory();
          System.out.println();
          name._numPokeball = name._numPokeball + 1;
        }
        clipCity.close();
        clipVictory.close();
        goToNurse(name, region);
        return cont;
      }
    }
    else if (random >= 5 && random <= 6) {
      // meeting a civilian is probability of 20%
      Human civilian = new Human(generateName()); // civilian randomly generated
      wait(2);
      System.out.print("\u001b[38;2;180;180;180m");
      System.out.println("\nYou encounter a civilian.");
      System.out.println();
      civilian.greet(name); // civilian greets player
      wait(2);
      System.out.print("\u001b[38;2;180;180;180m");
      System.out.println("\u001b[38;2;153;153;255m" + civilian._name + " \u001b[38;2;180;180;180mgives you a Berry and a Pokeball for your efforts!");
      name._numBerries += 1;
      name._numPokeball += 1;
      // civilian gives player pokeball and berries
      civilian.goodbye(name); // civilian leaves
      name.displayInventory();
      // add extra talking functionality for stretch
      wait(2);
      System.out.print("\u001b[38;2;180;180;180m");
      System.out.println("\nDo you want to walk or go to the gym? (walk/gym)");
      wait(1);
      System.out.print("\u001b[38;2;255;255;255m");
      System.out.print("> ");
      System.out.print("\033[?25h"); // SHOW CURSOR
      String res = "";
      res = in.nextLine().toLowerCase();
      System.out.print("\033[?25l"); // HIDE CURSOR
      if (res.equals("walk")) {
        clipCity.close();
        return cont;
      }
      else if (res.equals("gym")) {
        if (name._numPokemon < 4){ // cannot go to gym if player has less than four pokemon
          wait(2);
          System.out.print("\u001b[38;2;180;180;180m");
          System.out.println("\nYou cannot go to the gym with less than four Pokemon. You walk again...");
          clipCity.close();
          return cont;
        }
        else {
          boolean gym = goToGym(name, region);
          if (gym == false) {
            clipCity.close();
            goToNurse(name, region);
            return cont;
          }
          else {
            clipCity.close();
            return true;
          }
        }
      }
      else {
        wait(2);
        System.out.print("\u001b[38;2;180;180;180m");
        System.out.println("\nThat is not a valid response. You go on a walk...");
        clipCity.close();
        return false;
      }
    }
    else {
      wait(2);
      System.out.print("\u001b[38;2;180;180;180m");
      System.out.println("\nA wild Pokemon appeared!");
      wait(2);
      System.out.println("\nWould you like to battle or run away? (battle/run away)");
      wait(1);
      System.out.print("\u001b[38;2;255;255;255m");
      System.out.print("> ");
      System.out.print("\033[?25h"); // SHOW CURSOR
      String encounterPokemon = "";
      encounterPokemon = in.nextLine().toLowerCase();
      System.out.print("\033[?25l"); // HIDE CURSOR
      if (encounterPokemon.equals("run away")) {
        boolean run = runaway();
        if (run == false) {
          wait(2);
          System.out.print("\u001b[38;2;180;180;180m");
          System.out.println("\nRunning away failed. You must engage in battle.");
          int type = generateType();
          Pokemon pok = generatePokemon(name, type);
          clipCity.close();
          boolean win = battle(name, pok, region);
          clipCity.setMicrosecondPosition(0);
          clipCity.start();
          clipCity.loop(Clip.LOOP_CONTINUOUSLY);
          if (win == false) {
            clipCity.close();
            goToNurse(name, region);
            return cont;
          }
          else {
            File file1 = new File("victory.au");
            AudioInputStream ais1 = AudioSystem.getAudioInputStream(file1);
            Clip clipVictory = AudioSystem.getClip();
            clipVictory.open(ais1);
            FloatControl gainControlV5 = (FloatControl) clipVictory.getControl(FloatControl.Type.MASTER_GAIN);
            gainControlV5.setValue(-30.0f);
            clipVictory.start();
            clipVictory.loop(Clip.LOOP_CONTINUOUSLY);
            String catchPokemon = "";
            wait(2);
            System.out.print("\u001b[38;2;180;180;180m");
            System.out.println("\nThis is your Inventory:");
            name.displayInventory();
            wait(2);
            System.out.print("\u001b[38;2;180;180;180m");
            System.out.println("\nDo you want to catch the Pokemon? (yes/no)");
            wait(1);
            System.out.print("\u001b[38;2;255;255;255m");
            System.out.print("> ");
            System.out.print("\033[?25h"); // SHOW CURSOR
            catchPokemon = in.nextLine().toLowerCase();
            System.out.print("\033[?25l"); // HIDE CURSOR
            if (catchPokemon.equals("yes")) {
              catchP(name, pok, region);
              wait(1);
              clipCity.close();
              clipVictory.close();
              goToNurse(name, region);
              return cont;
            }
            else if (!catchPokemon.equals("no") && !catchPokemon.equals("yes")) {
              wait(2);
              System.out.print("\u001b[38;2;180;180;180m");
              System.out.println("\nInvalid answer. You do not catch the Pokemon.");
              wait(1);
              clipCity.close();
              clipVictory.close();
              goToNurse(name, region);
              return cont;
            }
            else {
              clipCity.close();
              clipVictory.close();
              goToNurse(name, region);
              return cont;
            }
          }
        }
        else {
          wait(2);
          System.out.print("\u001b[38;2;180;180;180m");
          System.out.println("\nYou sucessfully escaped!");
          wait(2);
          System.out.println();
          System.out.println("\nWalk again or go to a gym? (walk/gym)"); // if nothing appears, can go to gym or walk again
          wait(1);
          System.out.print("\u001b[38;2;255;255;255m");
          System.out.print("> ");
          System.out.print("\033[?25h"); // SHOW CURSOR
          String response = "";
          response = in.nextLine().toLowerCase();
          System.out.print("\033[?25l"); // HIDE CURSOR
          if (response.equals("walk")) {
            clipCity.close();
            return cont;
          }
          else if (response.equals("gym")) {
            if (name._numPokemon < 4) { // cannot go to gym if player has less than four pokemon
              wait(2);
              System.out.print("\u001b[38;2;180;180;180m");
              System.out.println("\nYou cannot go to the gym with less than four Pokemon. You walk again...");
              clipCity.close();
              return cont;
            }
            else {
              boolean gym = goToGym(name, region);
              if (gym == false) {
                clipCity.close();
                goToNurse(name, region);
                return cont;
              }
              else {
                clipCity.close();
                return true;
              }
            }
          }
          else {
            wait(2);
            System.out.print("\u001b[38;2;180;180;180m");
            System.out.println("\nThat is not a valid response. You go on a walk...");
            clipCity.close();
            return false;
          }
        }
      }
      else if (encounterPokemon.equals("battle")) {
        wait(2);
        System.out.print("\u001b[38;2;180;180;180m");
        System.out.println("\nYou accept the challenge and engage in battle.");
        int type = generateType();
        Pokemon pok = generatePokemon(name, type);
        clipCity.close();
        boolean win = battle(name, pok, region);
        clipCity.setMicrosecondPosition(0);
        clipCity.start();
        clipCity.loop(Clip.LOOP_CONTINUOUSLY);
        if(win == false) {
          clipCity.close();
          goToNurse(name, region); // if lose, go to nurse to heal
          return cont;
        }
        else {
          File file1 = new File("victory.au");
          AudioInputStream ais1 = AudioSystem.getAudioInputStream(file1);
          Clip clipVictory = AudioSystem.getClip();
          clipVictory.open(ais1);
          FloatControl gainControlV3 = (FloatControl) clipVictory.getControl(FloatControl.Type.MASTER_GAIN);
          gainControlV3.setValue(-30.0f);
          clipVictory.start();
          clipVictory.loop(Clip.LOOP_CONTINUOUSLY);
          String catchPokemon = "";
          wait(2);
          System.out.print("\u001b[38;2;180;180;180m");
          System.out.println("\nThis is your Inventory:");
          name.displayInventory();
          wait(2);
          System.out.print("\u001b[38;2;180;180;180m");
          System.out.println("\nDo you want to catch the Pokemon? (yes/no)");
          wait(1);
          System.out.print("\u001b[38;2;255;255;255m");
          System.out.print("> ");
          System.out.print("\033[?25h"); // SHOW CURSOR
          catchPokemon = in.nextLine().toLowerCase();
          System.out.print("\033[?25l"); // HIDE CURSOR
          if (catchPokemon.equals("yes")) {
            catchP(name, pok, region);
            wait(1);
            clipCity.close();
            clipVictory.close();
            goToNurse(name, region);
            return cont;
          }
          else if (!catchPokemon.equals("no") && !catchPokemon.equals("yes")) {
            wait(2);
            System.out.print("\u001b[38;2;180;180;180m");
            System.out.println("\nInvalid answer. You do not catch the Pokemon.");
            clipCity.close();
            clipVictory.close();
            goToNurse(name, region);
            return cont;
          }
          else {
            clipCity.close();
            clipVictory.close();
            goToNurse(name, region);
            return cont;
          }
        }
      }
      else {
        File file1 = new File("victory.au");
        AudioInputStream ais1 = AudioSystem.getAudioInputStream(file1);
        Clip clipVictory = AudioSystem.getClip();
        wait(2);
        System.out.print("\u001b[38;2;180;180;180m");
        System.out.println("\nThat is not a valid response. You engage in battle.");
        wait(2);
        System.out.println("\nYou accept the challenge and engage in battle.");
        int type = generateType();
        Pokemon pok = generatePokemon(name, type);
        clipCity.close();
        boolean win = battle(name, pok, region);
        clipCity.setMicrosecondPosition(0);
        clipCity.start();
        clipCity.loop(Clip.LOOP_CONTINUOUSLY);
        if (win == true) {
          clipVictory.open(ais1);
          FloatControl gainControlV4 = (FloatControl) clipVictory.getControl(FloatControl.Type.MASTER_GAIN);
          gainControlV4.setValue(-30.0f);
          clipVictory.start();
          clipVictory.loop(Clip.LOOP_CONTINUOUSLY);
          String catchPokemon = "";
          wait(2);
          System.out.print("\u001b[38;2;180;180;180m");
          System.out.println("\nThis is your Inventory:");
          wait(1);
          name.displayInventory();
          wait(1);
          System.out.print("\u001b[38;2;180;180;180m");
          System.out.println("\nDo you want to catch the Pokemon? (yes/no)");
          wait(1);
          System.out.print("\u001b[38;2;255;255;255m");
          System.out.print("> ");
          System.out.print("\033[?25h"); // SHOW CURSOR
          catchPokemon = in.nextLine().toLowerCase();
          System.out.print("\033[?25l"); // HIDE CURSOR
          if (catchPokemon.equals("yes")) {
            catchP(name, pok, region);
            clipCity.close();
            clipVictory.close();
            goToNurse(name, region);
            return cont;
          }
          else if (!catchPokemon.equals("no") && !catchPokemon.equals("yes")) {
            wait(2);
            System.out.print("\u001b[38;2;180;180;180m");
            System.out.println("\nInvalid answer. You do not catch the Pokemon.");
            wait(1);
            clipCity.close();
            clipVictory.close();
            goToNurse(name, region);
            return cont;
          }
          else {
            clipCity.close();
            clipVictory.close();
            goToNurse(name, region);
            return cont;
          }
        }
        else {
          clipCity.close();
          clipVictory.close();
          goToNurse(name, region);
          return cont;
        }
      }
    }
  }

  public static void catchP(Player name, Pokemon pokName, String region) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
    int random = (int) ((Math.random() * 10));
    wait(2);
    System.out.print("\u001b[38;2;180;180;180m");
    System.out.println("\nYou try to catch a " + pokName._name + "...");
    if (name._numPokeball <= 0) {
      wait(2);
      System.out.print("\u001b[38;2;180;180;180m");
      System.out.println("\nYou do not have enough Pokeballs."); // need pokeball to catch
      wait(1);
    }
    else {
      if (random <= 6) { // catching is very likely
        wait(2);
        File fileC = new File("catch.au");
    		AudioInputStream aisC = AudioSystem.getAudioInputStream(fileC);
    		Clip clipCatch = AudioSystem.getClip();
    		clipCatch.open(aisC);
        FloatControl gainControl = (FloatControl) clipCatch.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(-20.0f);
        clipCatch.start();
        System.out.print("\u001b[38;2;180;180;180m");
        System.out.println("\nCongratulations, you successfully caught a " + pokName._name + "!");
        pokName._hp = pokName._lvl * pokName.getHPMultiplier();
        pokName._defense = pokName._lvl*pokName.getDefenseMultiplier();
        name.add(pokName);
        name._numPokemon = name._numPokemon + 1;
        name._numPokeball = name._numPokeball - 1;
        wait(2);
        System.out.println("\nYou now have " + name._numPokemon + " Pokemon.");
        System.out.println();
        name.displayPokedex(); // print out pokemon
        name.displayInventory(); // print out inventory
        System.out.println();
        wait(1);
        clipCatch.close();
      }
      else {
        wait(2);
        System.out.print("\u001b[38;2;180;180;180m");
        System.out.println("\nThe Pokemon got away!");
        name._numPokeball = name._numPokeball - 1;
        name.displayInventory();
        System.out.println();
        wait(1);
      }
    }
  }

  // start and end battle. If win, true; lose, false
  public static boolean battle(Player name, Pokemon opponent, String region) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
    // start some smashing battle music
    File file = new File("battle.au");
		AudioInputStream ais = AudioSystem.getAudioInputStream(file);
		Clip clipBattle = AudioSystem.getClip();
		clipBattle.open(ais);
    FloatControl gainControl = (FloatControl) clipBattle.getControl(FloatControl.Type.MASTER_GAIN);
    gainControl.setValue(-25.0f);
    clipBattle.start();
    clipBattle.loop(Clip.LOOP_CONTINUOUSLY);

    boolean result = false;
    wait(2);
    System.out.print("\u001b[38;2;180;180;180m");
    System.out.println("\nYou begin your battle with " + opponent._name + "...");
    System.out.println();
    wait(2);
    System.out.println("\nHere are the Stats of your Pokemon:");
    name.displayPokemon();
    wait(2);
    System.out.print("\u001b[38;2;180;180;180m");
    System.out.println("\nHere are the Stats of your opponent's Pokemon:");
    wait(1);
    System.out.print("\u001b[38;2;64;64;64m");
    System.out.println("\n~~~~~~~~~~~~~~~~~");
    wait(1);
    opponent.displayt();
    System.out.print("\u001b[38;2;64;64;64m");
    System.out.println("~~~~~~~~~~~~~~~~~");
    wait(1);
    while (opponent.isAlive() == true) { // continue to attack if opponent is alive
      int i = 0;
      while ( i < name._pokedexSize) { // will go through each pokemon in order of catching to try to faint opponent
        wait(2);
        System.out.print("\u001b[38;2;180;180;180m");
        System.out.println("\nYou take out " + name._pokedex[i]._name + "!");
        while (name._pokedex[i].isAlive() == true) { // if the pokemon you took out is still alive, it will continue to attack
          System.out.println();
          wait(1);
          name._pokedex[i].displayMove(); // allows user to see moves
          wait(2);
          System.out.print("\u001b[38;2;180;180;180m");
          System.out.println("\nChoose a move...");
          wait(1);
          System.out.print("\u001b[38;2;255;255;255m");
          System.out.print("> ");
          System.out.print("\033[?25h"); // SHOW CURSOR
          String answer = "";
          answer = in.nextLine().toLowerCase();
          System.out.print("\033[?25l"); // HIDE CURSOR
          if (name._pokedex[i] instanceof Water) {
            while (!answer.equals("water gun") && !answer.equals("rain dance") && !answer.equals("aqua ring")) { // if input not valid, asks for another input until it is valid
              System.out.println();
              wait(2);
              System.out.print("\u001b[38;2;180;180;180m");
              System.out.println("Input a valid move:");
              wait(1);
              System.out.print("\u001b[38;2;255;255;255m");
              System.out.print("> ");
              System.out.print("\033[?25h"); // SHOW CURSOR
              answer = in.nextLine().toLowerCase();
              System.out.print("\033[?25l"); // HIDE CURSOR
            }
          } else if (name._pokedex[i] instanceof Fire) {
            while (!answer.equals("ember") && !answer.equals("bulk up") && !answer.equals("restore")) { // if input not valid, asks for another input until it is valid
              System.out.println();
              wait(2);
              System.out.print("\u001b[38;2;180;180;180m");
              System.out.println("Input a valid move:");
              wait(1);
              System.out.print("\u001b[38;2;255;255;255m");
              System.out.print("> ");
              System.out.print("\033[?25h"); // SHOW CURSOR
              answer = in.nextLine().toLowerCase();
              System.out.print("\033[?25l"); // HIDE CURSOR
            }
          } else {
            while (!answer.equals("razor leaf") && !answer.equals("safe guard") && !answer.equals("rest")) { // if input not valid, asks for another input until it is valid
              System.out.println();
              wait(2);
              System.out.print("\u001b[38;2;180;180;180m");
              System.out.println("Input a valid move:");
              wait(1);
              System.out.print("\u001b[38;2;255;255;255m");
              System.out.print("> ");
              System.out.print("\033[?25h"); // SHOW CURSOR
              answer = in.nextLine().toLowerCase();
              System.out.print("\033[?25l"); // HIDE CURSOR
            }
          }
          System.out.println();
          wait(1);
          name._pokedex[i].move(answer, opponent); // pokemon makes a move
          name._pokedex[i]._exp = name._pokedex[i]._exp + 20; // exp goes up each move
          name._pokedex[i].lvlUp(); // level up if the exp is full (100 exp)
          // if level up, should make pokemon healthy again
          wait(1);
          if (opponent.isAlive() == false) {
            clipBattle.close();
            result = true;
            return result; // if the opponent dies, battle terminated, returns true
          }
          wait(2);
          System.out.print("\u001b[38;2;180;180;180m");
          System.out.println("\n" + opponent._name + " makes a move..."); // opponent makes a move
          System.out.println();
          wait(1);
          opponent.moveOther(generateMove(opponent), name._pokedex[i]); // opponents move is random
          opponent._exp = opponent._exp + 20;
          opponent.lvlUp();
          wait(1);
        }
        i = i + 1; // If by this time, the opponent is still alive but the battling pokemon is dead after the opponent move is case, increment to pull out new pokemon
      }
      System.out.println();
      wait(2);
      // if there are no more pokemon left, display stats of fainted pokemon
      System.out.print("\u001b[38;2;180;180;180m");
      System.out.println("\nAll of your Pokemon have fainted!");
      wait(2);
      System.out.println("\nYour Pokemon are rushed to the Nurse...");
      // will go to the nurse
      clipBattle.close();
      return result;
    }
    // if pokemon is not alive, it is fainted, you go to the nurse to recover and them return true
    clipBattle.close();
    result = true;
    return result;
  }

  // move generate depends on what type of pokemon it is
  public static String generateMove(Pokemon name) {
    String[] fireMoves = new String[] { "ember", "bulk up", "restore" };
    String[] waterMoves = new String[] { "water gun", "rain dance", "aqua ring" };
    String[] grassMoves = new String[] { "razor leaf", "safe guard", "rest" };
    int randMove = (int) (((Math.random()) * 3));
    if (name instanceof Fire) {
      return fireMoves[randMove];
    }
    else if (name instanceof Water) {
      return waterMoves[randMove];
    }
    else if (name instanceof Grass) {
      return grassMoves[randMove];
    }
    return "Something got messed up D:";
    }

  // nurse heals and makes walk
  public static void goToNurse(Player name, String region) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
    File file = new File("nurse.au");
		AudioInputStream ais = AudioSystem.getAudioInputStream(file);
		Clip clipNurse = AudioSystem.getClip();
		clipNurse.open(ais);
    FloatControl gainControl = (FloatControl) clipNurse.getControl(FloatControl.Type.MASTER_GAIN);
    gainControl.setValue(-30.0f);
    clipNurse.start();
    clipNurse.loop(Clip.LOOP_CONTINUOUSLY);

    wait(2);
    System.out.print("\u001b[38;2;180;180;180m");
    System.out.println("\nYou arrive at the clinic.");
    wait(2);
    System.out.println("\n\u001b[38;2;255;153;204mNurse Joy \u001b[38;2;180;180;180mrestores all of your Pokemon's health.");
    wait(2);
    for (int i = 0 ; i < name._pokedexSize; i++) {
      name._pokedex[i]._hp = name._pokedex[i]._lvl * name._pokedex[i].getHPMultiplier();
      // heals hp
      name._pokedex[i]._defense = name._pokedex[i]._lvl*name._pokedex[i].getDefenseMultiplier();
      // heals defense
      name._pokedex[i]._mana = name._pokedex[i]._lvl*name._pokedex[i].getManaMultiplier();
    }
    // displays newly healed pokemon
    name.displayPokemon();
    wait(2);
    clipNurse.close();
  }

  public static boolean goToGym(Player name, String region) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
    // gym depends on region
    if (region.equals("Eterna City")) {
      return gymBattleGrass(name, region);
    }
    else if (region.equals("Pastoria City")){
      return gymBattleWater(name, region);
    }
    else {
      return gymBattleFire(name, region);
    }
  }

  public static boolean gymBattleFire(Player name, String region) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
    File file = new File("gym.au");
		AudioInputStream ais = AudioSystem.getAudioInputStream(file);
		Clip clipGymFire = AudioSystem.getClip();
		clipGymFire.open(ais);
    FloatControl gainControl = (FloatControl) clipGymFire.getControl(FloatControl.Type.MASTER_GAIN);
    gainControl.setValue(-30.0f);
    clipGymFire.start();
    clipGymFire.loop(Clip.LOOP_CONTINUOUSLY);

    boolean badge = false;
    String answer = "";
    wait(1);
    System.out.print("\u001b[38;2;96;96;96m");
    System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------");
    wait(2);
    System.out.print("\u001b[38;2;180;180;180m");
    System.out.println("\nYou enter the gym and prepare to fight Flint.");
    wait(2);
    System.out.println("\nDo you wish to visit the Nurse before battling Flint? (yes/no)");
    wait(1);
    System.out.print("\u001b[38;2;255;255;255m");
    System.out.print("> ");
    System.out.print("\033[?25h"); // SHOW CURSOR
    answer = in.nextLine().toLowerCase();
    System.out.print("\033[?25l"); // HIDE CURSOR
    if (answer.equals("yes")) {
      wait(2);
      System.out.print("\u001b[38;2;180;180;180m");
      System.out.println("\nYou take a quick visit to the Nurse...");
      wait(1);
      goToNurse(name, region);
      wait(1);
      System.out.print("\u001b[38;2;180;180;180m");
      System.out.println("\nYou enter the gym and prepare to battle Flint's first Pokemon, Infernape.");
      Pokemon infernape = new Fire("Infernape", 23, 2320);
      boolean firstBattle = battle(name, infernape, region);
      if (firstBattle == true) { // if wins first battle, fights next
        wait(2);
        System.out.print("\u001b[38;2;180;180;180m");
        System.out.println("\nPrepare to fight Flint's next Pokemon, Rapidash!");
        wait(1);
        Pokemon rapidash = new Fire("Rapidash", 25, 2520);
        boolean secondBattle = battle(name, rapidash, region);
        if (secondBattle == true) {
          wait(2);
          System.out.print("\u001b[38;2;180;180;180m");
          System.out.println("\nCongratulations! You have successfully defeated Flint!");
          wait(2);
          System.out.println("\nYou earned the Fire Badge!");
          wait(1);
          System.out.print("\u001b[38;2;96;96;96m");
          System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------");
          System.out.println();
          System.out.print("\u001b[38;2;180;180;180m");
          wait(1);
          clipGymFire.close();
          badge = true; // if win both, then return true
          return badge;
        }
      }
    }
    else if (answer.equals("no")) {
      wait(2);
      System.out.print("\u001b[38;2;180;180;180m");
      System.out.println("\nYou enter the gym and prepare to battle Flint's first Pokemon, Infernape.");
      wait(1);
      Pokemon infernape = new Fire("Infernape", 23, 2320);
      boolean firstBattle = battle(name, infernape, region);
      if (firstBattle == true) { // if wins first battle, fights next
        wait(2);
        System.out.print("\u001b[38;2;180;180;180m");
        System.out.println("\nPrepare to fight Flint's next Pokemon, Rapidash!");
        wait(1);
        Pokemon rapidash = new Fire("Rapidash", 25, 2520);
        boolean secondBattle = battle(name, rapidash, region);
        if (secondBattle == true) {
          wait(2);
          System.out.print("\u001b[38;2;180;180;180m");
          System.out.println("\nCongratulations! You have successfully defeated Flint!");
          wait(2);
          System.out.println("\nYou earned the Fire Badge!");
          wait(1);
          System.out.print("\u001b[38;2;96;96;96m");
          System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------");
          System.out.println();
          System.out.print("\u001b[38;2;180;180;180m");
          wait(1);
          clipGymFire.close();
          badge = true; // if win both, then return true
          return badge;
          }
        }
      }
      else {
        wait(2);
        System.out.print("\u001b[38;2;180;180;180m");
        System.out.println("\nThat is not a valid response. You do not go to the Nurse.");
        wait(2);
        System.out.println("\nYou enter the gym and prepare to battle Flint's first Pokemon, Infernape.");
        wait(1);
        Pokemon infernape = new Fire("Infernape", 23, 2320);
        boolean firstBattle = battle(name, infernape, region);
        if (firstBattle == true) {
          System.out.print("\u001b[38;2;180;180;180m");
          System.out.println("\nPrepare to fight Flint's next Pokemon, Rapidash!");
          wait(1);
          Pokemon rapidash = new Fire("Rapidash", 25, 2520);
          boolean secondBattle = battle(name, rapidash, region);
          if (secondBattle == true) {
            wait(2);
            System.out.print("\u001b[38;2;180;180;180m");
            System.out.println("\nCongratulations! You have successfully defeated Flint!");
            wait(2);
            System.out.println("\nYou earned the Fire Badge!");
            wait(1);
            System.out.print("\u001b[38;2;96;96;96m");
            System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------");
            System.out.println();
            System.out.print("\u001b[38;2;180;180;180m");
            wait(1);
            clipGymFire.close();
            badge = true;
            return badge;
          }
        }
      }
      clipGymFire.close();
      return badge;
  }

  public static boolean gymBattleWater(Player name, String region) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
    File file = new File("gym.au");
		AudioInputStream ais = AudioSystem.getAudioInputStream(file);
		Clip clipGymWater = AudioSystem.getClip();
		clipGymWater.open(ais);
    FloatControl gainControl = (FloatControl) clipGymWater.getControl(FloatControl.Type.MASTER_GAIN);
    gainControl.setValue(-30.0f);
    clipGymWater.start();
    clipGymWater.loop(Clip.LOOP_CONTINUOUSLY);

    boolean badge = false;
    String answer = "";
    wait(1);
    System.out.print("\u001b[38;2;96;96;96m");
    System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------");
    wait(2);
    System.out.print("\u001b[38;2;180;180;180m");
    System.out.println("\nYou enter the gym and prepare to fight Crasher Wake.");
    wait(2);
    System.out.println("\nDo you wish to visit the Nurse before battling Crasher Wake? (yes/no)");
    wait(1);
    System.out.print("\u001b[38;2;255;255;255m");
    System.out.print("> ");
    System.out.print("\033[?25h"); // SHOW CURSOR
    answer = in.nextLine().toLowerCase();
    System.out.print("\033[?25l"); // HIDE CURSOR
    if (answer.equals("yes")) {
      wait(2);
      System.out.print("\u001b[38;2;180;180;180m");
      System.out.println("\nYou take a quick visit to the Nurse...");
      wait(1);
      goToNurse(name, region);
      wait(2);
      System.out.print("\u001b[38;2;180;180;180m");
      System.out.println("\nYou enter the gym and prepare to battle Crasher Wake's first Pokemon Gyarados.");
      wait(2);
      Pokemon gyarados = new Water("Gyarados", 19, 1980);
      boolean firstBattle = battle(name, gyarados, region);
      if (firstBattle == true) {
        wait(2);
        System.out.print("\u001b[38;2;180;180;180m");
        System.out.println("\nPrepare to fight Crasher Wake's next Pokemon, Quagsire!");
        wait(1);
        Pokemon quagsire = new Water("Quagsire", 20, 2040);
        boolean secondBattle = battle(name, quagsire, region);
        if (secondBattle == true) {
          wait(2);
          System.out.print("\u001b[38;2;180;180;180m");
          System.out.println("\nPrepare to fight Crasher Wake's next Pokemon, Floatzel!");
          wait(1);
          Pokemon floatzel = new Water("Floatzel", 21, 2160);
          boolean thirdBattle = battle(name, floatzel, region);
          if (thirdBattle == true) {
            wait(2);
            System.out.print("\u001b[38;2;180;180;180m");
            System.out.println("\nCongratulations! You have successfully defeated Crasher Wake!");
            wait(2);
            System.out.println("\nYou earned the Water Badge!");
            wait(1);
            System.out.print("\u001b[38;2;96;96;96m");
            System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------");
            System.out.println();
            System.out.print("\u001b[38;2;180;180;180m");
            wait(1);
            clipGymWater.close();
            badge = true;
            return badge;
          }
        }
      }
    }
    else if (answer.equals("no")) {
      wait(2);
      System.out.print("\u001b[38;2;180;180;180m");
      System.out.println("\nYou enter the gym and prepare to battle Crasher Wake's first Pokemon Gyarados.");
      wait(1);
      Pokemon gyarados = new Water("Gyarados", 19, 1980);
      boolean firstBattle = battle(name, gyarados, region);
      if (firstBattle == true) {
        wait(2);
        System.out.print("\u001b[38;2;180;180;180m");
        System.out.println("\nPrepare to fight Crasher Wake's next Pokemon, Quagsire!");
        wait(1);
        Pokemon quagsire = new Water("Quagsire", 20, 2040);
        boolean secondBattle = battle(name, quagsire, region);
        if (secondBattle == true) {
          wait(2);
          System.out.print("\u001b[38;2;180;180;180m");
          System.out.println("\nPrepare to fight Crasher Wake's next Pokemon, Floatzel!");
          wait(1);
          Pokemon floatzel = new Water("Floatzel", 21, 2160);
          boolean thirdBattle = battle(name, floatzel, region);
          if (thirdBattle == true) {
            wait(2);
            System.out.print("\u001b[38;2;180;180;180m");
            System.out.println("\nCongratulations! You have successfully defeated Crasher Wake!");
            wait(2);
            System.out.println("\nYou earned the Water Badge!");
            wait(1);
            System.out.print("\u001b[38;2;96;96;96m");
            System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------");
            System.out.println();
            System.out.print("\u001b[38;2;180;180;180m");
            wait(1);
            clipGymWater.close();
            badge = true;
            return badge;
          }
        }
      }
    }
    else {
      wait(2);
      System.out.print("\u001b[38;2;180;180;180m");
      System.out.println("\nThat is not a valid response. You do not go to the nurse.");
      wait(2);
      System.out.println("\nYou enter the gym and prepare to battle Crasher Wake's first Pokemon Gyarados.");
      wait(1);
      Pokemon gyarados = new Water("Gyarados", 19, 1980);
      boolean firstBattle = battle(name, gyarados, region);
      if (firstBattle == true) {
        wait(2);
        System.out.print("\u001b[38;2;180;180;180m");
        System.out.println("\nPrepare to fight Crasher Wake's next Pokemon, Quagsire!");
        wait(1);
        Pokemon quagsire = new Water("Quagsire", 20, 2040);
        boolean secondBattle = battle(name, quagsire, region);
        if (secondBattle == true) {
          wait(2);
          System.out.print("\u001b[38;2;180;180;180m");
          System.out.println("\nPrepare to fight Crasher Wake's next Pokemon, Floatzel!");
          wait(1);
          Pokemon floatzel = new Water("Floatzel", 21, 2160);
          boolean thirdBattle = battle(name, floatzel, region);
          if (thirdBattle == true) {
            wait(2);
            System.out.print("\u001b[38;2;180;180;180m");
            System.out.println("\nCongratulations! You have successfully defeated Crasher Wake!");
            wait(2);
            System.out.println("\nYou earned the Water Badge!");
            wait(1);
            System.out.print("\u001b[38;2;96;96;96m");
            System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------");
            System.out.println();
            System.out.print("\u001b[38;2;180;180;180m");
            wait(1);
            clipGymWater.close();
            badge = true;
            return badge;
          }
        }
      }
    }
    clipGymWater.close();
    return badge;
  }

  public static boolean gymBattleGrass(Player name, String region) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
    File file = new File("gym.au");
		AudioInputStream ais = AudioSystem.getAudioInputStream(file);
		Clip clipGymGrass = AudioSystem.getClip();
		clipGymGrass.open(ais);
    FloatControl gainControl = (FloatControl) clipGymGrass.getControl(FloatControl.Type.MASTER_GAIN);
    gainControl.setValue(-30.0f);
    clipGymGrass.start();
    clipGymGrass.loop(Clip.LOOP_CONTINUOUSLY);

    boolean badge = false;
    String answer = "";
    wait(1);
    System.out.print("\u001b[38;2;96;96;96m");
    System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------");
    wait(2);
    System.out.print("\u001b[38;2;180;180;180m");
    System.out.println("\nYou enter the gym and prepare to fight Gardenia.");
    wait(2);
    System.out.println("\nDo you wish to visit the Nurse before battling Gardenia? (yes/no)");
    wait(1);
    System.out.print("\u001b[38;2;255;255;255m");
    System.out.print("> ");
    System.out.print("\033[?25h"); // SHOW CURSOR
    answer = in.nextLine().toLowerCase();
    System.out.print("\033[?25l"); // HIDE CURSOR
    if (answer.equals("yes")) {
      wait(2);
      System.out.print("\u001b[38;2;180;180;180m");
      System.out.println("\nYou take a quick visit to the Nurse...");
      wait(1);
      goToNurse(name, region);
      wait(2);
      System.out.print("\u001b[38;2;180;180;180m");
      System.out.println("\nYou enter the gym and prepare to battle Gardenia's first Pokemon, Cherubi.");
      wait(1);
      Pokemon cherubi = new Grass("Cherubi", 14, 1480);
      boolean firstBattle = battle(name, cherubi, region);
      if (firstBattle == true) {
        wait(2);
        System.out.print("\u001b[38;2;180;180;180m");
        System.out.println("\nPrepare to fight Gardenia's next Pokemon, Turtwig!");
        wait(1);
        Pokemon turtwig = new Grass("Turtwig", 16, 1640);
        boolean secondBattle = battle(name, turtwig, region);
        if (secondBattle == true) {
          wait(2);
          System.out.print("\u001b[38;2;180;180;180m");
          System.out.println("\nPrepare to fight Gardenia's next Pokemon, Roserade!");
          wait(1);
          Pokemon roserade = new Grass("Roserade",17, 1760);
          boolean thirdBattle = battle(name, roserade, region);
          if (thirdBattle == true) {
            wait(2);
            System.out.print("\u001b[38;2;180;180;180m");
            System.out.println("\nCongratulations! You have successfully defeated Gardenia!");
            wait(2);
            System.out.println("\nYou earned the Grass Badge!");
            wait(1);
            System.out.print("\u001b[38;2;96;96;96m");
            System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------");
            System.out.println();
            System.out.print("\u001b[38;2;180;180;180m");
            wait(1);
            clipGymGrass.close();
            badge = true;
            return badge;
          }
        }
      }
    }
    else if (answer.equals("no")) {
      wait(2);
      System.out.print("\u001b[38;2;180;180;180m");
      System.out.println("\nYou enter the gym and prepare to battle Gardenia's First Pokemon, Cherubi.");
      wait(1);
      Pokemon cherubi = new Grass("Cherubi", 14, 1480);
      boolean firstBattle = battle(name, cherubi, region);
      if (firstBattle == true) {
        wait(2);
        System.out.print("\u001b[38;2;180;180;180m");
        System.out.println("\nPrepare to fight Gardenia's next Pokemon, Turtwig!");
        wait(1);
        Pokemon turtwig = new Grass("Turtwig", 16, 1640);
        boolean secondBattle = battle(name, turtwig, region);
        if (secondBattle == true) {
          wait(2);
          System.out.print("\u001b[38;2;180;180;180m");
          System.out.println("\nPrepare to fight Gardenia's next Pokemon, Roserade!");
          wait(2);
          Pokemon roserade = new Grass("Roserade",17, 1760);
          boolean thirdBattle = battle(name, roserade, region);
          if (thirdBattle == true) {
            wait(2);
            System.out.print("\u001b[38;2;180;180;180m");
            System.out.println("\nCongratulations! You have successfully defeated Gardenia!");
            wait(2);
            System.out.println("\nYou earned the Grass Badge!");
            wait(1);
            System.out.print("\u001b[38;2;96;96;96m");
            System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------");
            System.out.println();
            System.out.print("\u001b[38;2;180;180;180m");
            wait(1);
            clipGymGrass.close();
            badge = true;
            return badge;
          }
        }
      }
    }
    else {
      wait(2);
      System.out.print("\u001b[38;2;180;180;180m");
      System.out.println("\nThat is not a valid response. You do not go to the nurse.");
      wait(2);
      System.out.println("\nYou enter the gym and prepare to battle Gardenia's first Pokemon, Cherubi.");
      wait(1);
      Pokemon cherubi = new Water("Cherubi", 14, 1480);
      boolean firstBattle = battle(name, cherubi, region);
      if (firstBattle == true) {
        wait(2);
        System.out.print("\u001b[38;2;180;180;180m");
        System.out.println("\nPrepare to fight Gardenia's next Pokemon, Turtwig!");
        wait(1);
        Pokemon turtwig = new Water("Turtwig", 16, 1640);
        boolean secondBattle = battle(name, turtwig, region);
        if (secondBattle == true) {
          wait(2);
          System.out.print("\u001b[38;2;180;180;180m");
          System.out.println("\nPrepare to fight Gardenia's next Pokemon, Roserade!");
          wait(1);
          Pokemon roserade = new Water("Roserade", 17, 1760);
          boolean thirdBattle = battle(name, roserade, region);
          if (thirdBattle == true) {
            wait(2);
            System.out.print("\u001b[38;2;180;180;180m");
            System.out.println("\nCongratulations! You have successfully defeated Gardenia!");
            wait(2);
            System.out.println("\nYou earned the Grass Badge!");
            wait(1);
            System.out.print("\u001b[38;2;96;96;96m");
            System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------");
            System.out.println();
            System.out.print("\u001b[38;2;180;180;180m");
            wait(1);
            clipGymGrass.close();
            badge = true;
            return badge;
          }
        }
      }
    }
    clipGymGrass.close();
    return badge;
  }

  public static void gameSetup(Player player) throws UnsupportedAudioFileException, IOException, LineUnavailableException, Exception {
    File file = new File("begin.au");
		AudioInputStream ais = AudioSystem.getAudioInputStream(file);
		Clip clipBegin = AudioSystem.getClip();
		clipBegin.open(ais);
    FloatControl gainControl = (FloatControl) clipBegin.getControl(FloatControl.Type.MASTER_GAIN);
    gainControl.setValue(-30.0f);
    clipBegin.start();
    clipBegin.loop(Clip.LOOP_CONTINUOUSLY);

    System.out.println("\033[2J"); // CLEAR SCREEN
    System.out.print("\033[?25l"); // HIDE CURSOR
    System.out.print("\u001b[0m");
    System.out.print("\u001B[0m");
    System.out.println();
    wait(2);
    System.out.print("\u001b[38;2;180;180;180m");
    System.out.println("\nWelcome, Trainer, to Pokemon Shining Pearl!");
    wait(4);
    System.out.println("\nA game full of adventure awaits you!");
    wait(4);
    System.out.print("\nBut first,");
    wait(4);
    System.out.print(" may I ask");
    wait(3);
    System.out.print(".");
    wait(3);
    System.out.print(".");
    wait(3);
    System.out.print(".");
    wait(3);
    System.out.println();
    wait(3);
    System.out.println("\nWhat is your name?");
    wait(1);
    System.out.print("\u001b[38;2;255;255;255m");
    System.out.print("> ");
    System.out.print("\033[?25h"); // SHOW CURSOR
    String name = "";
    name = in.nextLine();
    player._name = name;
    System.out.print("\033[?25l"); // HIDE CURSOR
    wait(1);
		clipBegin.stop();
		clipBegin.flush();
		clipBegin.close();
  }

  public static void chooseStarter(Player player) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
    File file = new File("starter.au");
		AudioInputStream ais = AudioSystem.getAudioInputStream(file);
		Clip clipStarter = AudioSystem.getClip();
		clipStarter.open(ais);
    FloatControl gainControl = (FloatControl) clipStarter.getControl(FloatControl.Type.MASTER_GAIN);
    gainControl.setValue(-30.0f);
    clipStarter.start();
    clipStarter.loop(Clip.LOOP_CONTINUOUSLY);

    System.out.print("\033[?25l"); // HIDE CURSOR
    System.out.println();
    wait(2);
    System.out.print("\u001b[38;2;180;180;180m");
    System.out.println("\nRING RING! Wake up \u001b[38;2;255;255;255m" + player._name + "\u001b[38;2;180;180;180m! Your mom told you to go to \u001b[38;2;153;153;255mProfessor Rowan's \u001b[38;2;180;180;180mPokemon Center to get your first starter Pokemon.");
    wait(2);
    System.out.println("\nYou totally forgot. Today is your 10th birthday. You're finally old enough to become a Pokemon trainer!");
    wait(2);
    System.out.println("\nYou rush over to the Pokemon Center and luckily the line isn't long.");
    wait(2);
    System.out.print("\nA");
    wait(2);
    System.out.print(" few");
    wait(2);
    System.out.print(" minutes");
    wait(2);
    System.out.print(" later");
    wait(2);
    System.out.println("...");
    wait(3);
    System.out.println("\nIt's finally your turn!");
    // wake up, go to drs place to pick Pokemon
    wait(2);

    System.out.println();
    System.out.println("\nThere are three starter Pokemons: \u001b[38;2;243;113;66mChimchar\u001b[38;2;180;180;180m, \u001b[38;2;103;179;201mPiplup\u001b[38;2;180;180;180m, and \u001b[38;2;119;221;118mTurtwig\u001b[38;2;180;180;180m.");
    wait(2);

    System.out.println("\nInput the Pokemon that you want to know more about:");
    wait(1);
    System.out.print("\u001b[38;2;255;255;255m");
    System.out.print("> ");
    System.out.print("\033[?25h"); // SHOW CURSOR
    String pokemonOne = "";

    // POKEMON DESCRIPTIONS
    pokemonOne = in.nextLine().toLowerCase();
    System.out.print("\033[?25l"); // HIDE CURSOR
    wait(1);
    if (pokemonOne.equals("chimchar")) {
      Fire chimchar = new Fire("Chimchar", 5, 500);
      wait(1);
      System.out.print("\u001b[38;2;96;96;96m");
      System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------");
      wait(2);
      System.out.print("\u001b[38;2;243;113;66m");
      System.out.println("\n  * A Chimp Pokemon.");
      wait(2);
      System.out.print("\u001b[38;2;255;160;1m");
      System.out.println("  * With a flaming tail, Chimchar is skilled in Fire-type moves.");
      wait(2);
      System.out.print("\u001b[38;2;255;184;79m");
      System.out.println("  * Its light body affords it the ability to scale steep cliffs and live atop rocky mountains.");
      wait(2);
      System.out.print("\u001b[38;2;255;160;1m");
      System.out.println("  * Some say that the fiery tail is fueled by gas made in its stomach.");
      wait(2);
      System.out.print("\u001b[38;2;243;113;66m");
      System.out.println("  * Not even rain can put out the flames, but Chimchar always puts out the fire when asleep.");
      wait(1);
      System.out.print("\u001b[38;2;96;96;96m");
      System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------");
    }
    else if (pokemonOne.equals("piplup")) {
      Pokemon piplup = new Water("Piplup", 5, 500);
      wait(1);
      System.out.print("\u001b[38;2;96;96;96m");
      System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------");
      wait(2);
      System.out.print("\u001b[38;2;103;179;201m");
      System.out.println("\n  * A Penguin Pokemon.");
      wait(2);
      System.out.print("\u001b[38;2;172;229;238m");
      System.out.println("  * It's one of the starter Pokemon received from \u001b[38;2;153;153;255mProfessor Rowan \u001b[38;2;172;229;238mwhen the player departs from the Sinnoh region.");
      wait(2);
      System.out.print("\u001b[38;2;103;179;201m");
      System.out.println("  * It's a Water type, so it's strong versus Rock and Grounds.");
      wait(2);
      System.out.print("\u001b[38;2;34;130;164m");
      System.out.println("  * It's very cute but filled with pride--it hates to accept food from people.");
      wait(1);
      System.out.print("\u001b[38;2;96;96;96m");
      System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------");
    }
    else if (pokemonOne.equals("turtwig")) {
      Pokemon turtwig = new Grass("Turtwig", 5, 500);
      wait(1);
      System.out.print("\u001b[38;2;96;96;96m");
      System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------");
      wait(2);
      System.out.print("\u001b[38;2;119;221;118m");
      System.out.println("\n  * A Tiny Leaf Pokemon.");
      wait(2);
      System.out.print("\u001b[38;2;166;236;168m");
      System.out.println("  * Despite its animal appearance, Turtwig actually has vegetation sprouting from its head.");
      wait(2);
      System.out.print("\u001b[38;2;210;253;187m");
      System.out.println("  * The shell on its back is made of soil and hardens when it drinks water.");
      wait(2);
      System.out.print("\u001b[38;2;166;236;168m");
      System.out.println("  * Much like a plant, Turtwig performs photosynthesis, absorbing sunlight and making oxygen.");
      wait(2);
      System.out.print("\u001b[38;2;119;221;118m");
      System.out.println("  * It also relies heavily on water to keep its plant healthy and thus spends a lot of time near lakes.");
      wait(1);
      System.out.print("\u001b[38;2;96;96;96m");
      System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------");
    }
    else {
      wait(1);
      System.out.print("\u001b[38;2;180;180;180m");
      System.out.println("\nThat's not exactly a Pokemon, but \u001b[38;2;153;153;255mProfessor Rowan \u001b[38;2;180;180;180mwants to show you \u001b[38;2;243;113;66mChimchar\u001b[38;2;180;180;180m.");
      Pokemon chimchar = new Fire("Chimchar", 5, 500);
      wait(1);
      System.out.print("\u001b[38;2;96;96;96m");
      System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------");
      wait(2);
      System.out.print("\u001b[38;2;243;113;66m");
      System.out.println("\n  * A Chimp Pokemon.");
      wait(2);
      System.out.print("\u001b[38;2;255;160;1m");
      System.out.println("  * With a flaming tail, Chimchar is skilled in Fire-type moves.");
      wait(2);
      System.out.print("\u001b[38;2;255;184;79m");
      System.out.println("  * Its light body affords it the ability to scale steep cliffs and live atop rocky mountains.");
      wait(2);
      System.out.print("\u001b[38;2;255;160;1m");
      System.out.println("  * Some say that the fiery tail is fueled by gas made in its stomach.");
      wait(2);
      System.out.print("\u001b[38;2;243;113;66m");
      System.out.println("  * Not even rain can put out the flames, but Chimchar always puts out the fire when asleep.");
      wait(1);
      System.out.print("\u001b[38;2;96;96;96m");
      System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------");
    }

    wait(2);
    System.out.print("\u001b[38;2;180;180;180m");
    System.out.println("\nInput another Pokemon that you want to know more about:");
    wait(1);
    System.out.print("\u001b[38;2;255;255;255m");
    System.out.print("> ");
    System.out.print("\033[?25h"); // SHOW CURSOR
    String pokemonTwo = "";

    pokemonTwo = in.nextLine().toLowerCase();
    System.out.print("\033[?25l"); // HIDE CURSOR
    wait(1);
    if (pokemonTwo.equals("chimchar")) {
      Pokemon chimchar = new Fire("Chimchar", 5, 500);
      wait(1);
      System.out.print("\u001b[38;2;96;96;96m");
      System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------");
      wait(2);
      System.out.print("\u001b[38;2;243;113;66m");
      System.out.println("\n  * A Chimp Pokemon.");
      wait(2);
      System.out.print("\u001b[38;2;255;160;1m");
      System.out.println("  * With a flaming tail, Chimchar is skilled in Fire-type moves.");
      wait(2);
      System.out.print("\u001b[38;2;255;184;79m");
      System.out.println("  * Its light body affords it the ability to scale steep cliffs and live atop rocky mountains.");
      wait(2);
      System.out.print("\u001b[38;2;255;160;1m");
      System.out.println("  * Some say that the fiery tail is fueled by gas made in its stomach.");
      wait(2);
      System.out.print("\u001b[38;2;243;113;66m");
      System.out.println("  * Not even rain can put out the flames, but Chimchar always puts out the fire when asleep.");
      wait(1);
      System.out.print("\u001b[38;2;96;96;96m");
      System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------");
    }
    else if (pokemonTwo.equals("piplup")) {
      Pokemon piplup = new Water("Piplup", 5, 500);
      wait(1);
      System.out.print("\u001b[38;2;96;96;96m");
      System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------");
      wait(2);
      System.out.print("\u001b[38;2;103;179;201m");
      System.out.println("\n  * A Penguin Pokemon.");
      wait(2);
      System.out.print("\u001b[38;2;172;229;238m");
      System.out.println("  * It's one of the starter Pokemon received from \u001b[38;2;153;153;255mProfessor Rowan \u001b[38;2;172;229;238mwhen the player departs from the Sinnoh region.");
      wait(2);
      System.out.print("\u001b[38;2;103;179;201m");
      System.out.println("  * It's a Water type, so it's strong versus Rock and Grounds.");
      wait(2);
      System.out.print("\u001b[38;2;34;130;164m");
      System.out.println("  * It's very cute but filled with pride--it hates to accept food from people.");
      wait(1);
      System.out.print("\u001b[38;2;96;96;96m");
      System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------");
    }
    else if (pokemonTwo.equals("turtwig")) {
      Pokemon turtwig = new Grass("Turtwig", 5, 500);
      wait(1);
      System.out.print("\u001b[38;2;96;96;96m");
      System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------");
      wait(2);
      System.out.print("\u001b[38;2;119;221;118m");
      System.out.println("\n  * A Tiny Leaf Pokemon.");
      wait(2);
      System.out.print("\u001b[38;2;166;236;168m");
      System.out.println("  * Despite its animal appearance, Turtwig actually has vegetation sprouting from its head.");
      wait(2);
      System.out.print("\u001b[38;2;210;253;187m");
      System.out.println("  * The shell on its back is made of soil and hardens when it drinks water.");
      wait(2);
      System.out.print("\u001b[38;2;166;236;168m");
      System.out.println("  * Much like a plant, Turtwig performs photosynthesis, absorbing sunlight and making oxygen.");
      wait(2);
      System.out.print("\u001b[38;2;119;221;118m");
      System.out.println("  * It also relies heavily on water to keep its plant healthy and thus spends a lot of time near lakes.");
      wait(1);
      System.out.print("\u001b[38;2;96;96;96m");
      System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------");
    }
    else {
      wait(1);
      System.out.print("\u001b[38;2;180;180;180m");
      System.out.println("\nThat's not exactly a Pokemon, but \u001b[38;2;153;153;255mProfessor Rowan \u001b[38;2;180;180;180mwants to show you \u001b[38;2;103;179;201mPiplup\u001b[38;2;180;180;180m.");
      Pokemon piplup = new Water("Piplup", 5, 500);
      wait(1);
      System.out.print("\u001b[38;2;96;96;96m");
      System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------");
      wait(2);
      System.out.print("\u001b[38;2;103;179;201m");
      System.out.println("\n  * A Penguin Pokemon.");
      wait(2);
      System.out.print("\u001b[38;2;172;229;238m");
      System.out.println("  * It's one of the starter Pokemon received from \u001b[38;2;153;153;255mProfessor Rowan \u001b[38;2;172;229;238mwhen the player departs from the Sinnoh region.");
      wait(2);
      System.out.print("\u001b[38;2;103;179;201m");
      System.out.println("  * It's a Water type, so it's strong versus Rock and Grounds.");
      wait(2);
      System.out.print("\u001b[38;2;34;130;164m");
      System.out.println("  * It's very cute but filled with pride--it hates to accept food from people.");
      wait(1);
      System.out.print("\u001b[38;2;96;96;96m");
      System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------");
    }

    wait(2);
    System.out.print("\u001b[38;2;180;180;180m");
    System.out.println("\nInput the last Pokemon:");
    wait(1);
    System.out.print("\u001b[38;2;255;255;255m");
    System.out.print("> ");
    System.out.print("\033[?25h"); // SHOW CURSOR
    String pokemonThree = "";

    pokemonThree = in.nextLine().toLowerCase();
    System.out.print("\033[?25l"); // HIDE CURSOR
    wait(1);
    if (pokemonThree.equals("chimchar")) {
      Pokemon chimchar = new Fire("Chimchar", 5, 500);
      wait(1);
      System.out.print("\u001b[38;2;96;96;96m");
      System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------");
      wait(2);
      System.out.print("\u001b[38;2;243;113;66m");
      System.out.println("\n  * A Chimp Pokemon.");
      wait(2);
      System.out.print("\u001b[38;2;255;160;1m");
      System.out.println("  * With a flaming tail, Chimchar is skilled in Fire-type moves.");
      wait(2);
      System.out.print("\u001b[38;2;255;184;79m");
      System.out.println("  * Its light body affords it the ability to scale steep cliffs and live atop rocky mountains.");
      wait(2);
      System.out.print("\u001b[38;2;255;160;1m");
      System.out.println("  * Some say that the fiery tail is fueled by gas made in its stomach.");
      wait(2);
      System.out.print("\u001b[38;2;243;113;66m");
      System.out.println("  * Not even rain can put out the flames, but Chimchar always puts out the fire when asleep.");
      wait(1);
      System.out.print("\u001b[38;2;96;96;96m");
      System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------");
    }
    else if (pokemonThree.equals("piplup")) {
      Pokemon piplup = new Water("Piplup", 5, 500);
      wait(1);
      System.out.print("\u001b[38;2;96;96;96m");
      System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------");
      wait(2);
      System.out.print("\u001b[38;2;103;179;201m");
      System.out.println("\n  * A Penguin Pokemon.");
      wait(2);
      System.out.print("\u001b[38;2;172;229;238m");
      System.out.println("  * It's one of the starter Pokemon received from \u001b[38;2;153;153;255mProfessor Rowan \u001b[38;2;172;229;238mwhen the player departs from the Sinnoh region.");
      wait(2);
      System.out.print("\u001b[38;2;103;179;201m");
      System.out.println("  * It's a Water type, so it's strong versus Rock and Grounds.");
      wait(2);
      System.out.print("\u001b[38;2;34;130;164m");
      System.out.println("  * It's very cute but filled with pride--it hates to accept food from people.");
      wait(1);
      System.out.print("\u001b[38;2;96;96;96m");
      System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------");
    }
    else if (pokemonThree.equals("turtwig")) {
      Pokemon turtwig = new Grass("Turtwig", 5, 500);
      wait(1);
      System.out.print("\u001b[38;2;96;96;96m");
      System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------");
      wait(2);
      System.out.print("\u001b[38;2;119;221;118m");
      System.out.println("\n  * A Tiny Leaf Pokemon.");
      wait(2);
      System.out.print("\u001b[38;2;166;236;168m");
      System.out.println("  * Despite its animal appearance, Turtwig actually has vegetation sprouting from its head.");
      wait(2);
      System.out.print("\u001b[38;2;210;253;187m");
      System.out.println("  * The shell on its back is made of soil and hardens when it drinks water.");
      wait(2);
      System.out.print("\u001b[38;2;166;236;168m");
      System.out.println("  * Much like a plant, Turtwig performs photosynthesis, absorbing sunlight and making oxygen.");
      wait(2);
      System.out.print("\u001b[38;2;119;221;118m");
      System.out.println("  * It also relies heavily on water to keep its plant healthy and thus spends a lot of time near lakes.");
      wait(1);
      System.out.print("\u001b[38;2;96;96;96m");
      System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------");
    }
    else {
      wait(1);
      System.out.print("\u001b[38;2;180;180;180m");
      System.out.println("\nThat's not exactly a Pokemon, but \u001b[38;2;153;153;255mProfessor Rowan \u001b[38;2;180;180;180mwants to show you \u001b[38;2;119;221;118mTurtwig\u001b[38;2;180;180;180m.");
      Pokemon turtwig = new Grass("Turtwig", 5, 500);
      wait(2);
      System.out.print("\u001b[38;2;96;96;96m");
      System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------");
      wait(2);
      System.out.print("\u001b[38;2;119;221;118m");
      System.out.println("\n  * A Tiny Leaf Pokemon.");
      wait(2);
      System.out.print("\u001b[38;2;166;236;168m");
      System.out.println("  * Despite its animal appearance, Turtwig actually has vegetation sprouting from its head.");
      wait(2);
      System.out.print("\u001b[38;2;210;253;187m");
      System.out.println("  * The shell on its back is made of soil and hardens when it drinks water.");
      wait(2);
      System.out.print("\u001b[38;2;166;236;168m");
      System.out.println("  * Much like a plant, Turtwig performs photosynthesis, absorbing sunlight and making oxygen.");
      wait(2);
      System.out.print("\u001b[38;2;119;221;118m");
      System.out.println("  * It also relies heavily on water to keep its plant healthy and thus spends a lot of time near lakes.");
      wait(1);
      System.out.print("\u001b[38;2;96;96;96m");
      System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------");
    }
    // Instance of Pokemon

    // CHOOSING POKEMON
    wait(3);
    System.out.print("\u001b[38;2;180;180;180m");
    System.out.println("\nWhich starter Pokemon do you want? (\u001b[38;2;243;113;66mChimchar\u001b[38;2;180;180;180m/\u001b[38;2;103;179;201mPiplup\u001b[38;2;180;180;180m/\u001b[38;2;119;221;118mTurtwig\u001b[38;2;180;180;180m)");
    wait(1);
    System.out.print("\u001b[38;2;255;255;255m");
    System.out.print("> ");
    System.out.print("\033[?25h"); // SHOW CURSOR
    String response = "";
    response = in.nextLine().toLowerCase();
    System.out.print("\033[?25l"); // HIDE CURSOR
    if (response.equals("chimchar")) {
      Fire starter = new Fire("Chimchar", 4, 400);
      player.add(starter);
      wait(2);
      System.out.print("\u001b[38;2;180;180;180m");
      System.out.println("\nCongrats, you got your first Pokemon! It's a \u001b[38;2;243;113;66mChimchar\u001b[38;2;180;180;180m!");
    }
    else if (response.equals("piplup")) {
      Water starter = new Water("Piplup", 4, 400);
      player.add(starter);
      wait(2);
      System.out.print("\u001b[38;2;180;180;180m");
      System.out.println("\nCongrats, you got your first Pokemon! It's a \u001b[38;2;103;179;201mPiplup\u001b[38;2;180;180;180m!");
    }
    else if (response.equals("turtwig")) {
      Grass starter = new Grass("Turtwig", 4, 400);
      player.add(starter);
      wait(2);
      System.out.print("\u001b[38;2;180;180;180m");
      System.out.println("\nCongrats, you got your first Pokemon! It's a \u001b[38;2;119;221;118mTurtwig\u001b[38;2;180;180;180m!");
    }
    else if (!response.equals("chimchar") && !response.equals("piplup") && !response.equals("turtwig")) {
      int random = (int) ((Math.random() * 3));
      if (random == 0) {
        Fire starter = new Fire("Chimchar", 4, 400);
        player.add(starter);
        wait(2);
        System.out.print("\u001b[38;2;180;180;180m");
        System.out.println("\nYou responded too late! The only Pokemon left is \u001b[38;2;243;113;66mChimchar\u001b[38;2;180;180;180m!");
      }
      else if (random == 1) {
        Water starter = new Water("Piplup", 4, 400);
        player.add(starter);
        wait(2);
        System.out.print("\u001b[38;2;180;180;180m");
        System.out.println("\nYou responded too late! The only Pokemon left is \u001b[38;2;103;179;201mPiplup\u001b[38;2;180;180;180m!");
      }
      else if (random == 2) {
        Grass starter = new Grass("Turtwig", 4, 400);
        player.add(starter);
        wait(2);
        System.out.print("\u001b[38;2;180;180;180m");
        System.out.println("\nYou responded too late! The only Pokemon left is \u001b[38;2;119;221;118mTurtwig\u001b[38;2;180;180;180m!");
      }
    }
    wait(2);
    player._numPokemon += 1;
    player.displayPokedex();
    wait(2);
    System.out.print("\u001b[38;2;180;180;180m");
    System.out.println("You also receive 5 Pokeballs and 5 berries from \u001b[38;2;153;153;255mProfessor Rowan\u001b[38;2;180;180;180m.");
    player._numPokeball += 5;
    player._numBerries += 5;
    player.displayInventory();
    wait(2);
    System.out.print("\u001b[38;2;180;180;180m");
    System.out.println("\nYou thank Professor \u001b[38;2;153;153;255mRowan \u001b[38;2;180;180;180mand walk out of the Pokemon Center.");
    wait(1);
    System.out.print("\u001b[38;2;96;96;96m");
    System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------");
    wait(2);
    System.out.print("\u001b[38;2;180;180;180m");

    System.out.println("\nDo you wish to see the tutorial before beginning your Pokemon adventure? (yes/no)");
    wait(1);
    System.out.print("\u001b[38;2;255;255;255m");
    System.out.print("> ");
    System.out.print("\033[?25h"); // SHOW CURSOR
    String tutorial = "";
    tutorial = in.nextLine().toLowerCase();
    System.out.print("\033[?25l"); // HIDE CURSOR
    if (tutorial.equals("yes")) {
      wait(2);
      System.out.print("\u001b[38;2;180;180;180m");
      System.out.println("\n    * There are three types of Pokemon: \u001b[38;2;243;113;66mFire\u001b[38;2;180;180;180m, \u001b[38;2;103;179;201mWater\u001b[38;2;180;180;180m, and \u001b[38;2;119;221;118mGrass\u001b[38;2;180;180;180m.");
      wait(2);
      System.out.println("    * Each Pokemon has three moves: one that attacks, one that defends, and one that restores your Power Points (PP).");
      wait(2);
      System.out.println("    * After your turn, you will be given a choice to consume a Berry. A Berry restores 20 Hit Points (HP).");
      wait(2);
      System.out.println("    * Then, your opponent will be given a chance to make a move.");
      wait(2);
      System.out.println("    * If all of your Pokemon faint, your opponent has won and your Pokemon will be rushed to the Nurse.");
      wait(2);
      System.out.println("    * If you successfully defeat your opponent, you will be either prompted to another battle, keep walking, or face a gym.");
      wait(2);
      System.out.println("    * There are three gyms in total: \u001b[38;2;119;221;118mGrass\u001b[38;2;180;180;180m, \u001b[38;2;103;179;201mWater\u001b[38;2;180;180;180m, and \u001b[38;2;243;113;66mFire\u001b[38;2;180;180;180m, respectively.");
      wait(2);
      System.out.println("    * You are currently in the region with the \u001b[38;2;119;221;118mGrass \u001b[38;2;180;180;180mgym, \u001b[38;2;119;221;118mGardenia\u001b[38;2;180;180;180m.");
      wait(2);
      System.out.println("    * After you beat each gym, you will earn a badge of the respective region's element.");
      wait(2);
      System.out.println("    * If you conquer all three gyms, you will beat the game.");
      wait(2);
      System.out.println("    * That's all you need to know! Have fun and remember to catch them all!");
    }
    else if (tutorial.equals("no")) {
      wait(2);
      System.out.print("\u001b[38;2;180;180;180m");
      System.out.println("\nOkay then, have fun! Remember to catch them all!");
    }
    else {
      wait(2);
      System.out.print("\u001b[38;2;180;180;180m");
      System.out.println("\nThat is not a valid response. Nonetheless, have fun! Remember to catch them all!");
    }
    wait(1);
    System.out.print("\u001b[38;2;96;96;96m");
    System.out.print("\n---------------------------------------------------------------------------------------------------------------------------------");
    System.out.print("\u001b[38;2;180;180;180m");
    clipStarter.stop();
		clipStarter.flush();
		clipStarter.close();
  }

  public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException, Exception {
    Player player = new Player();

    gameSetup(player);
    chooseStarter(player);

    String[] regions = new String[] {"Eterna City", "Pastoria City", "Sunyshore City"};
    int i = 0;
    while (i < 3) {
      boolean nextGym = walk(player, regions[i]);
      if (nextGym == true) {
        wait(1);
        System.out.print("\u001b[38;2;180;180;180m");
        System.out.println("\nYou beat " + regions[i] + "!");
        wait(2);
        System.out.println();
        i++;
      }
    }
    wait(2);
    System.out.println("You beat the game. Congratulations!");
    System.out.print("\u001b[0m");
    System.out.print("\u001B[0m");
    StdAudio.close();
  }
}
