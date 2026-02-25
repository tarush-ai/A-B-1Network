/*
* Author: Tarush Gupta
* Date: February 19, 2026
* Description: A-B-1 Neural Network with gradient descent training.
*              Supports configurable layers, rand/fixed init, and training/running modes.
*/
public class AB1
{
/*
* Network config
*/
   public int inputLayer; 
   public int hiddenLayer;
   public double lambda;
   public double low;
   public double high;
   public int maxIterations;
   public double errorThreshold;
   public boolean useRandomWeights;
   public int numCases; 
   
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
   public double[][] inputs;
/*
* Training arrays
*/
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
   public boolean shouldRun;
   public boolean showInputTable;
   public boolean showTruthTable;

/*
* Sets config for the network. 
*/
   public void setConfig()
   {
      inputLayer = 2;
      hiddenLayer = 2;
      numCases = 4;
      low = -1.5;
      high = 1.5;
      maxIterations = 100000;
      lambda = 0.3;
      errorThreshold = 2e-4;
      useRandomWeights = true; 
      isTraining = true;
      shouldRun = true;
      showInputTable = true;
      showTruthTable = true;
   } // setConfig()

/*
* Prints current network config.
*/
   public void echoConfig()
   {
      System.out.println("Network Configuration: " + inputLayer + "-" + hiddenLayer + "-1");
      System.out.println("Number of Test Cases: " + numCases);
      System.out.println("Weight Initialization: " + (useRandomWeights ? "Random" : "Fixed"));
      System.out.println("Show Input Table: " + showInputTable);
      System.out.println("Show Truth Table: " + showTruthTable);
      System.out.println("Run After Training: " + shouldRun);
      System.out.println("Mode: " + (isTraining ? "Training" : "Running"));

      if (isTraining)
      {
         System.out.println("\nTraining Parameters:");
         System.out.println("Random Number Range: [" + low + ", " + high + "]");
         System.out.println("Max Iterations: " + maxIterations);
         System.out.println("Error Threshold: " + errorThreshold);
         System.out.println("Lambda: " + lambda);
      }
   } // echoConfig()

/*
* Allocates memory for all network arrays.
*/
   public void allocateMemory()
   {
      a = new double[inputLayer];
      h = new double[hiddenLayer];
      wKJ = new double[inputLayer][hiddenLayer];
      wJ0 = new double[hiddenLayer];
      thetaJ = new double[hiddenLayer];
      
      inputs = new double[numCases][inputLayer];
      results = new double[numCases];

      if (isTraining)
      {
         targets = new double[numCases];
         deltaWKJ = new double[inputLayer][hiddenLayer];
         deltaWJ0 = new double[hiddenLayer];
         omegaJ = new double[hiddenLayer];
         psiJ = new double[hiddenLayer];
      }
   } // allocateMemory()

/*
* Populate all of the network arrays based on the type of initialization
* (i.e. random or fixed) and whether the network is training or not.
*/
   public void populateArrays()
   {
      setInputTable();

      if (isTraining)
      {
         setTruthTable();
      }

      if (useRandomWeights)
      {
         useRandomWeights();
      } 
      else
      {
         useFixedWeights();
      } 
   } // populateArrays()

/*
* Sets input values for all relevant test cases, contingent on the function
* that is being optimized, such as AND, OR, XOR, or a[0] XOR a[1] AND a[2]
* for A=3.
*/
   public void setInputTable()
   {
      inputs[0][0] = 0.0; inputs[0][1] = 0.0;
      inputs[1][0] = 0.0; inputs[1][1] = 1.0;
      inputs[2][0] = 1.0; inputs[2][1] = 0.0;
      inputs[3][0] = 1.0; inputs[3][1] = 1.0;

/*
* a[0] XOR a[1] AND a[2] (A=3)
* inputs[0][0] = 0.0; inputs[0][1] = 0.0; inputs[0][2] = 0.0;
* inputs[1][0] = 0.0; inputs[1][1] = 0.0; inputs[1][2] = 1.0;
* inputs[2][0] = 0.0; inputs[2][1] = 1.0; inputs[2][2] = 0.0;
* inputs[3][0] = 0.0; inputs[3][1] = 1.0; inputs[3][2] = 1.0;
* inputs[4][0] = 1.0; inputs[4][1] = 0.0; inputs[4][2] = 0.0;
* inputs[5][0] = 1.0; inputs[5][1] = 0.0; inputs[5][2] = 1.0;
* inputs[6][0] = 1.0; inputs[6][1] = 1.0; inputs[6][2] = 0.0;
* inputs[7][0] = 1.0; inputs[7][1] = 1.0; inputs[7][2] = 1.0;
*/

/*
* Conventional
* inputs[0][0] = 0.0; inputs[0][1] = 0.0;
* inputs[1][0] = 0.0; inputs[1][1] = 1.0;
* inputs[2][0] = 1.0; inputs[2][1] = 0.0;
* inputs[3][0] = 1.0; inputs[3][1] = 1.0;
*/
   } // setInputTable()

/*
* Prints the input table by iterating through the inputs.
*/
   public void printInputTable()
   {
      System.out.println("\nInput Table:");
      for (int c = 0; c < numCases; ++c)
      {
         for (int k = 0; k < inputLayer; ++k)
         {
            System.out.print(inputs[c][k] + "\t");
         }
         System.out.println();
      }
   } // printInputTable()

/*
* Prints the truth table by iterating through the inputs and targets.
*/
   public void printTruthTable()
   {
      System.out.println("\nTruth Table:");
      for (int c = 0; c < numCases; ++c)
      {
         for (int k = 0; k < inputLayer; ++k)
         {
            System.out.print(inputs[c][k] + "\t");
         }
         System.out.println("| " + targets[c]);
      }
   } // printTruthTable()

/*
* Initializes the weights randomly using the randomize function.
*/
   public void useRandomWeights()
   {
      for (int k = 0; k < inputLayer; ++k)
      {
         for (int j = 0; j < hiddenLayer; ++j)
         {
            wKJ[k][j] = randomize(low, high);
         }
      }
      for (int j = 0; j < hiddenLayer; ++j)
      {
         wJ0[j] = randomize(low, high);
      }
   } // useRandomWeights()

/*
* Uses fixed weights for 2-2-1 testing.
*/
   public void useFixedWeights()
   {
/*
* Fixed weights for a 2-2-1 network, optimized for XOR.
*/
      if (inputLayer == 2 && hiddenLayer == 2)
      {
         wKJ[0][0] = 0.76;
         wKJ[0][1] = 0.9;
         wKJ[1][0] = 0.9;
         wKJ[1][1] = 0.76;
         wJ0[0] = 0.2;
         wJ0[1] = 0.2;
      }
   } // useFixedWeights()

/*
* Sets the truth table targets for A=2 based on the truthTable config
* for OR, AND, and XOR.
*/
   public void setTruthTable()
   {
/*
* XOR
* targets[0] = 0.0;
* targets[1] = 1.0;
* targets[2] = 1.0;
* targets[3] = 0.0;
*/

/*
* AND
* targets[0] = 0.0;
* targets[1] = 0.0;
* targets[2] = 0.0;
* targets[3] = 1.0;
*/

/*
* OR
*/
      targets[0] = 0.0;
      targets[1] = 1.0;
      targets[2] = 1.0;
      targets[3] = 1.0;

/*
*a[0] XOR a[1] AND a[2] (a specific test case for A=3)
* targets[0] = 0.0; 
* targets[1] = 0.0; 
* targets[2] = 0.0; 
* targets[3] = 1.0; 
* targets[4] = 0.0; 
* targets[5] = 1.0; 
* targets[6] = 0.0; 
* targets[7] = 0.0;
*/
   } // setTruthTable()

/*
* Returns a random double between low and high. It uses 
* Math.random().
*/
   public double randomize(double low, double high)
   {
      return low + Math.random() * (high - low);
   } // randomize(double low, double high)

/*
* Generalizable activation function. User can specify this to be
* sigmoid or linear. 
*/
   public double activation(double x)
   {
      return sigmoid(x);
   }

/*
* Generalizable activation function derivative. User can specify this
* to be the derivative of sigmoid or linear.
*/
   public double activationDerivative(double num)
   {
      return sigmoidDerivative(num);
   }

/*
* Sigmoid activation: f(x) = 1 / (1 + e^-x). Uses Math.exp().
*/
   public double sigmoid(double x)
   {
      return 1.0 / (1.0 + Math.exp(-x));
   } // sigmoid(double x)

/*
* Sigmoid activation derivative: f'(x) = sigmoid (1 - sigmoid)
*/
   public double sigmoidDerivative(double x)
   {
      double s = sigmoid(x);
      return s * (1.0 - s);
   }

/*
* Linear activation: f(x) = x
*/
   public double linear(double x)
   {
      return x;
   }

/*
* Linear activation derivative: f'(x) = 1
*/
   public double linearDerivative(double x)
   {
      return 1.0;
   }

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
      for (j = 0; j < hiddenLayer; ++j)
      {
         thetaJ[j] = 0.0;
         for (k = 0; k < inputLayer; ++k)
         {
            thetaJ[j] += a[k] * wKJ[k][j];
         }
         h[j] = activation(thetaJ[j]);
      }

/*
* Output
*/
      theta0 = 0.0;
      for (j = 0; j < hiddenLayer; ++j)
      {
         theta0 += h[j] * wJ0[j];
      }
      F0 = activation(theta0);
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
      int j;
      int k;
      double totalError;

