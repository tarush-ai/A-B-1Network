/*
* Author: Tarush Gupta
* Date: February 19, 2026
* Description: Main driver class for the AB1 Neural Net.
*              Execution flow driven entirely by setConfig().
*/
public class Main
{
/*
* Runs the program.
*/
   public static void main(String[] args)
   {
      AB1 network = new AB1();
/*
* Set Config Params
*/
      network.setConfig();
/*
* Echo Config Params
*/
      network.echoConfig();
/*
* Allocate Memory for Net Arrays
*/
      network.allocateMemory();
/*
* Populate Arrays
*/
      network.populateArrays();

/*
* Train
*/
      if (network.isTraining)
      {
         network.train();
         network.reportTrainingResults();
      }

/*
* Run
*/
      if (network.shouldRun)
      {
         if (network.isTraining)
         {
            network.runTrainingCases();
         }
         else
         {
            network.runTestCases();
         }
         network.reportRunResults();
      } // if (network.shouldRun)
   } // main(String[] args)
} // class Main
