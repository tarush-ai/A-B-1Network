/*
* Author: Tarush Gupta
* Date: February 17, 2026
* Description: A-B-1 Neural Network with backprop training.
*              Supports configurable layers, rand/fixed init, and training/running modes.
*/

public class AB1
{
/*
* Network config
*/
   public int A;
   public int B;
   public double lambda;
   public double low;
   public double high;
   public int maxIterations;
   public double errorThreshold;
   public String truthTable;
   
/*
* Network arrays
*/
   public double[] a;
   public double[] h;
   public double F0;
   public double[][] wKJ;
   public double[] wJ0;
   public double[] thetaJ;
   public double theta0;
   
/*
* Training arrays
*/
   public double[][] inputs;
   public double[] targets;
   public double[][] deltaWKJ;
   public double[] deltaWJ0;
   public double[] results;
   public double[] omegaJ;
   public double[] psiJ;
   public double omega0;
   public double psi0;
   
/*
* Training results
*/
   public int trainIterations;
   public double trainError;
   
   public boolean isTraining;
   public boolean showInputTable;
   public boolean showTruthTable;

   /*
    * Sets config for the network. 
    */
   public void setConfig()
   {
      A = 2;
      B = 2;

      low = -1.5;
      high = 1.5;
      maxIterations = 100000;
      lambda = 0.3;
      errorThreshold = 2e-4;
      truthTable = "XOR";
      showInputTable = true;
      showTruthTable = true;
   } // setConfig()

   /*
    * Sets training or running mode.
    */
   public void setMode(boolean training)
   {
      isTraining = training;
   } // setMode(boolean training)

   /*
    * Prints current network config.
    */
   public void echoConfig()
   {
      System.out.println("Your defined network configuration is:");
      System.out.println("" + A + "-" + B + "-1");

      if (isTraining)
      {
         System.out.println("Training Configuration:");
         System.out.println("Random Number Range: " + low + "-" + high);
         System.out.println("Max Iterations: " + maxIterations);
         System.out.println("Error Threshold: " + errorThreshold);
         System.out.println("Lambda value: " + lambda);
      }
   } // echoConfig()

   /*
    * Allocates memory for all network arrays.
    */
   public void allocateMemory()
   {
      a = new double[A];
      h = new double[B];
      wKJ = new double[A][B];
      wJ0 = new double[B];
      thetaJ = new double[B];
      
      int numCases = (int)power(2.0, A);
      inputs = new double[numCases][A];
      results = new double[numCases];

      if (isTraining)
      {
         targets = new double[numCases];
         deltaWKJ = new double[A][B];
         deltaWJ0 = new double[B];
         omegaJ = new double[B];
         psiJ = new double[B];
      }
   } // allocateMemory()

   /*
    * Populates network arrays.
    * @param random true for rand init, false for fixed
    */
   public void populateArrays(boolean random)
   {
      int k;
      int j;

      if (A == 2)
      {
         inputs[0][0] = 0.0; inputs[0][1] = 0.0;
         inputs[1][0] = 0.0; inputs[1][1] = 1.0;
         inputs[2][0] = 1.0; inputs[2][1] = 0.0;
         inputs[3][0] = 1.0; inputs[3][1] = 1.0;

         if (isTraining)
         {
            setTruthTable2();
         }
      } // if (A == 2)
      else if (A == 3)
      {
         inputs[0][0] = 0.0; inputs[0][1] = 0.0; inputs[0][2] = 0.0;
         inputs[1][0] = 0.0; inputs[1][1] = 0.0; inputs[1][2] = 1.0;
         inputs[2][0] = 0.0; inputs[2][1] = 1.0; inputs[2][2] = 0.0;
         inputs[3][0] = 0.0; inputs[3][1] = 1.0; inputs[3][2] = 1.0;
         inputs[4][0] = 1.0; inputs[4][1] = 0.0; inputs[4][2] = 0.0;
         inputs[5][0] = 1.0; inputs[5][1] = 0.0; inputs[5][2] = 1.0;
         inputs[6][0] = 1.0; inputs[6][1] = 1.0; inputs[6][2] = 0.0;
         inputs[7][0] = 1.0; inputs[7][1] = 1.0; inputs[7][2] = 1.0;

         if (isTraining)
         {
            setTruthTable3();
         }
      } // else if (A == 3)

/*
* Initialize weights
*/
      if (random)
      {
         for (k = 0; k < A; ++k)
         {
            for (j = 0; j < B; ++j)
            {
               wKJ[k][j] = randomize(low, high);
            }
         }
         for (j = 0; j < B; ++j)
         {
            wJ0[j] = randomize(low, high);
         }
      } // if (random)
      else
      {
/*
* Fixed weights for 2-2-1 testing
*/
         if (A == 2 && B == 2)
         {
            wKJ[0][0] = 0.76;
            wKJ[0][1] = 0.9;
            wKJ[1][0] = 0.9;
            wKJ[1][1] = 0.76;
            wJ0[0] = 0.2;
            wJ0[1] = 0.2;
         }
      } // else
   } // populateArrays(boolean random)

