import java.util.ArrayList;

class Job implements Comparable<Job> {

    private static long NEXT_ARRIVAL_TIME = 0;

    private final int id;
    private final String description;
    private final int priority;
    private final long arrivalTime;

    public Job(int id, String description, int priority) {
        this.id = id;
        this.description = description;
        this.priority = priority;
        this.arrivalTime = NEXT_ARRIVAL_TIME++;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

    public long getArrivalTime() {
        return arrivalTime;
    }

    @Override
    public int compareTo(Job other) {
        if (this.priority != other.priority) {
            return Integer.compare(this.priority, other.priority);
        }
        return Long.compare(this.arrivalTime, other.arrivalTime);
    }

    @Override
    public String toString() {
        return "Job{id=" + id +
               ", priority=" + priority +
               ", desc='" + description + "'}";
    }
}

class MinHeapPriorityQueue<K extends Comparable<K>> {

    private final ArrayList<K> heap = new ArrayList<>();

    public int size() {
        return heap.size();
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    public void insert(K key) {
        heap.add(key);
        upheap(heap.size() - 1);
    }

    public K min() {
        if (heap.isEmpty()) {
            return null;
        }
        return heap.get(0);
    }

    public K removeMin() {
        if (heap.isEmpty()) {
            return null;
        }
        K result = heap.get(0);
        K last = heap.remove(heap.size() - 1);
        if (!heap.isEmpty()) {
            heap.set(0, last);
            downheap(0);
        }
        return result;
    }

    private int parent(int i) {
        return (i - 1) / 2;
    }

    private int left(int i) {
        return 2 * i + 1;
    }

    private int right(int i) {
        return 2 * i + 2;
    }

    private void swap(int i, int j) {
        K temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    private void upheap(int i) {
        while (i > 0) {
            int p = parent(i);
            if (heap.get(i).compareTo(heap.get(p)) >= 0) {
                break;
            }
            swap(i, p);
            i = p;
        }
    }

    private void downheap(int i) {
        int size = heap.size();
        while (true) {
            int left = left(i);
            int right = right(i);
            int smallest = i;

            if (left < size &&
                    heap.get(left).compareTo(heap.get(smallest)) < 0) {
                smallest = left;
            }
            if (right < size &&
                    heap.get(right).compareTo(heap.get(smallest)) < 0) {
                smallest = right;
            }
            if (smallest == i) {
                break;
            }
            swap(i, smallest);
            i = smallest;
        }
    }
}

class Scheduler {

    private final MinHeapPriorityQueue<Job> queue = new MinHeapPriorityQueue<>();

    public void addJob(Job job) {
        queue.insert(job);
    }

    public Job peekNextJob() {
        return queue.min();
    }

    public Job processNextJob() {
        return queue.removeMin();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public int getJobCount() {
        return queue.size();
    }
}

public class SchedulerDemo {

    public static void main(String[] args) {
        Scheduler scheduler = new Scheduler();

        Job j1 = new Job(101, "Normal task", 5);
        Job j2 = new Job(102, "Urgent bug fix", 1);
        Job j3 = new Job(103, "Background cleanup", 8);
        Job j4 = new Job(104, "Customer support ticket", 3);
        Job j5 = new Job(105, "Critical system alert", 1);

        scheduler.addJob(j1);
        scheduler.addJob(j2);
        scheduler.addJob(j3);
        scheduler.addJob(j4);
        scheduler.addJob(j5);

        System.out.println("Jobs added. Total jobs: " + scheduler.getJobCount());
        System.out.println("Processing jobs in priority order:\n");

        while (!scheduler.isEmpty()) {
            Job next = scheduler.processNextJob();
            System.out.println("Processing -> " + next);
        }

        System.out.println("\nAll jobs processed. Scheduler empty? " + scheduler.isEmpty());
    }
}
