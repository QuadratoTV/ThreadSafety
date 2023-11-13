import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

class SharedPoint {
    private int x;
    private int y;

    public void setValues(int x, int y) {
        fetchGoogle();
        this.x = x;
        fetchGoogle();
        this.y = y;
    }

    private void fetchGoogle() {
        try {
            URL url = new URL("http://www.google.com");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printValues() {
        System.out.println("(" + x + ", " + y + ")");
    }
}

class PointSetter implements Runnable {
    private SharedPoint sharedPoint;

    public PointSetter(SharedPoint sharedPoint) {
        this.sharedPoint = sharedPoint;
    }

    @Override
    public void run() {
        sharedPoint.setValues(((int) Thread.currentThread().threadId()) % 2 == 0 ? 1 : 2, ((int) Thread.currentThread().threadId()) % 2 == 0 ? 1 : 2);
    }
}

public class Main {

    public static void main(String[] args) {
        while (true) {
            SharedPoint sharedPoint = new SharedPoint();

            // Erstelle zwei Threads, die dasselbe SharedPoint-Objekt verwenden
            Thread thread1 = new Thread(new PointSetter(sharedPoint));
            Thread thread2 = new Thread(new PointSetter(sharedPoint));

            // Starte die Threads
            thread1.start();
            thread2.start();

            // Warte, bis beide Threads beendet sind
            try {
                thread1.join();
                thread2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Logge die endgültigen Werte des SharedPoint-Objekts
            sharedPoint.printValues();
        }
    }
}