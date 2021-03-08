package org.tomw.imagedao.fileimpl;

import javafx.scene.image.Image;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.tomw.fileutils.FileSelector;
import org.tomw.fileutils.FileTransporter;
import org.tomw.fileutils.TomwFileUtils;
import org.tomw.imagedao.ImageDao;
import org.tomw.imagedao.ImageDaoException;
import org.tomw.imagedao.ImageIdToFileConverter;
import org.tomw.mediautils.MediaFileUtils;
import org.tomw.utils.IdGenerator;
import org.tomw.utils.JsonFileUtils;
import org.tomw.utils.TomwStringUtils;
import org.tomw.utils.UniqueIdGenerator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageDaoFileImpl implements ImageDao {
    private final static Logger LOGGER = Logger.getLogger(ImageDaoFileImpl.class.getName());

    private File baseDirectory;
    private String baseDirectoryString;
    //private ImageIdToFileConverter converter;
    private IdGenerator idGenerator = new UniqueIdGenerator();
    private Map<String,File> id2FileMap = new HashMap<>();

    private File imagesFile;
    private String imagesFileName="images.json";
    private JsonFileUtils jsonUtils = new JsonFileUtils();


    public ImageDaoFileImpl(File dir) throws ImageDaoException {
        baseDirectory=dir.getAbsoluteFile();
        TomwFileUtils.mkdirs(baseDirectory);
        baseDirectoryString = TomwStringUtils.backslash2slash(baseDirectory.toString());
        imagesFile = new File(baseDirectory,imagesFileName);
        load();
    }

    /**
     * Convert short file name to full file by appending in front of it
     * base directory. Short file is written into json persistence
     * long file is in id-> file mappings
     * @param shortFileName short file name
     * @return fullFileName full file name, including base
     */
    File toFullFileName(File shortFileName){
        return new File(baseDirectory,shortFileName.toString());
    }

    /**
     * Convert full file name to short file by appending in front of it
     * base directory. Short file is written into json persistence
     * long file is in id-> file mappings
     * @param fullFileName full name of file, including base directory
     * @return shortFileName short name, base directory truncated
     */
    File toShortFileName(File fullFileName){
        String withoutBackslash = TomwStringUtils.backslash2slash(fullFileName.toString());
        return new File(withoutBackslash.replace(baseDirectoryString,""));
    }
    /**
     * Load file metadata from json file
     * @throws ImageDaoException if something goes wrong
     */
    public void load() throws ImageDaoException {
        if(imagesFile.exists()){
            try {
                JSONObject json = jsonUtils.jsonObjectFromFile(imagesFile);
                for(Object o: json.keySet()){
                    String key = (String)o;
                    String val = (String)json.get(key);
                    id2FileMap.put(key,new File(baseDirectory,val));
                }
            } catch (IOException e) {
                String message="Cannot read file "+imagesFile;
                LOGGER.error(message);
                throw new ImageDaoException(message);
            } catch (ParseException e) {
                String message="Cannot parse file "+imagesFile;
                LOGGER.error(message);
                throw new ImageDaoException(message);
            }
        }
    }

    /**
     * persist file metadata in json file
     */
    @SuppressWarnings("unchecked")
    public void commit() throws ImageDaoException {
        JSONObject json = new JSONObject();
        for(String id : id2FileMap.keySet()){
            File f = id2FileMap.get(id);
            //String val = f.toString().replace(baseDirectory.toString(),"");
            String val = toShortFileName(f.getAbsoluteFile()).toString();
            val = TomwStringUtils.backslash2slash(val);
            json.put(id,val);
        }
        try {
            jsonUtils.jsonObject2File(json,imagesFile);
        } catch (IOException e) {
            String message="Cannot write to file "+imagesFile;
            LOGGER.error(message);
            throw new ImageDaoException(message);
        }
    }


    @Override
    public boolean containsImage(String id) {
        return id2FileMap.containsKey(id);
    }

    @Override
    public Image getImage(String id) {
        if(id2FileMap.containsKey(id)){
            File imageFile = id2FileMap.get(id);
            return new Image(imageFile.toURI().toString());
        }else{
            return null;
        }
    }

    @Override
    public String upload(Image image) {
        //TODO implement
        throw new RuntimeException("Not implemented");
    }

    @Override
    public String upload(File imageFile) throws ImageDaoException {
        return upload(imageFile,false);
    }

    @Override
    public String upload(File imageFile, boolean deleteSource) throws ImageDaoException {
        File destination = new File(baseDirectory,imageFile.getName());
        if(deleteSource){
            try {
                FileTransporter.moveFile(imageFile,destination);
                String id = idGenerator.generate();
                id2FileMap.put(id,destination);
                return id;
            } catch (IOException e) {
                String message="Failed to upload/move file "+imageFile+" to "+baseDirectory;
                LOGGER.error(message);
                throw new ImageDaoException(message);
            }
        }else{
            try {
                FileTransporter.copyFile(imageFile,destination);
                String id = idGenerator.generate();
                id2FileMap.put(id,destination);
                return id;
            } catch (IOException e) {
                String message="Failed to upload/copy file "+imageFile+" to "+baseDirectory;
                LOGGER.error(message);
                throw new ImageDaoException(message);
            }
        }
    }

    @Override
    public String upload(File directory, File imageFile) throws ImageDaoException {
        return upload(directory,imageFile,false);
    }

    @Override
    public String upload(File directory, File imageFile, boolean deleteSource) throws ImageDaoException {

        File destination = TomwFileUtils.replaceBasedir(directory,imageFile, baseDirectory);
        System.out.println("directory = "+directory);
        System.out.println("imageFile="+imageFile);
        System.out.println("baseDirectory="+baseDirectory);
        System.out.println("destination="+destination);

//        String imageFileString = TomwStringUtils.backslash2slash(imageFile.getAbsolutePath().toString());
//        String directoryString = TomwStringUtils.backslash2slash(directory.getAbsolutePath().toString());
//
//        String baseSourceFileName = imageFileString.replace(directoryString,"");
//
//        File destination = new File(baseDirectory,baseSourceFileName);

        if(deleteSource){
            try {
                FileTransporter.moveFile(imageFile,destination);
                String id = idGenerator.generate();
                id2FileMap.put(id,destination);
                return id;
            } catch (IOException e) {
                String message="Failed to upload/move file "+imageFile+" to "+destination;
                LOGGER.error(message);
                throw new ImageDaoException(message);
            }
        }else{
            try {
                FileTransporter.copyFile(imageFile,destination);
                String id = idGenerator.generate();
                id2FileMap.put(id,destination);
                return id;
            } catch (IOException e) {
                String message="Failed to upload/copy file "+imageFile+" to "+destination;
                LOGGER.error(message);
                throw new ImageDaoException(message);
            }
        }
    }

    @Override
    public List<String> uploadAll(File directory, boolean deleteSource) throws ImageDaoException {
        List<String> result = new ArrayList<>();

        FileSelector imageSelector = new FileSelector(){
            @Override
            public boolean select(File f) {
                return MediaFileUtils.isImage(f);
            }
        };
        List<File> listOfImages = TomwFileUtils.getListOfFilesInDirectory(directory,imageSelector);
        for(File f : listOfImages){
            String id = upload(directory,f,deleteSource);
            result.add(id);
        }
        return result;
    }

    @Override
    public boolean imageExists(String id) {
        if(id2FileMap.containsKey(id)) {
            File f = id2FileMap.get(id);
            return f.exists();
        }else{
            return false;
        }
    }

    @Override
    public boolean deleteImage(String id) {
        if(id2FileMap.containsKey(id)) {
            File f = id2FileMap.get(id);
            TomwFileUtils.deleteFile(f);
            id2FileMap.remove(id);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean deleteImages(List<String> listOfIds) {
        boolean result = true;
        for(String id : listOfIds){
            result = result && deleteImage(id);
        }
        return result;
    }

    @Override
    public File getFile(String id) throws ImageDaoException {
        if(id2FileMap.containsKey(id)) {
            return id2FileMap.get(id);
        }else{
            return null;
        }
    }
}
