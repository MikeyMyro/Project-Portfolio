/* *****************************************
 * CSCI 205 - Software Engineering and Design
 * Spring 2024
 * Instructor: Prof. Lily Romano
 *
 * Name: Casey King
 * Section: 1:00
 * Date: 4/10/24
 * Time: 1:46 PM
 *
 * Project: csci205_final_project
 * Package: model
 * Class: GridMaker
 *
 * Description:
 *
 * ****************************************
 */
package model;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Class that creates generates a list of Tile objects
 * for a game of Connections
 */
public class GridMaker {

    /** Map of easy mode categories and words */
    private static TreeMap<String, String[]> easyModeMap;

    /** Map of medium mode categories and words */
    private static TreeMap<String, String[]> mediumModeMap;

    /** Map of hard mode categories and words */
    private static TreeMap<String, String[]> hardModeMap;

    /** Map of extreme mode categories and words */
    private static TreeMap<String, String[]> extremeModeMap;

    /** Map of Hollywood mode categories and words */
    private static TreeMap<String, String[]> hollywoodMap;



    /**
     * Makes an easy mode board of Connections
     * @return 4x4 array of Tiles
     * @author Casey king
     */
    public static ArrayList<Tile> makeEasyModeBoard() {
        easyModeMap = new TreeMap<>();

        easyModeMap.put("____ Hall", new String[]{"Roberts", "Larison", "Vedder", "Swartz"});
        easyModeMap.put("Downtown Houses", new String[]{"Duck", "Tank", "Garage", "Bodega"});
        easyModeMap.put("Places to Eat", new String[]{"Bostwick", "Flyson", "Bison", "Commons"});
        easyModeMap.put("Outdoor Sports Facilities", new String[]{"Emmet", "Graham", "Depew", "Becker"});

        return makeBoard(easyModeMap);
    }

    /**
     * Makes a medium mode board of Connections
     * @return 4x4 array of Tiles
     * @author Owen Reilly
     */
    public static ArrayList<Tile> makeMediumModeBoard() {
       mediumModeMap = new TreeMap<>();

        mediumModeMap.put("Tennis Terms", new String[]{"Love", "Ace", "Deuce", "Fault"});
        mediumModeMap.put("Water____", new String[]{"Melon", "Color", "Way", "Fall"});
        mediumModeMap.put("Words with Colors in them", new String[]{"Brownie", "Bred", "Blueberry", "Whiteboard"});
        mediumModeMap.put("Letter Homophones", new String[]{"Why", "See", "Are", "Ex"});

        return makeBoard(mediumModeMap);
    }

    /**
     * Makes a hard mode board of Connections
     * @return 4x4 array of Tiles
     * @author Owen Reilly
     */
    public static ArrayList<Tile> makeHardModeBoard() {
        hardModeMap = new TreeMap<>();

        hardModeMap.put("Legendary Athletes Last Names", new String[]{"Woods", "Ruth", "Bolt", "Phelps"});
        hardModeMap.put("Things in Nature", new String[]{"Forest", "River", "Mountain", "Desert"});
        hardModeMap.put("Palindromes", new String[]{"Racecar", "Deed", "Kayak", "Noon"});
        hardModeMap.put("Star____", new String[]{"Ship", "Fish", "Light", "Wars"});

        return makeBoard(hardModeMap);
    }

    /**
     * Makes an extreme mode board of Connections
     * @return 4x4 array of Tiles
     * @author Owen Reilly
     */
    public static ArrayList<Tile> makeExtremeModeBoard() {
        extremeModeMap = new TreeMap<>();

        extremeModeMap.put("Musicals", new String[]{"Cats", "Hamilton", "Wicked", "Chicago"});
        extremeModeMap.put("Slang to Call Someone Cool", new String[]{"Dog", "Beast", "Animal", "Him"});
        extremeModeMap.put("Wizard of Oz Characters", new String[]{"Witch", "Lion", "Scarecrow", "Wizard"});
        extremeModeMap.put("ACC Team Names", new String[]{"Tiger", "Irish", "Orange", "Hurricane"});

        return makeBoard(extremeModeMap);
    }

