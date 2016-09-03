package no.javazone;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class ActorSleeper {

    public static class ActorMessage<T>  {
        public final T message;
        public final Aktor sender;

        public ActorMessage(T message, Aktor sender) {
            this.message = message;
            this.sender = sender;
        }
    }

    @FunctionalInterface
    public interface Aktor<T> {
        void onMessage(ActorMessage<T> msg);
    }

    static class ActorWithMailbox {
        public final Aktor aktor;
        public Queue<ActorMessage> mailBox = new ConcurrentLinkedQueue<>();

        ActorWithMailbox(Aktor aktor) {
            this.aktor = aktor;
        }

        public void processMessage() {
            ActorMessage msg = mailBox.poll();
            if (msg != null) {
                aktor.onMessage(msg);
            }
        }

    }

    public static class AktorSystem {

        private static final AktorSystem instance = new AktorSystem();
        public static AktorSystem get() {
            return instance;
        }


        int nThreads = Runtime.getRuntime().availableProcessors();
        final ExecutorService actorService = Executors.newFixedThreadPool(nThreads);
        final Map<Class, List<ActorWithMailbox>> actorsPerClass = new ConcurrentHashMap<>();
        final Map<Aktor, ActorWithMailbox> actors = new ConcurrentHashMap<>();
        final List<List<ActorWithMailbox>> actorsPerThread = new CopyOnWriteArrayList<>();

        private AktorSystem() {
            IntStream.range(0, nThreads)
                    .mapToObj(i -> new CopyOnWriteArrayList<ActorWithMailbox>())
                    .peek(list -> actorsPerThread.add(list))
                    .forEach(list -> actorService.submit(() -> {
                        while (!Thread.currentThread().isInterrupted()) {
                            list.forEach(ActorWithMailbox::processMessage);
                        }
                    }));
        }

        public Aktor addActor(Aktor a) {
            if (actors.containsKey(a)) return a;
            ActorWithMailbox withMailbox = new ActorWithMailbox(a);

            if (!actorsPerClass.containsKey(a.getClass())) {
                actorsPerClass.put(a.getClass(), new CopyOnWriteArrayList<>());
            }

            actors.put(a, withMailbox);
            actorsPerClass.get(a.getClass()).add(withMailbox);
            actorsPerThread.get(new Random().nextInt(nThreads)).add(withMailbox);
            return a;
        }

        public void addActors(Supplier<Aktor> actor, int num) {
            IntStream.range(0, num).forEach(i -> addActor(actor.get()));
        }

        public void sendMessage(Class<? extends Aktor> clazz, ActorMessage message) {
            List<ActorWithMailbox> actors = actorsPerClass.get(clazz);
            actors.get(new Random().nextInt(actors.size())).mailBox.offer(message);
        }

        public void sendMessage(Aktor a, ActorMessage msg) {
            actors.get(a).mailBox.offer(msg);
        }

        public void shutDown() {
            actorService.shutdown();
        }

    }

}
