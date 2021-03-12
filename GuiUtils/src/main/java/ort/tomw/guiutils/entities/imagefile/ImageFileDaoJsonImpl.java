package ort.tomw.guiutils.entities.imagefile;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.image.Image;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.tomw.fileutils.TomwFileUtils;
import org.tomw.imagedao.ImageDaoException;
import org.tomw.mediautils.MediaFileUtils;
import org.tomw.utils.JsonFileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageFileDaoJsonImpl implements ImageFileDao {
    private final static Logger LOGGER = Logger.getLogger(ImageFileDaoJsonImpl.class.getName());

    public static final String IMAGE_FILES = "ImageFiles";

    private JsonFileUtils jsonFileUtils = new JsonFileUtils();
    private ObjectMapper objectMapper = new ObjectMapper();

    private File dataFile;
    private File imageDirectory;

    private Map<String,ImageFile> imageFiles = new HashMap<>();

    public ImageFileDaoJsonImpl(File dataFile, File imageDirectory){
        this.dataFile=dataFile;
        this.imageDirectory=imageDirectory;
        load();
    }
    public ImageFileDaoJsonImpl(String directoryName){
        this.dataFile=new File(directoryName);
        load();
    }

    private void load() {
        if(!this.imageDirectory.exists()){
            TomwFileUtils.mkdirs(this.imageDirectory);
        }
        if(!this.dataFile.exists()){
            try {
                FileUtils.writeStringToFile(this.dataFile,"{}", Charsets.UTF_8);
            } catch (IOException e) {
                LOGGER.error("Failed to initialize file "+this.dataFile);
            }
        }
        try {
            JSONObject json = jsonFileUtils.jsonObjectFromFile(this.dataFile);
            JSONArray imageFilesJson = (JSONArray) json.get(IMAGE_FILES);
            imageFiles = unpackImageFiles(imageFilesJson);
        } catch (IOException | ParseException e) {
            String message = "Failed to load data from file " + this.dataFile;
            LOGGER.fatal(message, e);
            throw new RuntimeException(message);
        }
    }

    private Map<String,ImageFile> unpackImageFiles(JSONArray jsonArray) throws IOException {
        Map<String, ImageFile> result = new HashMap<>();
        if (jsonArray != null) {
            for (Object o : jsonArray) {
                JSONObject jsonObject = (JSONObject) o;
                try {
                    ImageFile imageFile = objectMapper.readValue(jsonObject.toJSONString(), ImageFile.class);
                    File f = imageFile.getFile();
                    f=new File(imageDirectory,f.toString());
                    imageFile.setFile(f);
                    result.put(imageFile.getId(), imageFile);
                } catch (IOException e) {
                    String message = "Failed to decode planes string " + jsonObject.toJSONString();
                    LOGGER.error(message, e);
                    throw e;
                }
            }
        }
        return result;
    }

    @Override
    public List<ImageFile> getAll() {
        List<ImageFile> result = new ArrayList<>();
        for(ImageFile imageFile : imageFiles.values()){
            result.add(imageFile);
        }
       return result;
    }

    @Override
    public ImageFile getImageFile(String id) {
        if(imageFiles.containsKey(id)){
            return imageFiles.get(id);
        }else{
            return null;
        }
    }

    /**
     * Add image file and copy it to the current repository
     * file location must be already in repository
     * @param imageFile image file to be added
     * @return its id, once it has been inserted into dao
     */
    @Override
    public String add(ImageFile imageFile) {
        if(imageFile!=null && imageFile.getFile()!=null && imageFile.getFile().exists()){
            if(imageFiles.containsKey(imageFile.getId())){
                ImageFile existingImageFile = getImageFile(imageFile.getId());
                if(imageFile.equals(existingImageFile)){
                    return imageFile.getId();
                }else{
                    return null;
                }
            }else{
                imageFiles.put(imageFile.getId(),imageFile);
                return imageFile.getId();
            }
        }else{
            return null;
        }
    }

    @Override
    public void deleteFile(ImageFile imageFile) {
        if(imageFile.getFile()!=null){
            if(imageFile.getFile().exists()){
                imageFile.getFile().delete();
                imageFile.setFile(null);
            }
        }
    }

    @Override
    public void upload(ImageFile imageFile, File file) throws ImageDaoException{
        upload(imageFile, file, true);
    }

    @Override
    public void upload(ImageFile imageFile, File file, boolean deleteSource) throws ImageDaoException {
        if(file==null || !file.exists() || imageFile==null){
            LOGGER.error("imageFile="+imageFile+", file="+file+" is null or does not exist");
        }else{
            String baseName = file.getName();
            File fileInRepository = new File(this.imageDirectory,baseName);
            try {
                FileUtils.copyFileToDirectory(file,this.imageDirectory,true);
                imageFile.setFile(fileInRepository);
            } catch (IOException e) {
                String message="Failed to copy "+file+" to "+this.imageDirectory;
                LOGGER.error(message,e);
                throw new ImageDaoException(message);
            }

            if(deleteSource){
                file.delete();
            }
        }
    }

    @Override
    public ImageFile upload(File directory, File file, String name) throws ImageDaoException {
        return upload(directory,file,name,false);
    }

    @Override
    public ImageFile upload(File directory, File file, String name, String comment) throws ImageDaoException {
        return upload(directory, file,  name,  comment,  false) ;
        }

    @Override
    public ImageFile upload(File directory, File file, String name, boolean deleteSource) throws ImageDaoException {
        String id = upload(directory,file);
        ImageFile result = getImageFile(id);
        result.setName(name);
        if(deleteSource){
            TomwFileUtils.deleteFile(file);
        }
        return result;
    }

    @Override
    public ImageFile upload(File directory, File file, String name, String comment, boolean deleteSource) throws ImageDaoException {
        String id = upload(directory,file);
        ImageFile result = getImageFile(id);
        result.setName(name);
        result.setComment(comment);
        if(deleteSource){
            TomwFileUtils.deleteFile(file);
        }
        return result;
    }

    @Override
    public boolean contains(ImageFile imageFile) {
        return imageFiles.containsValue(imageFile);
    }

    @Override
    public boolean imageFileExists(String id) {
        return imageFiles.containsKey(id);
    }

    @Override
    public ImageFile delete(ImageFile imageFile) {
        if(contains(imageFile)){
            return imageFiles.remove(imageFile.getId());
        }else{
            return null;
        }
    }

    @Override
    public ImageFile deleteImageFile(String id) {
        if(imageFiles.containsKey(id)){
            ImageFile removedImage = imageFiles.remove(id);
            if(removedImage.getFile().exists()){
                TomwFileUtils.deleteFile(removedImage.getFile());
            }
            return removedImage;
        }else{
            return null;
        }
    }

    @Override
    public boolean containsImage(String id) {
       if(imageFiles.containsKey(id)){
            ImageFile imageFile = getImageFile(id);
            return imageFile.getFile()!=null && imageFile.getFile().exists();
       }else{
           return false;
       }

    }

    @Override
    public Image getImage(String id) {
        if(imageFiles.containsKey(id)){
            ImageFile imageFile = getImageFile(id);
            return new Image(imageFile.getFile().toURI().toString());
        }else{
            return null;
        }
    }

    @Override
    public String upload(Image image) throws ImageDaoException {
        //TODO implement later
        throw new RuntimeException("Not implemented");
    }

    /**
     * Upload file. Copy it to image directory while preserving original file
     * @param file file to be uploaded
     * @return id of the created ImageFile object
     * @throws ImageDaoException if something goes wrong
     */
    @Override
    public String upload(File file) throws ImageDaoException {
        return upload(file, false);
    }

    /**
     * Upload file. if deleteSource then after succesful upload delete the original source
     * @param file file to be uploaded
     * @param deleteSource whether to delete source file after upload is ok
     * @return id of the created ImageFile object
     * @throws ImageDaoException   if something goes wrong
     */
    @Override
    public String upload(File file, boolean deleteSource) throws ImageDaoException {
        if(file==null || !file.exists()){
            return null;
        }else{
            String baseName = file.getName();
            File fileInRepository = new File(this.imageDirectory,baseName);
            try {
                FileUtils.copyFileToDirectory(file,this.imageDirectory,true);
            } catch (IOException e) {
                String message="Failed to copy "+file+" to "+this.imageDirectory;
                LOGGER.error(message,e);
                throw new ImageDaoException(message);
            }
            ImageFile result = new ImageFile();
            result.setFile(fileInRepository);
            result.setName(baseName);
            result.setComment("Uploaded from "+file);

            add(result);

            if(deleteSource){
                file.delete();
            }

            return result.getId();
        }
    }

    @Override
    public String upload(File directory, File file) throws ImageDaoException {
        return upload(directory,file,false);
    }

    @Override
    public String upload(File directory, File file, boolean deleteSource) throws ImageDaoException {
        File destination = TomwFileUtils.replaceBasedir(directory,file,imageDirectory);
        try {
            FileUtils.copyFile(file,destination);
        } catch (IOException e) {
            String message = "Failed to upload file "+file +" to "+destination;
            LOGGER.error(message,e);
            throw new ImageDaoException(message);
        }
        if(!destination.exists()){
            String message = "Failed to copy file "+file +" to "+destination;
            throw new ImageDaoException(message);
        }
        ImageFile result = new ImageFile();
        result.setFile(destination);
        result.setName(destination.getName());
        result.setComment("Uploaded from "+file);
        add(result);
        if(deleteSource){
            TomwFileUtils.deleteFile(file);
        }
        return result.getId();
    }

    /**
     * Upload all image files in a directory
     * @param directory where to look for files to upload
     * @param deleteSource delete source files if true
     * @return list of Id of created image object files
     * @throws ImageDaoException if something is wrong
     */
    @Override
    public List<String> uploadAll(File directory, boolean deleteSource) throws ImageDaoException {
        List<String> results = new ArrayList<>();
        if(directory!=null) {
            for (File f : directory.listFiles()) {
                if (MediaFileUtils.isImage(f)) {
                    String id = upload(f, deleteSource);
                    if (id != null) {
                        results.add(id);
                    }
                }
            }
        }else{
            LOGGER.warn("directory is null");
        }
        return results;
    }

    @Override
    public boolean imageExists(String id) {
        if(this.imageFiles.containsKey(id)) {
            File f = imageFiles.get(id).getFile();
            return f.exists();
        }else{
            return false;
        }
    }

    /**
     * delete image file completely, both id from dao and the file from repository
     * @param id id of the object to be deleted
     * @return true if everything ok
     */
    @Override
    public boolean deleteImage(String id) {
        if(imageFiles.containsKey(id)) {
            File f = imageFiles.get(id).getFile();
            TomwFileUtils.deleteFile(f);
            imageFiles.remove(id);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean deleteImages(List<String> listOfIds) {
        boolean result = true;
        for(String id : listOfIds){
            boolean partialResult = deleteImage(id);
            result = result && partialResult;
        }
        return result;
    }

    @Override
    public File getFile(String id) throws ImageDaoException {
        ImageFile imageFile = getImageFile(id);
        if(imageFile==null){
            return null;
        }else{
            return imageFile.getFile();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void commit() throws ImageDaoException {
        JSONObject dataObject = new JSONObject();
        dataObject.put(IMAGE_FILES, packImageFiles(imageFiles));
        try {
            jsonFileUtils.jsonObject2File(dataObject, dataFile);
        } catch (IOException e) {
            String message = "Failed to write data to file " + this.dataFile;
            LOGGER.fatal(message, e);
            throw new RuntimeException(message);
        }
    }

    @SuppressWarnings("unchecked")
    private Object packImageFiles(Map<String, ImageFile> imageFiles) {
        JSONArray jsonArray = new JSONArray();
        for (ImageFile a : imageFiles.values()) {
            JSONObject json = a.toJSONObject();
            String fileName = (String)json.get(ImageFile.FILE_KEY);
            fileName = fileName.replace(this.imageDirectory.toString(),"");
            json.put(ImageFile.FILE_KEY,fileName);
            jsonArray.add(json);
        }
        return jsonArray;
    }
}