    /**
     * Makes Hollywood level board in Connections
     * @return the Board in list format
     * @author Owen Reilly
     */
    public static ArrayList<Tile> makeHollywoodBoard() {
        // image urls
        String rdj = "https://upload.wikimedia.org/wikipedia/commons/9/94/Robert_Downey_Jr_2014_Comic_Con_%28cropped%29.jpg";
        String evans = "https://upload.wikimedia.org/wikipedia/commons/thumb/8/8d/ChrisEvans2023.jpg/800px-ChrisEvans2023.jpg";
        String hemsworth = "https://upload.wikimedia.org/wikipedia/commons/e/e8/Chris_Hemsworth_by_Gage_Skidmore_2_%28cropped%29.jpg";
        String scar = "https://upload.wikimedia.org/wikipedia/commons/thumb/2/2a/Scarlett_Johansson_by_Gage_Skidmore_2_%28cropped%2C_2%29.jpg/640px-Scarlett_Johansson_by_Gage_Skidmore_2_%28cropped%2C_2%29.jpg";
        String brando = "https://upload.wikimedia.org/wikipedia/commons/5/53/Marlon_Brando_publicity_for_One-Eyed_Jacks.png";
        String pacino = "https://upload.wikimedia.org/wikipedia/commons/9/98/Al_Pacino.jpg";
        String deniro = "https://upload.wikimedia.org/wikipedia/commons/2/25/Robert_De_Niro_Cannes_2016_2.jpg";
        String duval = "https://upload.wikimedia.org/wikipedia/commons/0/03/Robert_Duvall_1.jpg";
        String cena = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/60/John_Cena_July_2018.jpg/1200px-John_Cena_July_2018.jpg";
        String bautista = "https://upload.wikimedia.org/wikipedia/commons/thumb/4/40/Dave_Bautista_Photo_Op_GalaxyCon_Minneapolis_2019.jpg/640px-Dave_Bautista_Photo_Op_GalaxyCon_Minneapolis_2019.jpg";
        String rock = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQJYIpe1NAJI16PXDkZTt8tiebkI2dDCn4XV7djOeWVkg&s";
        String andre = "https://b.fssta.com/uploads/application/wwe/headshots/andre-the-giant.vresize.350.350.medium.99.png";
        String murphy = "https://upload.wikimedia.org/wikipedia/commons/5/5a/Cillian_Murphy_at_Berlinale_2024%2C_Ausschnitt.jpg";
        String smith = "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/TechCrunch_Disrupt_2019_%2848834434641%29_%28cropped%29.jpg/640px-TechCrunch_Disrupt_2019_%2848834434641%29_%28cropped%29.jpg";
        String fraser = "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f7/Brendan_Fraser_October_2022.jpg/1200px-Brendan_Fraser_October_2022.jpg";
        String hopkins = "https://upload.wikimedia.org/wikipedia/commons/4/47/AnthonyHopkins10TIFF.jpg";

        hollywoodMap = new TreeMap<String, String[]>();
        hollywoodMap.put("Avengers Actors", new String[]{rdj, evans, hemsworth, scar});
        hollywoodMap.put("Godfather Actors", new String[]{brando, pacino, deniro, duval});
        hollywoodMap.put("Former WWE Stars", new String[]{cena, bautista, rock, andre});
        hollywoodMap.put("Last 4 Oscar Winners", new String[]{murphy, smith, fraser, hopkins});

        return makeBoard(hollywoodMap);
        }


    /**
     * Makes the list of words
      * @param mapOfWords map containing words and their categories
     * @return list of words in Tile class
     */
    private static ArrayList<Tile> makeBoard(TreeMap<String, String[]> mapOfWords) {

        // init new array list
        ArrayList<Tile> listOfTiles = new ArrayList<>();

        int currIndex = 0;

        // Add each in w category
        for(Map.Entry<String, String[]> entry : mapOfWords.entrySet()){
            for(String word : entry.getValue()) {
                listOfTiles.add(new Tile(word, entry.getKey(), currIndex+1));
            }
            currIndex ++;
        }


        return listOfTiles;
    }

}