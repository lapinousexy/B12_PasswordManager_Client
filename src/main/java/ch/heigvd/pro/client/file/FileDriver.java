package ch.heigvd.pro.client.file;

import ch.heigvd.pro.client.structure.Entry;
import ch.heigvd.pro.client.structure.Safe;
import ch.heigvd.pro.client.structure.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.crypto.BadPaddingException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

// Source : https://www.tutorialspoint.com/gson/gson_object_serialization.htm
public class FileDriver implements IStorePasswordDriver {

    private File file;
    private Safe safe;

    public FileDriver(Safe safe, File file) {
        this.file = file;
        this.safe = safe;
    }

    /**
     * Load JSON from a file and return the Safe object of the JSON data.
     *
     * @return a Safe object, unserialized from the JSON file
     */
    public Safe loadSafe() {
        try {
            FileReader fr = new FileReader(file);
            GsonBuilder builder = new GsonBuilder();
            Gson parser = builder.create();

            return parser.fromJson(fr, Safe.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Save a Safe object into a JSON format.
     */
    public void saveSafe() {
        safe.encryptPassword();
        try {
            FileWriter fw = new FileWriter(file, false);
            GsonBuilder builder = new GsonBuilder();
            Gson parser = builder.create();

            fw.write(parser.toJson(safe));
            fw.close();

            safe.decryptPassword();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Safe login(char[] username, char[] password) throws Exception {
        return null;
    }

    @Override
    public void createUser(char[] username, char[] email, char[] password) throws Exception {

    }

    @Override
    public User getUserInformation() throws Exception {
        return null;
    }

    @Override
    public void createFolder(String folderName, int safeIndex) throws Exception {
        safe.addFolder(folderName.toCharArray(), 0);
    }

    @Override
    public void deleteFolder(int idFolder, int safeIndex) throws Exception {
        safe.deleteFolder(idFolder);
    }

    @Override
    public void editFolder(char[] folderName, int index, int safeIndex) throws Exception {
        safe.editFolder(folderName, index);
    }

    @Override
    public void addEntry(Entry newEntry, int selectedFolderNumber, int safeIndex) throws Exception {
        this.safe.getFolderList().get(selectedFolderNumber).addEntry(newEntry);
    }

    @Override
    public void editEntry(Entry actualEntry, Entry editedEntry, int safeIndex) throws Exception {
        actualEntry.setUsername(editedEntry.getUsername());
        actualEntry.setEntryName(editedEntry.getEntryName());
        actualEntry.setClearPassword(editedEntry.getClearPassword());
        actualEntry.setTarget(editedEntry.getTarget());
        actualEntry.setNotes(editedEntry.getNotes());
        actualEntry.setIcon(editedEntry.getIcon());
    }

    @Override
    public void deleteEntry(int selectedFolderNumber, int indexOfEntryToRemove, int safeIndex) throws Exception {
        safe.getFolderList().get(selectedFolderNumber).removeEntry(indexOfEntryToRemove);
    }

    @Override
    public void createGroup(char[] groupName) throws Exception {

    }

    @Override
    public Safe getSafe(int safeIndex) {
        return safe;
    }

    @Override
    public void addSafe(Safe safe) {
        this.safe = safe;
    }

    @Override
    public int getSafeSize() {
        return 1;
    }
}
