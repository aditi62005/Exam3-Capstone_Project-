Smart Scheduler – Requirements & Design (Option B)
1. Problem Overview

Many modern systems receive more incoming tasks than can be processed immediately, such as OS job scheduling, ER triage, loan processing. Some tasks have higher urgency compared to others and must be processed before tasks that arrived earlier but have lower priority.

The aim of this project is to implement, in Java, a Smart Scheduler module which manages a stream of tasks, always selecting the highest-priority task next. We will model this using a Min-Heap–based Priority Queue.


2. Real-World Context

Example contexts:

Operating system job scheduler (short CPU bursts with high priority).

Emergency room triage (critical patients first).

Banking / loan processing (VIP or time-sensitive requests first).

For this project, I will treat it generically as a “job scheduler” that receives Job objects and always processes the smallest priority value (e.g., 1 = critical, 10 = low).

3. Functional Requirements

a. Add job

Input: job info (id, description, priority, optional metadata).

Behavior: insert job into the scheduler.

Data structure: MinHeapPriorityQueue<Job> keyed by job priority.

b. View next job

Behavior: return (but do not remove) the highest-priority job.

If the scheduler is empty, return null or an appropriate message.

c. Process next job

Behavior: remove and return the highest-priority job.

Used to simulate executing the next task.

d. Check if scheduler is empty

Behavior: return true if there are no jobs to process.

e. View number of jobs

Behavior: return the number of jobs currently waiting.

Demo scenario (driver)

f. Create several jobs with different priorities.

Insert in non-sorted order.

Repeatedly process jobs and print the order to show that high-priority jobs are handled first.

 4. Non-Functional Requirements

Efficiency: Insertion and removal must be no worse than O(log n) to handle many jobs.

Scalability: The scheduler should support a large number of jobs without a large performance drop.

Simplicity: Clean, readable, object-oriented design using familiar Java collections (ArrayList).

5. Data Structures & Algorithms
Chosen Structures

a. Job class

Represents a single unit of work.

Fields (initial plan):

int id

String description

int priority (smaller = more urgent)

Optional: long arrivalTime, int estimatedDuration

Implements Comparable<Job> based on priority (and possibly arrivalTime as a tiebreaker).

b. MinHeapPriorityQueue<K extends Comparable<K>>

Backed by ArrayList<K> heap.

Maintains the min-heap property: heap[parent(i)] <= heap[i] for all nodes.

Core operations:

insert(K key) → upheap

removeMin() → remove root, move last to root, downheap

min() → read root

isEmpty(), size()

c. Scheduler

Wraps the priority queue specifically for Job.

Methods:

addJob(Job j) → pq.insert(j)

peekNextJob() → pq.min()

processNextJob() → pq.removeMin()

isEmpty() / getJobCount()

d. SchedulerDemo (driver)

Contains main.

Creates Scheduler instance.

Adds jobs with various priorities.

Processes jobs while printing the order.

6. Big-O Complexity Justification

Let n be the number of jobs currently in the scheduler.

a. Min-Heap Priority Queue

Underlying representation: ArrayList used as a binary heap.

insert(job)

Places new element at the end (O(1)) and then “bubbles up” via upheap.

Upheap potentially walks from leaf to root: maximum height = O(log n).

Time: O(log n)

removeMin()

Removes root (index 0), moves the last element to the root, then performs downheap.

Downheap may move from root to deepest level: height = O(log n).

Time: O(log n)

min()

Reads root element heap.get(0).

Time: O(1)

isEmpty(), size()

Just check heap.isEmpty() or heap.size().

Time: O(1)

b. Why a Heap Instead of a List?

Unsorted list

Insert: O(1)

Find/remove min: O(n)

Sorted list

Insert: O(n) (must find position)

Remove min: O(1) (head)

Binary heap (chosen)

Insert: O(log n)

Remove min: O(log n)

Both operations are balanced and asymptotically better than O(n).

Because the scheduler must handle a continuous stream of jobs with frequent inserts and removals, a heap-based priority queue offers a good trade-off and is widely used in real job schedulers.

7. UML DIAGRAM 
+------------------------+
|        Job             |
+------------------------+
| - id: int              |
| - description: String  |
| - priority: int        |
| - arrivalTime: long    |
+------------------------+
| + compareTo(Job): int  |
+------------------------+

              implements Comparable<Job>
                        ^
                        |
+---------------------------------------+
| MinHeapPriorityQueue<K extends        |
|           Comparable<K>>              |
+---------------------------------------+
| - heap: ArrayList<K>                  |
+---------------------------------------+
| + insert(K): void                     |
| + removeMin(): K                      |
| + min(): K                            |
| + isEmpty(): boolean                  |
| + size(): int                         |
| - upheap(int): void                   |
| - downheap(int): void                 |
+---------------------------------------+

                uses
Scheduler ------------------> MinHeapPriorityQueue<Job>

+---------------------------+
|        Scheduler          |
+---------------------------+
| - queue: MinHeapPriorityQueue<Job> |
+---------------------------+
| + addJob(Job): void       |
| + peekNextJob(): Job      |
| + processNextJob(): Job   |
| + isEmpty(): boolean      |
| + getJobCount(): int      |
+---------------------------+

+---------------------------+
|     SchedulerDemo         |
+---------------------------+
| + main(String[]): void    |
+---------------------------+