      while (iteration < maxIterations && avgError >= errorThreshold)
      {
         totalError = 0.0;

/*
* Reset deltas
*/
         for (j = 0; j < hiddenLayer; ++j)
         {
            deltaWJ0[j] = 0.0;
            for (k = 0; k < inputLayer; ++k)
            {
               deltaWKJ[k][j] = 0.0;
            }
         }

         for (caseIndex = 0; caseIndex < numCases; ++caseIndex)
         {
            populateInputAndRun(caseIndex);

            omega0 = targets[caseIndex] - F0;
            totalError += 0.5 * omega0 * omega0;

/*
* gradient descent
*/
            psi0 = omega0 * activationDerivative(theta0);

            for (j = 0; j < hiddenLayer; ++j)
            {
               deltaWJ0[j] += lambda * psi0 * h[j];

               omegaJ[j] = psi0 * wJ0[j];
               psiJ[j] = omegaJ[j] * activationDerivative(thetaJ[j]);
               
               for (k = 0; k < inputLayer; ++k)
               {
                  deltaWKJ[k][j] += lambda * a[k] * psiJ[j];
               }
            } // for (j = 0; j < hiddenLayer; ++j)
         } // for (caseIndex = 0; caseIndex < numCases; ++caseIndex)
         
/*
* Apply weight changes
*/
         for (j = 0; j < hiddenLayer; ++j)
         {
            wJ0[j] += deltaWJ0[j];
            for (k = 0; k < inputLayer; ++k)
            {
               wKJ[k][j] += deltaWKJ[k][j];
            }
         }

         avgError = totalError / (double)numCases;
         iteration++;
      } // while (iteration < maxIterations && avgError >= errorThreshold)

