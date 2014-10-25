import java.util.ArrayList;

// Gregory Halverson
// Pierce College
// CS 532
// Spring 2014

public class StringConversion
{
    public static Character [] toCharacterArray(String string)
    {
        ArrayList<Character> characterList = new ArrayList<Character>();

        for (int i = 0; i < string.length(); i++)
            characterList.add(string.charAt(i));

        Character [] characterArray = characterList.toArray(new Character[characterList.size()]);
        return characterArray;
    }

    public static String toString(ArrayList<Character> characterList)
    {
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < characterList.size(); i++)
            output.append(characterList.get(i));

        return output.toString();
    }
}
