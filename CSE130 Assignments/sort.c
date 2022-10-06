#include "merge.h"
#include <pthread.h>

struct newArr {
  int *arr;
  int left;
  int right;
};

/* LEFT index and RIGHT index of the sub-array of ARR[] to be sorted */
void singleThreadedMergeSort(int arr[], int left, int right) 
{
  if (left < right) {
    int middle = (left+right)/2;
    singleThreadedMergeSort(arr, left, middle); 
    singleThreadedMergeSort(arr, middle+1, right); 
    merge(arr, left, middle, right); 
  } 
}

/* 
 * This function stub needs to be completed
 */
 void *startRoutine2(void *arg){
  struct newArr *ar = (struct newArr *) arg;
  singleThreadedMergeSort(ar->arr, ar->left, ar->right);
  return NULL;
}

void *startRoutine(void *arg){
  struct newArr *ar = (struct newArr *) arg;
  int mid = (ar->left+ar->right)/2;
  pthread_t t3;

  struct newArr a3;
  a3.arr = ar->arr;
  a3.left = ar->left;
  a3.right = mid;
  pthread_t t4;
  struct newArr a4;
  a4.arr = ar->arr;
  a4.left = mid+1;
  a4.right = ar->right;
  pthread_create(&t3, NULL, startRoutine2, &a3);
  pthread_create(&t4, NULL, startRoutine2, &a4);
  pthread_join(t4, NULL);
  pthread_join(t3, NULL);
  merge(ar->arr, ar->left, mid, ar->right);
  return NULL;
}

void multiThreadedMergeSort(int arr[], int left, int right) 
{
  int mid = (left+right)/2;
  pthread_t t1;
  struct newArr a1;
  a1.arr = arr;
  a1.left = left;
  a1.right = mid;
  pthread_t t2;
  struct newArr a2;
  a2.arr = arr;
  a2.left = mid+1;
  a2.right = right;
  
  pthread_create(&t1, NULL, startRoutine, &a1);
  pthread_create(&t2, NULL, startRoutine, &a2);
  pthread_join(t2, NULL);
  pthread_join(t1, NULL);
  merge(arr, left, mid, right);
}
