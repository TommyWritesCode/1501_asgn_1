import java.util.ArrayList;
import java.util.Dictionary;
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
        
        // basic logic checks
        if(word == null || boggleBoard == null || word.length() > (boggleBoard.length * boggleBoard.length))
            return false;

        // seach board for first letter in "word"
        for(int i = 0; i < boggleBoard.length; i++){
            for(int j = 0; j < boggleBoard.length; j++){
                // Used to keep track of what tiles we have used so far
                boolean[][] isUsed = new boolean[boggleBoard.length][boggleBoard.length];
                if(boggleBoard[i][j] == word.charAt(0)){
                    // if DFS function reuturns true its possible to make the desired word starting at [i][j]
                    if(goingDeep(boggleBoard, word, i, j, 0, isUsed)){
                        return true;
                    }
                }
            }
        }
        // if first letter is not found on board return false.
        // b/c it's imposible to craft word with board.
        // no backtracking needed
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

    // a helper method that allows for easy traversal of DFS data structure
    // returns false if word is not found
    public boolean goingDeep(char[][] board, String word, int row, int col, int charIndex, boolean[][] isUsed){

        // Fail state ie init backtracking
        char currValue = board[row][col];
        if(currValue != word.charAt(charIndex)){
            return false;
        }
        
        // Sucess Case (found a path for entire length of "word")
        if(charIndex == word.length() - 1)
            return true;

        // mark curr position as used
        isUsed[row][col] = true;

        Tile currTile = new Tile(row,col);
        // Recursive Step
        for(int i = 0; i < 8; i++){
            Tile neighborTile = calcNeighbor(currTile, i);
            
            if(neighborTile == null){
                throw new  RuntimeException("WRONG MODE FOR NEIGHBOR CALC....");
            } 
            
            if(inBounds(board, neighborTile)){
                // if neigboring tile is not used.....
                if(!isUsed[neighborTile.row][neighborTile.col]){
                    // check to see if neigboring tile contains the intended character
                    if(board[neighborTile.row][neighborTile.col] == word.charAt(charIndex + 1)){
                        // Increment charIndex because we are looking for the next occuring char when we recurse
                        // putitng it in a new variable for recursing so we dont mess up this instance of the funcition in the event of backtracking
                        int nextIndex = charIndex + 1;
                        // if all characters are found return true
                        if(goingDeep(board, word, neighborTile.row, neighborTile.col, nextIndex, isUsed)) return true;
                        
                    }
                }
            }
        }

        // unmark tile as used b/c it was not usable in this instance
        isUsed[row][col] = false;                    
        // If none of the neigboring cells worked the depth first search failed and no word was found
        return false;
    }

    public String wordFinder(char[][] board, DictInterface dict, StringBuilder word, int row, int col, boolean[][] isUsed, int minChars){

        // Var used to keep track of whether or not our SB contains a prefix or a word
        int word_dictStatus;

        // Logical check built around DictInterface.java
        if(dict == null){
            return null;
        }
        else{
            word_dictStatus = dict.searchPrefix(word);
        }

        // Fail State(s)
        // check word is neither a prefix nor a word in the dictonary
        if(word_dictStatus == 0){
            return null;
        }

        // is prefix but not a word
        if(word_dictStatus == 1 && word.length() >= board.length * board.length){
            return null;
        }

        // sucess state(s)
        if((word.length() >= minChars) && ((word_dictStatus == 2) || (word_dictStatus == 3))){
            String validWord = word.toString();
            return validWord;
        }
        

        // mark curr position as used
        isUsed[row][col] = true;

        Tile currTile = new Tile(row,col);
        // Recursive Step
        for(int i = 0; i < 8; i++){
            Tile neighborTile = calcNeighbor(currTile, i);
            
            if(neighborTile == null){
                throw new  RuntimeException("WRONG MODE FOR NEIGHBOR CALC....");
            } 
            
            if(inBounds(board, neighborTile)){
                // if neigboring tile is not used.....
                if(!isUsed[neighborTile.row][neighborTile.col]){
                    word.append(board[neighborTile.row][neighborTile.col]);
                    // Recursive step w/ neighbor tile
                    String found = wordFinder(board, dict, word, neighborTile.row, neighborTile.col, isUsed, minChars);
                    if(found != null){
                        isUsed[row][col] = false;
                        return found; 
                    } 
                    else
                        word.deleteCharAt(word.length() -1); // The latest appended char was not compatable                      
                }   
            }        
        }

        // If none of the neigboring cells worked the depth first search failed and no word was found
        // unmark tile as used b/c it was not usable in this instance
        isUsed[row][col] = false; 
        return null;
    }


    public boolean goingDeepPath(char[][] board, String word, int row, int col, int charIndex, boolean[][] isUsed, ArrayList<Tile> tiles){

        // Fail state ie init backtracking
        char currValue = board[row][col];
        if(currValue != word.charAt(charIndex)){
            return false;
        }
        
        // Sucess Case (found a path for entire length of "word")
        if(charIndex == word.length() - 1){
            return true;
        }

        Tile currTile = new Tile(row,col);
        // mark curr position as used
        isUsed[row][col] = true;
        // add tile to array list
        tiles.add(currTile);

        // Recursive Step
        for(int i = 0; i < 8; i++){
            Tile neighborTile = calcNeighbor(currTile, i);
            
            if(neighborTile == null){
                throw new  RuntimeException("WRONG MODE FOR NEIGHBOR CALC....");
            } 
            
            if(inBounds(board, neighborTile)){
                // if neigboring tile is not used.....
                if(!isUsed[neighborTile.row][neighborTile.col]){
                    // check to see if neigboring tile contains the intended character
                    if(board[neighborTile.row][neighborTile.col] == word.charAt(charIndex + 1)){
                        // putitng it in a new variable for recursing so we dont mess up this instance of the funcition in the event of backtracking
                        int nextIndex = charIndex + 1;
                        // if all characters are found return true
                        if(goingDeepPath(board, word, neighborTile.row, neighborTile.col, nextIndex, isUsed, tiles)) return true;
                        
                    }
                }
            }
        }

        // unmark tile as used b/c it was not usable in this instance
        isUsed[row][col] = false;
        tiles.remove(tiles.size() - 1);                    
        // If none of the neigboring cells worked the depth first search failed and no word was found
        return false;
    }    

    @Override
    public String anyWord(char[][] boggleBoard, DictInterface dictionary) {
        // looping through entire board searching for the first desired char
        for(int i = 0; i < boggleBoard.length; i ++){
            for(int j = 0; j < boggleBoard.length; j++){
                
                StringBuilder boggle_word = new StringBuilder();
                boggle_word.append(boggleBoard[i][j]);

                int word_dictStatus = dictionary.searchPrefix(boggle_word);
                if(word_dictStatus == 1 || word_dictStatus == 3){
                    boolean[][] isUsed = new boolean[boggleBoard.length][boggleBoard.length];
                    String found = wordFinder(boggleBoard,dictionary,boggle_word,i,j,isUsed,3);
                    if(found != null) return found;
                }
            }
        }
                    
        return null;
    }

    @Override
    public ArrayList<Tile> markWordInBoard(char[][] boggleBoard, String word) {
        // basics
        if(boggleBoard == null || word == null){
            return null;
        }
        word = word.toUpperCase(); // normalize input

        // looping through entire board searching for the first desired char
        for(int i = 0; i < boggleBoard.length; i ++){
            for(int j = 0; j < boggleBoard.length; j++){
                if(boggleBoard[i][j] == word.charAt(0)){
                    boolean[][] isUsed = new boolean[boggleBoard.length][boggleBoard.length];
                    ArrayList<Tile> path = new ArrayList<>();

                    // if a path is possible for word return the path
                    if(goingDeepPath(boggleBoard, word, i, j, 0, isUsed, path))
                        return path;
                }
            }
        }

        return null;

    }

    @Override
    public boolean checkTiles(char[][] boggleBoard, ArrayList<Tile> tiles, String word) {
        // basics
        if (boggleBoard == null || tiles == null || word == null) return false;
        if(tiles.size() != word.length()) return false;

        word = word.toUpperCase();

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
        // looping through entire board searching for the first desired char
        for(int i = 0; i < boggleBoard.length; i ++){
            for(int j = 0; j < boggleBoard.length; j++){
                
                StringBuilder boggle_word = new StringBuilder();
                boggle_word.append(boggleBoard[i][j]);

                int word_dictStatus = dictionary.searchPrefix(boggle_word);
                if(word_dictStatus == 1 || word_dictStatus == 3){
                    boolean[][] isUsed = new boolean[boggleBoard.length][boggleBoard.length];
                    String found = wordFinder(boggleBoard,dictionary,boggle_word,i,j,isUsed,length);
                    if(found != null) return found;
                }
            }
        }
                    
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
