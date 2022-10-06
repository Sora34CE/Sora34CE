/************************************************************************
 * 
 * CSE130 Assignment 1
 * 
 * UNIX Shared Memory Multi-Process Merge Sort
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
  int shmid = shmget(IPC_PRIVATE, 1024, 0666|IPC_CREAT);
  int *intArray = (int*) shmat(shmid, (void*)0,0);
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
        shmdt(intArray);
        shmctl(shmid,IPC_RMID,NULL);
  }
}