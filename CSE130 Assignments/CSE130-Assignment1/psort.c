/************************************************************************
 * 
 * CSE130 Assignment 1
 *  
 * POSIX Shared Memory Multi-Process Merge Sort
 * 
 * Copyright (C) 2020-2021 David C. Harrison. All right reserved.
 *
 * You may not use, distribute, publish, or modify this code without 
 * the express written permission of the copyright holder.
 *
 ************************************************************************/

#include "merge.h"
#include <sys/ipc.h>
#include <sys/shm.h>
#include <sys/wait.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <stdlib.h>
#include <sys/mman.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <sys/types.h>


/* 
 * Merge Sort in the current process a sub-array of ARR[] defined by the 
 * LEFT and RIGHT indexes.
 */
void singleProcessMergeSort(int arr[], int left, int right) 
{
  if (left < right) {
    int middle = (left+right)/2;
    singleProcessMergeSort(arr, left, middle); 
    singleProcessMergeSort(arr, middle+1, right); 
    merge(arr, left, middle, right); 
  } 
}

/* 
 * Merge Sort in the current process and at least one child process a 
 * sub-array of ARR[] defined by the LEFT and RIGHT indexes.
 */
void multiProcessMergeSort(int arr[], int left, int right) 
{
  int mid = (left+right)/2;
  const char* name = "SM";
  int shmid = shm_open(name, O_CREAT | O_RDWR, 0666);
  ftruncate(shmid, 1024);
  int *intArray = (int *) mmap(0, 1024, PROT_WRITE, MAP_SHARED, shmid, 0);
  for (int i = left; i <= mid; i++){
    intArray[i] = arr[i];
  }
  switch (fork()){
    case -1:
        exit(-1);
    case 0:
        singleProcessMergeSort(intArray, left, mid);
        exit(-1);
    default:
        singleProcessMergeSort(arr,mid+1,right);
        wait(NULL);
        for (int i = left; i <= mid; i++){
          arr[i] = intArray[i];
        }
        merge(arr, left, mid, right);
        munmap(0, 1024);
        shm_unlink(name);
        close(shmid);
  }
}
