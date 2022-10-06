#include "cartman.h"
#include <semaphore.h>
#include <pthread.h>
#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
typedef struct cartmanInfo{
  unsigned int cart;
  enum track track;
  enum junction junction;
} cartInfo;

cartInfo c[100];

int trackCounter;

sem_t sem[17];
pthread_t cartmanThread[100];
sem_t semR;

/*
 * Callback when CART on TRACK arrives at JUNCTION in preparation for
 * crossing a critical section of track.
 */
static void *trackAction (void *arg){
  struct cartmanInfo* c = (struct cartmanInfo*) arg;
  if(c->track%2 == 0){
    sem_wait(&sem[c->track]);
    reserve(c->cart, c->track);
    sem_wait(&sem[(c->track+1)%trackCounter]);
    reserve(c->cart, (c->track+1)%trackCounter);
  }
  else{
    sem_wait(&sem[(c->track+1)%trackCounter]);
    reserve(c->cart, (c->track+1)%trackCounter);
    sem_wait(&sem[c->track]);
    reserve(c->cart, c->track);
  }
  cross(c->cart, c->track, c->junction);
  release(c->cart, c->track);
  release(c->cart, (c->track+1)%trackCounter);
  sem_post(&sem[c->track]);
  sem_post(&sem[(c->track+1)%trackCounter]);
  return NULL;
}
void arrive(unsigned int cart, enum track track, enum junction junction) 
{
  c[cart].cart = cart;
  c[cart].track = track;
  c[cart].junction = junction;
  pthread_create(&cartmanThread[cart], NULL, trackAction, (void*)&c[cart]);
  pthread_detach(cartmanThread[cart]);
}

/*
 * Start the CART Manager. 
 *
 * Return is optional, i.e. entering an infinite loop is allowed, but not
 * recommended. Some implementations will do nothing, most will initialise
 * necessary concurrency primitives.
 */
void cartman(unsigned int tracks) 
{
  for(int i = 0; i < tracks; i++){
    sem_init(&sem[i], 0, 1);
  }
  trackCounter = tracks;
}
