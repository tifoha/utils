package net.tifoha;

import java.awt.peer.ListPeer;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
//        System.out.println("Hello World!");
//        Random r = new Random();
        int[] arr = new int[]{2, 5, 5, 4, 8, 5, 4, 8, 8, 9};
        for (int i = arr.length/2-1; i >=0; i--) {
            sift(arr, i);
        }
        System.out.println(Arrays.toString(arr));
        sift(arr, 0);
        System.out.println(Arrays.toString(arr));
    }

    public static void sift(int[] arr, int i) {
        int max = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        if (left < arr.length && arr[left] > arr[max]) {
            max = left;
        }

        if (right < arr.length && arr[right] > arr[max]) {
            max = right;
        }

        if (max != i) {
            swap(arr, i, max);
            sift(arr, max);
        }
    }

    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }
}
