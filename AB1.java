public class AB1
{
   private float[] a;
   private float[] h;
   private float[][] w_hidden;
   private float[][] w_output;
   private float f;
   private float low;
   private float high;
   private int a_length;
   private int h_length;
   
   public void AB1()
   {

   }

   public void main()
   {
         

   }

   public void setConfig()
   {
      this.a_length = 2;
      this.h_length = 2;

   }

   public void allocateArrayMemory()
   {
      a_length = this.a_length
      h_length = this.h_length
      this.a = new float[a_length];
      this.h = new float[h_length];
      this.w = new float[a_length][h_length];
   }

   public void populateArrayMemory()
   {
      for (k = 0; k < this.a_length; ++k)
      {
         a[k] = randomize(low, high);
      }

   }


   public float forward()
   {
      for (j = 0; j < this.h_length; ++j)
      {
         float sum = 0.0;
         for (k = 0; k < this.a_length; ++k)
         {
            sum += a[k] * w_hidden[k][j];
         }
         h[j] = sum;
      }
      float sum = 0.0;
      for (j = 0; j < this.h_length; ++j)
      {
         sum += h[j] * w_output[j][0];
      }
      f = sum;
      return f;
   }

   public void train(int truth)
   {
      f = forward()
      
      E = 0.5 * (to - fo);

   }


   public void report(boolean training)
   {
      System.out.println("Hello! Here are the specifications of this method.");
      if (training)
      {
         System.out.println("Network Configuration: " + a.length + "-" + h.length + "-1");
         System.out.println("Random Number Range: " + low + "-" + high);
         System.out.println("Max Iterations: " + max);
         System.out.println("Error Threshold: " + lambda);
         System.out.println("Lambda value: " + lambda);
      }
   }


   public void randomize(float low, float high)
   {
      //randomizes, take from earlier code
   }

}