      trainIterations = iteration;
      trainError = avgError;
   } // train()

/*
* Reports training exit info: reason, iterations, and final error.
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
* Stores each output in results. Identical to runTestCases().
*/
   public void runTrainingCases()
   {
      int caseIndex;

      for (caseIndex = 0; caseIndex < numCases; ++caseIndex)
      {
         populateInputAndRun(caseIndex);
         results[caseIndex] = F0;
      }
   } // runTrainingCases()

/*
* Loops over all test cases, setting inputs and running the network.
* Stores each output in results. Identical to runTrainingCases().
*/
   public void runTestCases()
   {
      int caseIndex;

      for (caseIndex = 0; caseIndex < numCases; ++caseIndex)
      {
         populateInputAndRun(caseIndex);
         results[caseIndex] = F0;
      }
   } // runTestCases()

/*
* Sets input activations from the given case and runs the network.
*/
   public void populateInputAndRun(int caseIndex)
   {
      for (int k = 0; k < inputLayer; ++k)
      {
         a[k] = inputs[caseIndex][k];
      }
      run();
   }

/*
* Reports run results with expected vs actual output.
*/
   public void reportRunResults()
   {
      int caseIndex;
      int k;

      if (showInputTable)
      {
         System.out.print("\nInputs\t\t");
      }
      if (showTruthTable && isTraining)
      {
         System.out.print("| Expected\t");
      }
      System.out.println("| Actual");

      for (caseIndex = 0; caseIndex < numCases; ++caseIndex)
      {
         if (showInputTable)
         {
            for (k = 0; k < inputLayer; ++k)
            {
               System.out.print(inputs[caseIndex][k] + "\t");
            }
         }

         if (showTruthTable && isTraining)
         {
            System.out.print("| " + targets[caseIndex] + "\t\t");
         }

         System.out.println("| " + results[caseIndex]);
      }
   } // reportRunResults()
} // class AB1
