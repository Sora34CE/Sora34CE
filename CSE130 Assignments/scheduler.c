/**
 * See scheduler.h for function details. All are callbacks; i.e. the simulator 
 * calls you when something interesting happens.
 */
#include <stdlib.h>

#include "simulator.h"
#include "scheduler.h"
#include "queue.h"
#include <stdio.h>

static void * ready_queue;
static void * thread_queue;
thread_t *global;

enum algorithm alg;

typedef struct {
  thread_t *thread;
  int arrive;
  int startWait;
  int wait;
  int finish;
} outer_t;

static bool helper(void *outer, void *inner) {
  return ((outer_t *)outer)->thread == (thread_t*) inner;
}
static int inner_comparator(void *a, void *b){
  return ((outer_t*)a)->thread->priority - ((outer_t*)b)->thread->priority;
}

/**
 * Initalise a scheduler implemeting the requested ALGORITHM. QUANTUM is only
 * meaningful for ROUND_ROBIN.
 */
void scheduler(enum algorithm algorithm, unsigned int quantum) {
  alg = algorithm;
  ready_queue = queue_create();
  thread_queue = queue_create();
}

/**
 * Programmable clock interrupt handler. Call sim_time() to find out
 * what tick this is. Called after all calls to sys_exec() for this
 * tick have been made.
 */
void tick() { 
  if (queue_size(ready_queue) > 0 && global == NULL){
    if(alg == NON_PREEMPTIVE_PRIORITY) {
      queue_sort(ready_queue, inner_comparator);
    }
    outer_t *next_outer = queue_dequeue(ready_queue);
    global = next_outer->thread;
    next_outer->wait += sim_time() - next_outer->startWait;
    sim_dispatch(next_outer->thread);
  }
}

/**
 * Tread T is ready to be scheduled for the first time.
 */
void sys_exec(thread_t *t) {
  outer_t *outer = (outer_t *)malloc(sizeof(outer_t));
  outer->thread = t;
  outer->arrive = sim_time();
  outer->startWait = sim_time();
  outer->wait = 0;
  outer->finish = 0;
  queue_enqueue(thread_queue, outer);
  queue_enqueue(ready_queue, outer);
}

/**
 * Thread T has completed execution and should never again be scheduled.
 */
void sys_exit(thread_t *t) {
  global = NULL;
  outer_t *new_outer = queue_find(thread_queue, helper, t);
  new_outer->finish = sim_time();
}

/**
 * Thread T has requested a read operation and is now in an I/O wait queue.
 * When the read operation starts, io_starting(T) will be called, when the
 * read operation completes, io_complete(T) will be called.
 */
void sys_read(thread_t *t) {
  global = NULL;
  outer_t *new_outer = queue_find(thread_queue, helper, t);
  new_outer->startWait = sim_time()+1;
}

/**
 * Thread T has requested a write operation and is now in an I/O wait queue.
 * When the write operation starts, io_starting(T) will be called, when the
 * write operation completes, io_complete(T) will be called.
 */
void sys_write(thread_t *t) {
  sys_read(t);
}

/**
 * An I/O operation requested by T has completed; T is now ready to be 
 * scheduled again.
 */
void io_complete(thread_t *t) {
  outer_t *outer = queue_find(thread_queue, helper, t);
  outer->startWait = sim_time()+1;
  queue_enqueue(ready_queue, outer);
}

/**
 * An I/O operation requested by T is starting; T will not be ready for
 * scheduling until the operation completes.
 */
void io_starting(thread_t *t) {
  outer_t *outer = queue_find(thread_queue, helper, t);
  outer->wait += sim_time() - outer->startWait;
}

/**
 * Return stats for the scheduler simulation, see scheduler.h for details.
 */
stats_t *stats() {
  int thread_count = queue_size(thread_queue);
  stats_t *stats = malloc(sizeof(stats_t));
  stats->tstats = malloc(sizeof(stats_t)*thread_count);
  stats->thread_count = thread_count;
  stats->turnaround_time = 0;    
  stats->waiting_time = 0;
  for(int i = 0; i < stats->thread_count; i++){
    outer_t *outer = queue_dequeue(thread_queue);
    stats->tstats[i].tid = outer->thread->tid;
    stats->tstats[i].turnaround_time = (outer->finish+1) - outer->arrive;
    stats->tstats[i].waiting_time = outer->wait;
    stats->turnaround_time += stats->tstats[i].turnaround_time;
    stats->waiting_time += stats->tstats[i].waiting_time;
  }
  stats->turnaround_time = stats->turnaround_time/stats->thread_count;
  stats->waiting_time = stats->waiting_time/stats->thread_count;
  return stats;
}
