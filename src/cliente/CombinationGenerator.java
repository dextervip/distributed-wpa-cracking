package cliente;

/**
 *
 * @author Rafael
 */
import java.io.*;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

public class CombinationGenerator {

    private char[] characterString;
    private int characterStringLength;
    private int minCharacters;
    private int maxCharacters;
    private int actualPart;
    private int totalParts;
    private String filePath;
    private char[] currentString;
    private int symbols;
    private String stringToSave;
    private FileWriter writer;
    private String[] stringsArray;
    private int[] startCombination;
    private int startCombinationSymbols;
    private int[] endCombination;
    private int endCombinationSymbols;
    private long[] addToSum;
    private List<String> list;
    private int symbolsUsed;

    public CombinationGenerator() {
        characterString = new char[1];
        characterString[0] = 0;
        currentString = new char[1];
        currentString[0] = 0;
        symbols = 0;
        stringToSave = "";
    }

    public void setCharacterSet(String characterSet) {
        characterString = characterSet.toCharArray();
        characterStringLength = characterSet.length();
    }

    public void setMinCharacter(int num) {
        minCharacters = num;
    }

    public void setMaxCharacter(int num) {
        maxCharacters = num;
        currentString = new char[num];
    }

    public void setActualPart(int part) {
        actualPart = part;
    }

    public void setTotalParts(int parts) {
        totalParts = parts;
    }

    public void setPath(String path) {
        filePath = path;
    }

    public void startProceeds() {
        // calculate combinations
        long combinations = 1;
        long combinationsSum = 0;
        for (int i = 1; i < minCharacters; i++) {
            combinations *= characterStringLength;
        }
        for (int i = minCharacters; i <= maxCharacters; i++) {
            combinations *= characterStringLength;
            combinationsSum += combinations;
        }

        // calculate length of one part
        long lengthOfPart = combinationsSum / totalParts;

        // calculate start index for a combinations set (a part)
        long indexStart;
        if (actualPart == 1) {
            indexStart = 0;
        } else {
            indexStart = (actualPart - 1) * lengthOfPart;
        }

        // calculate end index for a combinations set (a part)
        long indexEnd;
        if (actualPart == totalParts) {
            indexEnd = combinationsSum - 1;
        } else {
            indexEnd = indexStart + lengthOfPart - 1;
        }

        // find a number of combinations which add with increasing a symbol in current position
        addToSum = new long[maxCharacters];
        addToSum[maxCharacters - 1] = 1;
        for (int i = maxCharacters - 2; i >= minCharacters - 1; i--) {
            addToSum[i] = 1 + addToSum[i + 1] * characterStringLength;
        }
        for (int i = minCharacters - 2; i >= 0; i--) {
            addToSum[i] = addToSum[i + 1] * characterStringLength;
        }

        // initialization of array with indexes of symbols in start and final combinations
        startCombination = new int[maxCharacters];
        endCombination = new int[maxCharacters];
        for (int i = 0; i < maxCharacters; i++) {
            startCombination[i] = 0;
            startCombinationSymbols = 1;
            endCombination[i] = 0;
            endCombinationSymbols = maxCharacters;
        }

        // if have a 1st part - start indexes = 0
        long currentCombinations = 1;
        int currentIndex = 0;
        if (actualPart != 1) {
            while (currentCombinations != (indexStart + 1)) {
                currentCombinations += addToSum[currentIndex];
                startCombination[currentIndex]++;
                if (currentCombinations > (indexStart + 1)) {
                    currentCombinations -= addToSum[currentIndex];
                    startCombination[currentIndex]--;
                    if (currentIndex + 1 >= minCharacters) {
                        currentCombinations++;
                    }
                    currentIndex++;
                }
            }
            startCombinationSymbols = currentIndex + 1;
        }

        // if have a last part - final indexes = index of last symbol in source string
        currentCombinations = 1;
        currentIndex = 0;
        if (actualPart != totalParts) {
            while (currentCombinations - 1 != indexEnd) {
                currentCombinations += addToSum[currentIndex];
                endCombination[currentIndex]++;
                if (currentCombinations - 1 > indexEnd) {
                    currentCombinations -= addToSum[currentIndex];
                    endCombination[currentIndex]--;
                    if (currentIndex + 1 >= minCharacters) {
                        currentCombinations++;
                    }
                    currentIndex++;
                }
            }
            endCombinationSymbols = currentIndex + 1;
        } else {
            for (int i = 0; i < maxCharacters; i++) {
                endCombination[i] = characterStringLength - 1;
                endCombinationSymbols = maxCharacters;
            }
        }

        stringsArray = new String[maxCharacters - minCharacters + 1];
        for (int i = 0; i < maxCharacters - minCharacters + 1; i++) {
            stringsArray[i] = "";
        }

        proceedAPart();
    }

    public void proceedAPart() {
        // generate a list of strings with one letter
        list = new ArrayList<String>();
        for (int i = startCombination[0]; i <= endCombination[0]; i++) {
            list.add(Character.toString(characterString[i]));
        }
        symbolsUsed = 1;

        if (symbolsUsed >= minCharacters) {
            saveList();
        }
        proceed();
    }

    public void proceed() {
        // if we used all elements - exit
        symbolsUsed++;
        if (symbolsUsed > maxCharacters) {
            return;
        }

        if (startCombinationSymbols < symbolsUsed) {
            for (int i = symbolsUsed - 1; i < maxCharacters; i++) {
                startCombination[i] = 0;
            }
            startCombinationSymbols = maxCharacters;
        }

        if (endCombinationSymbols < symbolsUsed) {
            for (int i = symbolsUsed - 1; i < maxCharacters; i++) {
                endCombination[i] = characterStringLength - 1;
            }
            endCombinationSymbols = maxCharacters;
            list.remove(list.size() - 1);
        }

        // generate next level list
        List<String> newList = new ArrayList<String>();
        int countElements = list.size();
        String str;

        // if has only one element in list
        if (countElements == 1) {
            str = list.get(0);
            // create new elements with current in begin
            for (int i = startCombination[symbolsUsed - 1]; i <= endCombination[symbolsUsed - 1]; i++) {
                newList.add(str + Character.toString(characterString[i]));
            }
        } else if (countElements == 2) {
            str = list.get(0);
            for (int i = startCombination[symbolsUsed - 1]; i < characterStringLength; i++) {
                newList.add(str + Character.toString(characterString[i]));
            }
            str = list.get(1);
            for (int i = 0; i <= endCombination[symbolsUsed - 1]; i++) {
                newList.add(str + Character.toString(characterString[i]));
            }
        } else {
            str = list.get(0);
            for (int i = startCombination[symbolsUsed - 1]; i < characterStringLength; i++) {
                newList.add(str + Character.toString(characterString[i]));
            }
            for (int j = 1; j < countElements - 1; j++) {
                str = list.get(j);
                for (int i = 0; i < characterStringLength; i++) {
                    newList.add(str + Character.toString(characterString[i]));
                }
            }
            str = list.get(countElements - 1);
            for (int i = 0; i <= endCombination[symbolsUsed - 1]; i++) {
                newList.add(str + Character.toString(characterString[i]));
            }
        }
        list = newList;

        if (symbolsUsed >= minCharacters) {
            saveList();
        }
        proceed();

    }

    public void saveList() {
        try {
            writer = new FileWriter(new File(filePath), true);
            Iterator it = list.iterator();
            while (it.hasNext()) {
                writer.write((String) it.next() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }
}