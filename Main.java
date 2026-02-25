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

      network.setConfig(); //set the configuration parameters

      network.echoConfig(); //echo the config parameters to the users

      network.allocateMemory(); //allocate memory for the network arrays

      network.populateArrays(); //populate the network arrays.

/*
* Train if the network is training.
*/
      if (network.isTraining)
      {
         network.train();
         network.reportTrainingResults();
      }

/*
* Run if the network is running. 
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
