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

    public boolean inBounds(char[][] board, Tile tile){
        int n = board.length;
        return tile.row >= 0 && tile.row < n 
            && tile.col >= 0 && tile.col < n;
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
        StringBuilder sb = new StringBuilder(word);
        if(dictionary.searchPrefix(sb) != 2 && dictionary.searchPrefix(sb) != 3){
            return false;
        }
        return true;
    }

    @Override
    public boolean isWordInBoard(char[][] boggleBoard, String word) {
        // TODO Implement this method
        return false;
    }

    // KEY FOR MODE
    // 0 == N, 1 == NE, 2 == E, 3 == SE,
    // 4 == S, 5 == SW, 6 == W, 7 == NW.  
    public Tile calcNeighbor(Tile currTile, int mode){
        
        int row = currTile.row;
        int col = currTile.col;
        Tile neighborTile = new Tile(row,col);

        if(mode == 0){
            neighborTile.row = neighborTile.row - 1;
            return neighborTile;
        }
        if(mode == 1){
            neighborTile.row = neighborTile.row - 1;
            neighborTile.col = neighborTile.col + 1;
            return neighborTile;
        }
        if(mode == 2){
            neighborTile.col = neighborTile.col + 1;
            return neighborTile;
        }
        if(mode == 3){
            neighborTile.row = neighborTile.row + 1;
            neighborTile.col = neighborTile.col +1;
            return neighborTile;
        }
        if(mode == 4){
            neighborTile.row = neighborTile.row + 1;
            return neighborTile;
        }
        if(mode == 5){
            neighborTile.row = neighborTile.row + 1;
            neighborTile.col = neighborTile.col - 1;
            return neighborTile;
        }
        if(mode == 6){
            neighborTile.col = neighborTile.col - 1;
            return neighborTile;
        }
        if(mode == 7){
            neighborTile.row = neighborTile.row - 1;
            neighborTile.col = neighborTile.col - 1;
            return neighborTile;
        }
        
        else return null;
    }

    
    // HEY YOU STUPID PROGRAMMER!!!!!!
    // THIS NEEDS FIXING!!!!!
    public boolean goingDeep(char[][] board, String word, int row, int col, int charIndex, boolean[][] isUsed){

        // Base Case (found a path for entire length of "word")
        if(index == word.length() - 1)
            return true;

        // mark curr position as used
        isUsed[row][col] = true;

        Tile currTile = new Tile(row,col);
        // Recursive Step
        for(int i = 0; i < 7; i++){
            Tile neighborTile = calcNeighbor(currTile, i);
            if(neighborTile == null) throw Exception("WRONG MODE FOR NEIGHBOR CALC");
            
            if(inBounds(board, neighborTile));{
                if(!isUsed[neighborTile.row][neighborTile.col]){
                    if(board[neighborTile.row][neighborTile.col] == word.charAt(charIndex)){
                        isUsed[neighborTile.row][neighborTile.col] = true;
                        charIndex++;
                        goingDeep(board, word, neighborTile.row, neighborTile.col, charIndex, visited);
                    }
                }
            }
            // unmark tile as used b/c atleast one of the above tests failed.
            isUsed[neighborTile.row][neighborTile.col] = false;            
        }
        // If none of the neigboring cells worked the depth first search failed and no word was found
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
        // basics
        if (boggleBoard == null || tiles == null || word == null) return false;
        if(tiles.size() != word.length()) return false;

        // A data strucutre to keep track of what tile have been used
        int n = boggleBoard.length;
        boolean[][] used = new boolean[n][n];

        for(int i = 0; i < tiles.size(); i++){
            Tile tile = tiles.get(i);
            if(tile == null) return false;

            int row = tile.row;
            int col = tile.col;
            
            // Bounds check
            if(!inBounds(boggleBoard, tile)) return false;

            // already used?
            if(used[row][col]) return false;
            // if not used, mark it as used.
            used[row][col] = true;

            // letter check
            if(boggleBoard[row][col] != word.charAt(i)) return false;

            // tile adjacency check
            if(i > 0){
                Tile prevTile = tiles.get(i-1);
                if(!isAdjacent(prevTile, tile)) return false;
            }

        }
        // if all of those tests passed, then we know that our selected tiles are valid.
        return true;
    }

    // Using pythagorean therum to check if two tiles are adjacent
    public boolean isAdjacent(Tile tile, Tile tileCompared){
        
        int row = tile.row;
        int rowCompared = tileCompared.row;
        int col = tile.col;
        int colCompared = tileCompared.col;

        double distance_y = row - rowCompared;
        double distance_x = col - colCompared;

        // If the distance is greater than sqrt(1 + 1) we know that the tiles are not adjacent
        double distance = Math.sqrt((distance_x * distance_x) + (distance_y * distance_y));
        if(distance > Math.sqrt(2)){
            return false;
        }
        return true;

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