   /*
    * Sets the truth table targets for A=2 based on the truthTable config.
    * Supports OR, AND, and XOR.
    */
   public void setTruthTable2()
   {
      if (truthTable.equals("OR"))
      {
         targets[0] = 0.0;
         targets[1] = 1.0;
         targets[2] = 1.0;
         targets[3] = 1.0;
      }
      else if (truthTable.equals("AND"))
      {
         targets[0] = 0.0;
         targets[1] = 0.0;
         targets[2] = 0.0;
         targets[3] = 1.0;
      }
      else if (truthTable.equals("XOR"))
      {
         targets[0] = 0.0;
         targets[1] = 1.0;
         targets[2] = 1.0;
         targets[3] = 0.0;
      }
   } // setTruthTable2()

   /*
    * Sets the truth table targets for A=3 based on the truthTable config.
    * Supports XOR_AND: a[0] XOR a[1] AND a[2].
    */
   public void setTruthTable3()
   {
      if (truthTable.equals("XOR_AND"))
      {
         targets[0] = 0.0;
         targets[1] = 0.0;
         targets[2] = 0.0;
         targets[3] = 1.0;
         targets[4] = 0.0;
         targets[5] = 1.0;
         targets[6] = 0.0;
         targets[7] = 0.0;
      }
   } // setTruthTable3()

   /*
    * Returns a random double between low and high.
    */
   public double randomize(double low, double high)
   {
      return low + Math.random() * (high - low);
   } // randomize(double low, double high)

   /*
    * Calculates base raised to exp.
    */
   public double power(double base, int exp)
   {
      double result = 1.0;
      int n;
      for (n = 0; n < exp; ++n)
      {
         result *= base;
      }
      return result;
   } // power(double base, int exp)

   /*
    * Sigmoid activation: 1 / (1 + e^-x).
    */
   public double sigmoid(double x)
   {
      return 1.0 / (1.0 + Math.exp(-x));
   } // sigmoid(double x)

   /*
    * Runs the network once using current input activations.
    */
   public void run()
   {
      int j;
      int k;

/*
* Hidden layer
*/
      for (j = 0; j < B; ++j)
      {
         thetaJ[j] = 0.0;
         for (k = 0; k < A; ++k)
         {
            thetaJ[j] += a[k] * wKJ[k][j];
         }
         h[j] = sigmoid(thetaJ[j]);
      }

/*
* Output
*/
      theta0 = 0.0;
      for (j = 0; j < B; ++j)
      {
         theta0 += h[j] * wJ0[j];
      }
      F0 = sigmoid(theta0);
   } // run()

