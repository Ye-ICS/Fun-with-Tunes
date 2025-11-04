import java.util.Scanner;
import java.io.FileNotFoundException;

class App {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(System.in);

        // Menu to choose what tune to play
        int choice;
        do {
            System.out.println("Choose an option:");
            System.out.println("1. Play user-entered notes");
            System.out.println("2. Play notes from file");
            System.out.println("3. Play Twinkle Twinkle Little Star");
            System.out.println("4. Play Moonlight Sonata");
            System.out.println("5. Exit");
            choice = Integer.parseInt(sc.nextLine());
            switch (choice) {
                case 1 -> Tunes.playUserNotes();
                case 2 -> {
                    System.out.println("Enter filename: ");
                    String filename = sc.nextLine();
                    Tunes.playFile(filename);
                }
                case 3 -> Tunes.playTwinkle(60);
                case 4 -> Tunes.playMoonlightSonata(60);
                case 5 -> System.out.println("Exiting...");
                default -> System.out.println("Invalid choice");
            }
        } while (choice != 5);
    }
}