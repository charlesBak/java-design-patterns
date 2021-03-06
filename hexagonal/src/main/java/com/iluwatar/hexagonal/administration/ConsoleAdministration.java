/**
 * The MIT License
 * Copyright (c) 2014 Ilkka Seppälä
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.iluwatar.hexagonal.administration;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.iluwatar.hexagonal.domain.LotteryAdministration;
import com.iluwatar.hexagonal.domain.LotteryNumbers;
import com.iluwatar.hexagonal.domain.LotteryService;
import com.iluwatar.hexagonal.module.LotteryModule;
import com.iluwatar.hexagonal.mongo.MongoConnectionPropertiesLoader;
import com.iluwatar.hexagonal.sampledata.SampleData;

import java.util.Scanner;

/**
 * Console interface for lottery administration
 */
public class ConsoleAdministration {

  /**
   * Program entry point
   */
  public static void main(String[] args) {
    MongoConnectionPropertiesLoader.load();
    Injector injector = Guice.createInjector(new LotteryModule());
    LotteryAdministration administartion = injector.getInstance(LotteryAdministration.class);
    LotteryService service = injector.getInstance(LotteryService.class);
    SampleData.submitTickets(service, 20);
    Scanner scanner = new Scanner(System.in);
    boolean exit = false;
    while (!exit) {
      printMainMenu();
      String cmd = readString(scanner);
      if (cmd.equals("1")) {
        administartion.getAllSubmittedTickets().forEach((k,v)->System.out.println("Key: " + k + " Value: " + v));
      } else if (cmd.equals("2")) {
        LotteryNumbers numbers = administartion.performLottery();
        System.out.println("The winning numbers: " + numbers.getNumbersAsString());
        System.out.println("Time to reset the database for next round, eh?");
      } else if (cmd.equals("3")) {
        administartion.resetLottery();
        System.out.println("The lottery ticket database was cleared.");
      } else if (cmd.equals("4")) {
        exit = true;
      } else {
        System.out.println("Unknown command: " + cmd);
      }
    }
  }

  private static void printMainMenu() {
    System.out.println("");
    System.out.println("### Lottery Administration Console ###");
    System.out.println("(1) Show all submitted tickets");
    System.out.println("(2) Perform lottery draw");
    System.out.println("(3) Reset lottery ticket database");
    System.out.println("(4) Exit");
  }

  private static String readString(Scanner scanner) {
    System.out.print("> ");
    String cmd = scanner.next();
    return cmd;
  }
}
