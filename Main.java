/*
 * Author: Tarush Gupta
 * Date: February 17, 2026
 * Description: Main driver class for the AB1 Neural Net.
 *              Handles user input for config and exec modes.
 */

import java.util.Scanner;

public class Main
{
   /*
    * Runs the program.
    */
   public static void main(String[] args)
   {
      AB1 network = new AB1();
      Scanner scan = new Scanner(System.in);
      String temp;
      boolean isTraining;
      boolean isRandom;

      System.out.println("Project #2 A-B-1 Network");
      System.out.println("Please choose if you are training or testing.");
      System.out.println("Enter '1' for training or '2' for testing.");

      temp = scan.nextLine();
      isTraining = temp.equals("1");

/*
* Set Config Params
*/
      network.setConfig();
      
/*
* Set Mode
*/
      network.setMode(isTraining);

/*
* Echo Config Params
*/
      network.echoConfig();

/*
* Allocate Memory for Net Arrays
*/
      System.out.println("Allocating your array memory.");
      network.allocateMemory();

/*
* Select Truth Table
*/
      if (isTraining)
      {
         System.out.println("Please select your truth table.");
         System.out.println("Enter '1' for OR, '2' for AND, '3' for XOR, '4' for XOR_AND (A=3).");
         temp = scan.nextLine();
         if (temp.equals("1"))
         {
            network.truthTable = "OR";
         }
         else if (temp.equals("2"))
         {
            network.truthTable = "AND";
         }
         else if (temp.equals("3"))
         {
            network.truthTable = "XOR";
         }
         else if (temp.equals("4"))
         {
            network.truthTable = "XOR_AND";
         }
      } // if (isTraining)

/*
* Populate Arrays
*/
      System.out.println("Please select your initialization method.");
      System.out.println("Enter '1' for fixed (testing) or '2' for random.");

      temp = scan.nextLine();
      isRandom = temp.equals("2");
      
      System.out.println("Understood. Initializing weights.");
      network.populateArrays(isRandom);

/*
* Train or Run
*/
      if (isTraining)
      {
         System.out.println("Do you want to train? (y/n)");
         temp = scan.nextLine();
         if (temp.equalsIgnoreCase("y"))
         {
            network.train();
            network.reportTrainingResults();
         }
         
         System.out.println("Do you want to run the trained network? (y/n)");
         temp = scan.nextLine();
         if (temp.equalsIgnoreCase("y"))
         {
            System.out.println("Running trained network on all cases:");
            network.runAllCases();
            network.reportRunResults();
         }
      } // if (isTraining)
      else
      {
         System.out.println("Running now.");
         network.runAllCases();
         network.reportRunResults();
      }

      scan.close();
   } // main(String[] args)
}
