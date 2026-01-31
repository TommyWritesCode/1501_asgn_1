import java.util.ArrayList;
import java.util.Random;

public class BoggleGame implements BoggleGameInterface{

    @Override
    public char[][] generateBoggleBoard(int size) {
        if(size <= 0){
            return null;
        }
        int stringLength = size*size;
        if(stringLength <= 0){
            return null;
        }
        String s = generateRandomString(stringLength);
        char[][] board = new char[size][size];
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                board[i][j] = s.charAt(i*size+j);
            }
        }
        return board;
    }

    @Override
    public int countWords(char[][] boggleBoard, DictInterface dictionary) {
        // TODO Implement this method
        return -1;
    }

    @Override
    public int countWordsOfCertainLength(char[][] boggleBoard, DictInterface dictionary, int wordLength) {
        // TODO Implement this method
        return -1;
    }

    @Override
    public boolean isWordInDictionary(DictInterface dictionary, String word) {
        // TODO Implement this method
        return false;
    }

    @Override
    public boolean isWordInBoard(char[][] boggleBoard, String word) {
        // TODO Implement this method
        return false;
    }

    @Override
    public String anyWord(char[][] boggleBoard, DictInterface dictionary) {
        // TODO Implement this method
        return null;
    }

    @Override
    public ArrayList<Tile> markWordInBoard(char[][] boggleBoard, String word) {
        // TODO Implement this method
        return null;
    }

    @Override
    public boolean checkTiles(char[][] boggleBoard, ArrayList<Tile> tiles, String word) {
        // TODO Implement this method
        return false;
    }

    @Override
    public String anyWord(char[][] boggleBoard, DictInterface dictionary, int length) {
        // TODO Implement this method
        return null;
    }

    private String generateRandomString(int length) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = length;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString().toUpperCase();

        // System.out.println(generatedString);
        return generatedString;
    }
    
}
