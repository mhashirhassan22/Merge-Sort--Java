package com.MergeSort;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Thread;
import java.util.Arrays;

class MergeSort{

   private static int[] mergeSort(int[] array) throws InterruptedException, IOException {
        // Recursive control 'if' statement (base-case)
       if(array.length<=2){
           //if array has two un-sorted elements than swap them to sort
           if(array.length == 2) {
               String fname=Thread.currentThread().getName()+".txt";
               BufferedWriter output = null;
               try {
                   File file = new File(fname);
                   output = new BufferedWriter(new FileWriter(file));
                   output.write("Input Array:  ");
                   output.write(Arrays.toString(array));
                   output.newLine();
               } catch (IOException e ) {
                   e.printStackTrace();
               }
               if(array[0]>array[1]){
                   int temp=array[0];
                   array[0]=array[1];
                   array[1]=temp;
               }
               if (output != null) {
                   output.write("Sorted Array:  ");
                   output.write(Arrays.toString(array));
                   output.close();
               }

           }
           return array;
       }
        else{
           String fname=Thread.currentThread().getName()+".txt";
           BufferedWriter output = null;
           try {
               File file = new File(fname);
               output = new BufferedWriter(new FileWriter(file));
               output.write("Input Array:  ");
               output.write(Arrays.toString(array));
               output.newLine();
           } catch (IOException e ) {
               e.printStackTrace();
           }
            int midpoint = array.length / 2;

            //making int[]  to final int[][] so inner thread class functions can access it
            final int[][] left = {new int[midpoint]};
            final int[][] right = {new int[array.length - midpoint]};

            // Populate the left and right arrays.
            System.arraycopy(array, 0, left[0], 0, midpoint);
            System.arraycopy(array, midpoint, right[0], 0, right[0].length);

            class SortRight extends Thread{
                @Override
                public void run(){
                    try {
                        right[0] =mergeSort(right[0]);
                    } catch (InterruptedException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            class SortLeft extends Thread{
                @Override
                public void run() {
                    try {
                        left[0]=mergeSort(left[0]);
                    } catch (InterruptedException | IOException e) {
                        e.printStackTrace();
                    }
                }

            }
            int[] result;
           SortLeft obj=new SortLeft();
           SortRight ob=new SortRight();
           String lname = Thread.currentThread().getName() + ".l";
           String rname = Thread.currentThread().getName() + ".r";
            obj.setName(lname);
            ob.setName(rname);
            obj.start();       //sort left sub array
            ob.start();       //sort right sub array
            obj.join();
            ob.join();
            result = merge(left[0], right[0]);  //parent thread merging the two sorted arrays from child threads
           if (output != null) {
               output.write("Sorted Array:  ");
               output.write(Arrays.toString(result));
               output.close();
           }

           return result;
        }
    }
    private static int[] merge(int[] l, int[] r){
        int[] res=new int[l.length+r.length];
        int lit,rit,resit;
        lit=rit=resit=0;
        while(lit < l.length || (r.length > rit)){
            if(l.length>lit&&r.length>rit){
                if(l[lit]<r[rit])
                    res[resit++]=l[lit++];
                else
                    res[resit++]=r[rit++];
            }
            else if(l.length>lit)
                res[resit++]=l[lit++];
            else
                res[resit++]=r[rit++];
        }
        return res;

    }
    public static void main(String[] args) throws InterruptedException {
        Thread.currentThread().setName("T");    //required name of main thread
        int[] array = new int[]{6,5,3,1,8,7,2,4};
       try{
            array=mergeSort(array);
            //print sorted array
         /*  for (int i : array) {
               System.out.print(i + " ");
           }*/

       } catch (IOException e) {
           e.printStackTrace();
       }

    }

}