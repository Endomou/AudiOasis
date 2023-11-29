package com.project;
import java.util.Comparator;

public class MergeSort {
    public Song[] ascendMerge(Song[] arr, Comparator<Song> comparator) {
        if (arr.length <= 1) {
            return arr;
        }

        int mid = arr.length / 2;
        Song[] left = new Song[mid];
        Song[] right = new Song[arr.length - mid];

        System.arraycopy(arr, 0, left, 0, mid);
        System.arraycopy(arr, mid, right, 0, arr.length - mid);

        left = ascendMerge(left, comparator);
        right = ascendMerge(right, comparator);

        return merge(arr, left, right, comparator);
    }

    public Song[] descendMerge(Song[] arr, Comparator<Song> comparator) {
        if (arr.length <= 1) {
            return arr;
        }

        int mid = arr.length / 2;
        Song[] left = new Song[mid];
        Song[] right = new Song[arr.length - mid];

        System.arraycopy(arr, 0, left, 0, mid);
        System.arraycopy(arr, mid, right, 0, arr.length - mid);

        left = descendMerge(left, comparator);
        right = descendMerge(right, comparator);

        return merge(arr, left, right, comparator.reversed());
    }

    private Song[] merge(Song[] arr, Song[] left, Song[] right, Comparator<Song> comparator) {
        int i = 0, j = 0, k = 0;

        while (i < left.length && j < right.length) {
            if (comparator.compare(left[i], right[j]) <= 0) {
                arr[k] = left[i];
                i++;
            } else {
                arr[k] = right[j];
                j++;
            }
            k++;
        }

        while (i < left.length) {
            arr[k] = left[i];
            i++;
            k++;
        }

        while (j < right.length) {
            arr[k] = right[j];
            j++;
            k++;
        }

        return arr;
    }

    public void displayArray(Song[] arr) {
        for (Song song : arr) {
            System.out.println(song.getTitle() + " - " + song.getArtist());
        }
        System.out.println();
    }
}
