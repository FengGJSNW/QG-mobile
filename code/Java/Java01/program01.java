import java.util.Scanner;

public class program01 {
    public static void main(String[] args) {
        Room room = new Room(8, 10);

        while(true) {
            room.showSeats();
            System.out.println("请输入您要购买的座位的行号和列号：");
            Scanner scanner = new Scanner(System.in);
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            scanner.close();
            room.choice(x, y);
        }

    }
}

class Room {
    private int roomRows;
    private int roomCols;
    private int[][] seats;

    Room(int rows, int cols) {
        roomRows = rows;
        roomCols = cols;
        seats = new int[roomRows][roomCols];
    }

    public void choice(int row, int col) {
        if (row >= 1 && row <= roomRows && col >= 1 && col <= roomCols) {
            if (seats[row - 1][col - 1] == 0) {
                seats[row - 1][col - 1] = 1;
                System.out.println("购票成功");
            } else {
                System.out.println("该位置已出售，请重新选择");
            }
        } else {
            System.out.println("该影厅没有该座位，请重新选择");
        }
    }

    public void showSeats() {
        System.out.println("------------- QG影院座位图 ------------");

        System.out.print("    ");
        for (int i = 1; i <= roomCols; i++) {
            System.out.printf("%-3d", i);
        }
        System.out.println();

        for (int i = 0; i < roomRows; i++) {
            System.out.printf("%-3d", i + 1);
            for (int j = 0; j < roomCols; j++) {
                if (seats[i][j] == 0) {
                    System.out.print("[ ]");
                } else {
                    System.out.print("[X]");
                }
            }
            System.out.println();
        }

        System.out.println("--------------------------------------");
    }

    public int outputRow() {
        return roomRows;
    }

    public int outputCol() {
        return roomCols;
    }
}