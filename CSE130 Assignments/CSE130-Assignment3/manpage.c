
/*********************************************************************
 *
 * Copyright (C) 2020-2021 David C. Harrison. All right reserved.
 *
 * You may not use, distribute, publish, or modify this code without 
 * the express written permission of the copyright holder.
 *
 ***********************************************************************/

#include "manpage.h"
#include <semaphore.h>
#include <pthread.h>
#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>

sem_t sems[7]; 
pthread_t t[7];

/*
 * Create a thread for each of seven manpage paragraphs and have them synchronize
 * their invocations of showParagraph() so the manual page displays as follows:
 *
 --------- 8< ---------------------------------------------------
 
A semaphore S is an unsigned-integer-valued variable.
Two operations are of primary interest:
 
P(S): If processes have been blocked waiting on this semaphore,
 wake one of them, else S <- S + 1.
 
V(S): If S > 0 then S <- S - 1, else suspend execution of the calling process.
 The calling process is said to be blocked on the semaphore S.
 
A semaphore S has the following properties:

1. P(S) and V(S) are atomic instructions. Specifically, no
 instructions can be interleaved between the test that S > 0 and the
 decrement of S or the suspension of the calling process.
 
2. A semaphore must be given an non-negative initial value.
 
3. The V(S) operation must wake one of the suspended processes. The
 definition does not specify which process will be awakened.

 --------- 8< ---------------------------------------------------
 *
 * As supplied, shows random single messages.
 */
void *thread_func(void *arg){
  int i = getParagraphId();
  if(i > 0){
    sem_wait(&sems[i]);
  }
  showParagraph();
  if(i<6){
    sem_post(&sems[i+1]);
  }
  return NULL;
}
void manpage() 
{
  for(int i = 0; i < 7; i++){
    sem_init(&sems[i], 0, 0);
  }
  for(int i = 0; i < 7; i++){
    pthread_create(&t[i], NULL, thread_func, (void *)&sems[i]);
  }
  for(int i = 0; i <= 6; i++){
    pthread_exit(NULL);
  }
}