   /*
    * Trains the network using steepest descent across all training cases.
    * Stores final iteration count and error in trainIterations and trainError.
    */
   public void train()
   {
      int iteration = 0;
      double avgError = 1.0;
      int caseIndex;
      int k;
      int j;
      double totalError;

      while (iteration < maxIterations && avgError >= errorThreshold)
      {
         totalError = 0.0;

/*
* Reset deltas
*/
         for (j = 0; j < B; ++j)
         {
            deltaWJ0[j] = 0.0;
            for (k = 0; k < A; ++k)
            {
               deltaWKJ[k][j] = 0.0;
            }
         }

         for (caseIndex = 0; caseIndex < inputs.length; ++caseIndex)
         {
            for (k = 0; k < A; ++k)
            {
               a[k] = inputs[caseIndex][k];
            }

            run();

            omega0 = targets[caseIndex] - F0;
            totalError += 0.5 * omega0 * omega0;

/*
* Backprop
*/
            psi0 = omega0 * F0 * (1.0 - F0);

            for (j = 0; j < B; ++j)
            {
               deltaWJ0[j] += lambda * psi0 * h[j];

               omegaJ[j] = psi0 * wJ0[j];
               psiJ[j] = omegaJ[j] * h[j] * (1.0 - h[j]);
               
               for (k = 0; k < A; ++k)
               {
                  deltaWKJ[k][j] += lambda * a[k] * psiJ[j];
               }
            } // for (j = 0; j < B; ++j)
         } // for (caseIndex = 0; caseIndex < inputs.length; ++caseIndex)
         
/*
* Apply weight changes
*/
         for (j = 0; j < B; ++j)
         {
            wJ0[j] += deltaWJ0[j];
            for (k = 0; k < A; ++k)
            {
               wKJ[k][j] += deltaWKJ[k][j];
            }
         }

         avgError = totalError / (double)inputs.length;
         iteration++;
      } // while (iteration < maxIterations && avgError >= errorThreshold)

      trainIterations = iteration;
      trainError = avgError;
   } // train()

   /*
    * Reports training exit information: reason, iterations, and final error.
    */
   public void reportTrainingResults()
   {
      System.out.println("Training Exit Information:");
      if (trainIterations >= maxIterations)
      {
         System.out.println("Reason: Max iterations reached.");
      }
      if (trainError < errorThreshold)
      {
         System.out.println("Reason: Error threshold reached.");
      }
      System.out.println("Iterations reached: " + trainIterations);
      System.out.println("Average Error reached: " + trainError);
   } // reportTrainingResults()

   /*
    * Loops over all test cases, setting inputs and running the network.
    * Stores each output in results.
    */
   public void runAllCases()
   {
      int caseIndex;
      int k;

      for (caseIndex = 0; caseIndex < inputs.length; ++caseIndex)
      {
         for (k = 0; k < A; ++k)
         {
            a[k] = inputs[caseIndex][k];
         }

         run();
         results[caseIndex] = F0;
      }
   } // runAllCases()

   /*
    * Prints input table (optional), truth table (optional), and outputs.
    * Display controlled by showInputTable and showTruthTable config fields.
    */
   public void reportRunResults()
   {
      int caseIndex;
      int k;

      System.out.println("\nRun Results:");

      if (showInputTable)
      {
         System.out.print("Input");
         for (k = 0; k < A; ++k)
         {
            System.out.print("\t");
         }
      }

      if (showTruthTable && isTraining)
      {
         System.out.print("Target\t");
      }
      System.out.println("Output");

      for (caseIndex = 0; caseIndex < inputs.length; ++caseIndex)
      {
         if (showInputTable)
         {
            for (k = 0; k < A; ++k)
            {
               System.out.print(inputs[caseIndex][k] + "\t");
            }
         }

         if (showTruthTable && isTraining)
         {
            System.out.print(targets[caseIndex] + "\t");
         }
         System.out.println(results[caseIndex]);
      } // for (caseIndex = 0; caseIndex < inputs.length; ++caseIndex)
   } // reportRunResults()
}
